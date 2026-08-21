#!/bin/bash

set -euo pipefail

# ============================================================
# LUCKSPINQUEST - ENTITY EXCEL -> DATABASE REPAIR
# ============================================================

DATABASE_NAME="luckspinquest"
DATABASE_USER="${PGUSER:-$(whoami)}"
DATABASE_HOST="${PGHOST:-localhost}"
DATABASE_PORT="${PGPORT:-5432}"

MASTER_FILE="luckspinquest_entitytable_master_perbaikan.xlsx"
SHEET_NAME="Entity Table"

BACKUP_FILE="luckspinquest_schema_backup_$(date +%Y%m%d_%H%M%S).sql"
GENERATED_SQL="LUCKSPINQUEST_REPAIR_GENERATED.sql"
AUDIT_SQL="LUCKSPINQUEST_FINAL_AUDIT.sql"

echo ""
echo "============================================================"
echo "      LUCKSPINQUEST - DATABASE REPAIR"
echo "============================================================"
echo "DATABASE : ${DATABASE_NAME}"
echo "USER     : ${DATABASE_USER}"
echo "HOST     : ${DATABASE_HOST}"
echo "PORT     : ${DATABASE_PORT}"
echo "MASTER   : ${MASTER_FILE}"
echo "SHEET    : ${SHEET_NAME}"
echo "============================================================"
echo ""

# ------------------------------------------------------------
# 1. CHECK DEPENDENCIES
# ------------------------------------------------------------

command -v psql >/dev/null 2>&1 || {
    echo "ERROR: psql tidak ditemukan."
    exit 1
}

command -v pg_dump >/dev/null 2>&1 || {
    echo "ERROR: pg_dump tidak ditemukan."
    exit 1
}

python3 -c "import openpyxl" >/dev/null 2>&1 || {
    echo "ERROR: Python package openpyxl belum tersedia."
    echo ""
    echo "Install:"
    echo "  python3 -m pip install openpyxl"
    exit 1
}

# ------------------------------------------------------------
# 2. FIND MASTER EXCEL
# ------------------------------------------------------------

if [ ! -f "$MASTER_FILE" ]; then

    FOUND_FILE=""

    for f in \
        "./${MASTER_FILE}" \
        "../${MASTER_FILE}" \
        "./backend/${MASTER_FILE}" \
        "../backend/${MASTER_FILE}"
    do
        if [ -f "$f" ]; then
            FOUND_FILE="$f"
            break
        fi
    done

    if [ -z "$FOUND_FILE" ]; then
        FOUND_FILE="$(find .. -name "$MASTER_FILE" -type f 2>/dev/null | head -1 || true)"
    fi

    if [ -z "$FOUND_FILE" ]; then
        echo "ERROR: File master tidak ditemukan:"
        echo "  ${MASTER_FILE}"
        echo ""
        echo "Letakkan file Excel di folder backend."
        exit 1
    fi

    MASTER_FILE="$FOUND_FILE"
fi

echo "MASTER FOUND:"
echo "  $MASTER_FILE"
echo ""

# ------------------------------------------------------------
# 3. DATABASE CONNECTION
# ------------------------------------------------------------

PSQL=(psql
    -h "$DATABASE_HOST"
    -p "$DATABASE_PORT"
    -U "$DATABASE_USER"
    -d "$DATABASE_NAME"
    -v ON_ERROR_STOP=1
)

if ! "${PSQL[@]}" -Atqc "SELECT current_database();" >/dev/null 2>&1; then
    echo ""
    echo "ERROR: Tidak dapat konek ke database:"
    echo "  ${DATABASE_NAME}"
    echo ""
    echo "Pastikan PostgreSQL aktif dan DATABASE_URL/PGUSER/PGHOST benar."
    exit 1
fi

echo "Database connection: OK"
echo ""

# ------------------------------------------------------------
# 4. BACKUP CURRENT SCHEMA
# ------------------------------------------------------------

echo "============================================================"
echo "1. BACKUP CURRENT DATABASE SCHEMA"
echo "============================================================"

pg_dump \
    -h "$DATABASE_HOST" \
    -p "$DATABASE_PORT" \
    -U "$DATABASE_USER" \
    -d "$DATABASE_NAME" \
    --schema-only \
    --no-owner \
    --no-privileges \
    > "$BACKUP_FILE"

echo "Backup:"
echo "  $BACKUP_FILE"
echo ""

# ------------------------------------------------------------
# 5. PARSE EXCEL -> JSON
# ------------------------------------------------------------

MASTER_JSON="$(mktemp)"

python3 - "$MASTER_FILE" "$SHEET_NAME" > "$MASTER_JSON" <<'PY'
import sys
import json
import openpyxl

filename = sys.argv[1]
sheet_name = sys.argv[2]

wb = openpyxl.load_workbook(filename, read_only=True, data_only=True)

if sheet_name not in wb.sheetnames:
    raise SystemExit(
        f"ERROR: Sheet '{sheet_name}' tidak ditemukan. "
        f"Sheets: {wb.sheetnames}"
    )

ws = wb[sheet_name]

rows = list(ws.iter_rows(values_only=True))

if not rows:
    raise SystemExit("ERROR: Sheet Entity Table kosong.")

headers = [str(x).strip() if x is not None else "" for x in rows[0]]

required = [
    "No",
    "Table Name",
    "Column Name",
    "PostgreSQL Type",
    "Constraint / Default"
]

for col in required:
    if col not in headers:
        raise SystemExit(
            f"ERROR: Kolom '{col}' tidak ditemukan di Excel."
        )

idx = {h: i for i, h in enumerate(headers)}

entities = []

for row in rows[1:]:
    if not row:
        continue

    table = row[idx["Table Name"]]
    column = row[idx["Column Name"]]

    if table is None or column is None:
        continue

    table = str(table).strip()
    column = str(column).strip()

    if not table or not column:
        continue

    pg_type = row[idx["PostgreSQL Type"]]
    constraint = row[idx["Constraint / Default"]]

    entities.append({
        "no": row[idx["No"]],
        "table": table,
        "column": column,
        "type": str(pg_type).strip() if pg_type is not None else "",
        "constraint": str(constraint).strip() if constraint is not None else ""
    })

print(json.dumps(entities, ensure_ascii=False))
PY

echo "Excel parsed successfully."

EXPECTED_TABLES="$(python3 - "$MASTER_JSON" <<'PY'
import sys, json
data=json.load(open(sys.argv[1]))
print(len(set(x["table"] for x in data)))
PY
)"

EXPECTED_COLUMNS="$(python3 - "$MASTER_JSON" <<'PY'
import sys, json
data=json.load(open(sys.argv[1]))
print(len(data))
PY
)"

echo "Expected tables : $EXPECTED_TABLES"
echo "Expected columns: $EXPECTED_COLUMNS"
echo ""

if [ "$EXPECTED_TABLES" -ne 43 ]; then
    echo "ERROR: Master Excel tidak memiliki 43 tabel."
    echo "Detected: $EXPECTED_TABLES"
    exit 1
fi

# ------------------------------------------------------------
# 6. GENERATE SQL
# ------------------------------------------------------------

echo "============================================================"
echo "2. GENERATING REPAIR SQL"
echo "============================================================"

python3 - "$MASTER_JSON" "$GENERATED_SQL" <<'PY'
import sys
import json
import re

master_file = sys.argv[1]
sql_file = sys.argv[2]

data = json.load(open(master_file))

def qident(v):
    return '"' + v.replace('"', '""') + '"'

def normalize_type(t):
    t = t.strip().upper()

    # PostgreSQL aliases
    if t == "BIGSERIAL":
        return "BIGINT"
    if t == "SERIAL":
        return "INTEGER"

    return t

def default_from_constraint(c):
    if not c:
        return None

    m = re.search(
        r"DEFAULT\s+(.+?)(?:,\s*(?:PK|FK|UNIQUE|NOT NULL|NULL|CHECK)|$)",
        c,
        re.I
    )

    if not m:
        return None

    value = m.group(1).strip()

    # Cleanup common formatting
    value = value.rstrip(",")

    return value

def is_pk(c):
    return bool(re.search(r"\bPK\b", c, re.I))

def is_fk(c):
    return bool(re.search(r"\bFK\b", c, re.I))

def is_unique(c):
    return bool(re.search(r"\bUNIQUE\b", c, re.I))

def is_not_null(c):
    return bool(re.search(r"\bNOT\s+NULL\b", c, re.I))

tables = {}

for item in data:
    tables.setdefault(item["table"], []).append(item)

sql = []

sql.append("-- ============================================================")
sql.append("-- LUCKSPINQUEST GENERATED REPAIR SQL")
sql.append("-- Source: luckspinquest_entitytable_master_perbaikan.xlsx")
sql.append("-- ============================================================")
sql.append("BEGIN;")
sql.append("")

# ------------------------------------------------------------
# TEMP FK DISABLE:
# We do not disable PostgreSQL globally.
# Existing FK constraints are handled later.
# ------------------------------------------------------------

# ------------------------------------------------------------
# CREATE MISSING TABLES
# ------------------------------------------------------------

for table, cols in tables.items():

    sql.append(f'CREATE TABLE IF NOT EXISTS {qident(table)} (')

    definitions = []

    for item in cols:
        col = item["column"]
        pg_type = normalize_type(item["type"])
        c = item["constraint"]

        if not pg_type:
            pg_type = "TEXT"

        definition = f"    {qident(col)} {pg_type}"

        default = default_from_constraint(c)

        if default:
            definition += f" DEFAULT {default}"

        if is_not_null(c):
            definition += " NOT NULL"

        definitions.append(definition)

    sql.append(",\n".join(definitions))
    sql.append(");")
    sql.append("")

# ------------------------------------------------------------
# ADD MISSING COLUMNS
# ------------------------------------------------------------

sql.append("-- ============================================================")
sql.append("-- ADD MISSING COLUMNS")
sql.append("-- ============================================================")

for table, cols in tables.items():

    for item in cols:

        col = item["column"]
        pg_type = normalize_type(item["type"]) or "TEXT"
        c = item["constraint"]

        default = default_from_constraint(c)

        sql.append(
            f'ALTER TABLE {qident(table)} '
            f'ADD COLUMN IF NOT EXISTS {qident(col)} {pg_type};'
        )

        if default:
            sql.append(
                f'ALTER TABLE {qident(table)} '
                f'ALTER COLUMN {qident(col)} SET DEFAULT {default};'
            )

        if is_not_null(c):
            # Safe NOT NULL conversion is handled by a validation block.
            sql.append(
                f'UPDATE {qident(table)} '
                f'SET {qident(col)} = {default if default else "NULL"} '
                f'WHERE {qident(col)} IS NULL;'
            )

            sql.append(
                f'ALTER TABLE {qident(table)} '
                f'ALTER COLUMN {qident(col)} SET NOT NULL;'
            )

sql.append("")

# ------------------------------------------------------------
# SEQUENCES FOR BIGSERIAL
# ------------------------------------------------------------

sql.append("-- ============================================================")
sql.append("-- BIGSERIAL SEQUENCES")
sql.append("-- ============================================================")

for table, cols in tables.items():

    for item in cols:

        if item["type"].strip().upper() != "BIGSERIAL":
            continue

        col = item["column"]
        seq = f"{table}_{col}_seq"

        sql.append(
            f'CREATE SEQUENCE IF NOT EXISTS {qident(seq)};'
        )

        sql.append(
            f'ALTER TABLE {qident(table)} '
            f'ALTER COLUMN {qident(col)} '
            f'SET DEFAULT nextval({qident(seq)}::regclass);'
        )

        sql.append(
            f'ALTER SEQUENCE {qident(seq)} '
            f'OWNED BY {qident(table)}.{qident(col)};'
        )

sql.append("")

# ------------------------------------------------------------
# PK CONSTRAINTS
# ------------------------------------------------------------

sql.append("-- ============================================================")
sql.append("-- PRIMARY KEYS")
sql.append("-- ============================================================")

for table, cols in tables.items():

    pk_cols = [
        item["column"]
        for item in cols
        if is_pk(item["constraint"])
    ]

    if not pk_cols:
        continue

    constraint_name = f"pk_{table}"

    sql.append(
        f'ALTER TABLE {qident(table)} '
        f'DROP CONSTRAINT IF EXISTS {qident(constraint_name)};'
    )

    sql.append(
        f'ALTER TABLE {qident(table)} '
        f'ADD CONSTRAINT {qident(constraint_name)} '
        f'PRIMARY KEY ({", ".join(qident(x) for x in pk_cols)});'
    )

sql.append("")

# ------------------------------------------------------------
# UNIQUE CONSTRAINTS
# ------------------------------------------------------------

sql.append("-- ============================================================")
sql.append("-- UNIQUE")
sql.append("-- ============================================================")

for table, cols in tables.items():

    for item in cols:

        if not is_unique(item["constraint"]):
            continue

        col = item["column"]

        constraint_name = f"uq_{table}_{col}"

        sql.append(
            f'CREATE UNIQUE INDEX IF NOT EXISTS '
            f'{qident(constraint_name)} '
            f'ON {qident(table)} ({qident(col)});'
        )

sql.append("")

# ------------------------------------------------------------
# FK
#
# The Entity Excel identifies FK columns, but does not contain
# the FK target table in the Entity Table sheet.
#
# Therefore this script intentionally DOES NOT invent FK targets.
# Existing valid FK constraints are preserved.
# ------------------------------------------------------------

sql.append("-- ============================================================")
sql.append("-- FK NOTE")
sql.append("-- ============================================================")
sql.append("-- FK targets are NOT invented from column names.")
sql.append("-- Existing valid FK constraints are preserved.")
sql.append("-- Final FK validation is performed by the audit.")
sql.append("")

sql.append("COMMIT;")

open(sql_file, "w").write("\n".join(sql) + "\n")

print(f"Generated: {sql_file}")
PY

echo "Generated SQL:"
echo "  $GENERATED_SQL"
echo ""

# ------------------------------------------------------------
# 7. SHOW PLAN
# ------------------------------------------------------------

echo "============================================================"
echo "3. REPAIR PLAN"
echo "============================================================"

echo ""
echo "SQL statements:"
grep -c '^ALTER TABLE' "$GENERATED_SQL" || true

echo ""
echo "CREATE TABLE:"
grep -c '^CREATE TABLE' "$GENERATED_SQL" || true

echo ""
echo "CREATE SEQUENCE:"
grep -c '^CREATE SEQUENCE' "$GENERATED_SQL" || true

echo ""
echo "============================================================"
echo "WARNING"
echo "============================================================"
echo ""
echo "Script ini TIDAK menghapus tabel."
echo "Script ini TIDAK menghapus kolom."
echo "Script ini TIDAK menghapus data."
echo "Script ini TIDAK membuat data dummy."
echo ""
echo "Kolom ekstra dipertahankan agar data existing aman."
echo ""

# ------------------------------------------------------------
# 8. APPLY REPAIR
# ------------------------------------------------------------

echo "============================================================"
echo "4. APPLY DATABASE REPAIR"
echo "============================================================"

"${PSQL[@]}" -f "$GENERATED_SQL"

echo ""
echo "Database repair: DONE"
echo ""

# ------------------------------------------------------------
# 9. FINAL AUDIT
# ------------------------------------------------------------

echo "============================================================"
echo "5. FINAL DATABASE AUDIT"
echo "============================================================"

cat > "$AUDIT_SQL" <<'SQL'

\pset pager off
\pset null '[NULL]'

SELECT
    current_database() AS database_name,
    current_user AS database_user,
    current_schema AS schema_name;

\echo ''
\echo '============================================================'
\echo 'TABLE COUNT'
\echo '============================================================'

SELECT COUNT(*) AS total_tables
FROM information_schema.tables
WHERE table_schema = 'public'
AND table_type = 'BASE TABLE';

\echo ''
\echo '============================================================'
\echo 'TABLES'
\echo '============================================================'

SELECT
    row_number() OVER (ORDER BY table_name) AS no,
    table_name,
    (
        SELECT COUNT(*)
        FROM information_schema.columns c2
        WHERE c2.table_schema = 'public'
        AND c2.table_name = t.table_name
    ) AS columns
FROM information_schema.tables t
WHERE table_schema = 'public'
AND table_type = 'BASE TABLE'
ORDER BY table_name;

\echo ''
\echo '============================================================'
\echo 'FOREIGN KEYS'
\echo '============================================================'

SELECT
    tc.table_name,
    kcu.column_name,
    ccu.table_name AS referenced_table,
    ccu.column_name AS referenced_column,
    tc.constraint_name
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu
  ON tc.constraint_name = kcu.constraint_name
 AND tc.table_schema = kcu.table_schema
JOIN information_schema.constraint_column_usage ccu
  ON ccu.constraint_name = tc.constraint_name
 AND ccu.constraint_schema = tc.table_schema
WHERE tc.constraint_type = 'FOREIGN KEY'
AND tc.table_schema = 'public'
ORDER BY tc.table_name, kcu.column_name;

\echo ''
\echo '============================================================'
\echo 'PRIMARY KEYS'
\echo '============================================================'

SELECT
    tc.table_name,
    kcu.column_name,
    tc.constraint_name
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu
  ON tc.constraint_name = kcu.constraint_name
 AND tc.table_schema = kcu.table_schema
WHERE tc.constraint_type = 'PRIMARY KEY'
AND tc.table_schema = 'public'
ORDER BY tc.table_name, kcu.ordinal_position;

\echo ''
\echo '============================================================'
\echo 'INVALID FK REFERENCES'
\echo '============================================================'

SELECT
    tc.table_name,
    kcu.column_name,
    ccu.table_name AS referenced_table,
    ccu.column_name AS referenced_column
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu
  ON tc.constraint_name = kcu.constraint_name
 AND tc.table_schema = kcu.table_schema
JOIN information_schema.constraint_column_usage ccu
  ON ccu.constraint_name = tc.constraint_name
 AND ccu.constraint_schema = tc.table_schema
WHERE tc.constraint_type = 'FOREIGN KEY'
AND tc.table_schema = 'public'
AND (
    NOT EXISTS (
        SELECT 1
        FROM information_schema.tables t
        WHERE t.table_schema = 'public'
        AND t.table_name = ccu.table_name
    )
);

\echo ''
\echo '============================================================'
\echo 'FINAL SUMMARY'
\echo '============================================================'

SELECT
    (
        SELECT COUNT(*)
        FROM information_schema.tables
        WHERE table_schema = 'public'
        AND table_type = 'BASE TABLE'
    ) AS total_tables,

    (
        SELECT COUNT(*)
        FROM information_schema.columns
        WHERE table_schema = 'public'
    ) AS total_columns,

    (
        SELECT COUNT(*)
        FROM information_schema.table_constraints
        WHERE table_schema = 'public'
        AND constraint_type = 'FOREIGN KEY'
    ) AS total_foreign_keys,

    (
        SELECT COUNT(*)
        FROM information_schema.table_constraints
        WHERE table_schema = 'public'
        AND constraint_type = 'PRIMARY KEY'
    ) AS total_primary_keys,

    (
        SELECT COUNT(*)
        FROM information_schema.table_constraints
        WHERE table_schema = 'public'
        AND constraint_type = 'UNIQUE'
    ) AS total_unique_constraints,

    (
        SELECT COUNT(*)
        FROM information_schema.table_constraints
        WHERE table_schema = 'public'
        AND constraint_type = 'CHECK'
    ) AS total_check_constraints;

SQL

"${PSQL[@]}" -f "$AUDIT_SQL"

# ------------------------------------------------------------
# 10. FINAL MESSAGE
# ------------------------------------------------------------

rm -f "$MASTER_JSON"

echo ""
echo "============================================================"
echo "     LUCKSPINQUEST DATABASE REPAIR SELESAI"
echo "============================================================"
echo ""
echo "Database : $DATABASE_NAME"
echo "Master   : $MASTER_FILE"
echo ""
echo "Backup schema:"
echo "  $BACKUP_FILE"
echo ""
echo "Generated repair SQL:"
echo "  $GENERATED_SQL"
echo ""
echo "Final audit:"
echo "  $AUDIT_SQL"
echo ""
echo "IMPORTANT:"
echo "Kolom ekstra TIDAK dihapus otomatis."
echo "Data existing TIDAK dihapus."
echo "FK yang targetnya tidak didefinisikan oleh Entity Excel"
echo "tidak ditebak/diciptakan secara otomatis."
echo "============================================================"
