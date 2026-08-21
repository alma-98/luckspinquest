#!/bin/bash

set -u

DATABASE="luckspinquest"
EXPECTED_OWNER="admin"
MASTER_FILE_NAME="LuckySpinQuest_ERD_V2_Full.xlsx"

echo ""
echo "============================================================"
echo "       LUCKSPINQUEST - FULL ENTITY vs DATABASE AUDIT"
echo "============================================================"
echo "DATABASE : $DATABASE"
echo "MASTER   : $MASTER_FILE_NAME"
echo "MODE     : READ ONLY"
echo "============================================================"
echo ""

# ============================================================
# FIND MASTER EXCEL
# ============================================================

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

MASTER_FILE=""

SEARCH_PATHS=(
    "$SCRIPT_DIR/$MASTER_FILE_NAME"
    "$SCRIPT_DIR/../$MASTER_FILE_NAME"
    "$SCRIPT_DIR/../../$MASTER_FILE_NAME"
    "$PWD/$MASTER_FILE_NAME"
    "$PWD/../$MASTER_FILE_NAME"
    "$PWD/../../$MASTER_FILE_NAME"
)

for FILE in "${SEARCH_PATHS[@]}"; do
    if [ -f "$FILE" ]; then
        MASTER_FILE="$(cd "$(dirname "$FILE")" && pwd)/$(basename "$FILE")"
        break
    fi
done

if [ -z "$MASTER_FILE" ]; then
    echo ""
    echo "ERROR: MASTER EXCEL TIDAK DITEMUKAN"
    echo ""
    echo "Nama yang dicari:"
    echo "  $MASTER_FILE_NAME"
    echo ""
    echo "Letakkan file tersebut di:"
    echo "  $SCRIPT_DIR/"
    echo ""
    echo "Atau cari dengan:"
    echo "  find .. -name '$MASTER_FILE_NAME' -print"
    echo ""
    exit 1
fi

echo "MASTER DITEMUKAN:"
echo "$MASTER_FILE"
echo ""

# ============================================================
# CHECK PYTHON
# ============================================================

PYTHON_CMD=""

if command -v python3 >/dev/null 2>&1; then
    PYTHON_CMD="python3"
elif command -v python >/dev/null 2>&1; then
    PYTHON_CMD="python"
else
    echo "ERROR: Python tidak ditemukan."
    exit 1
fi

# ============================================================
# CHECK OPENPYXL
# ============================================================

if ! "$PYTHON_CMD" -c "import openpyxl" >/dev/null 2>&1; then
    echo "ERROR: Python module openpyxl belum tersedia."
    echo ""
    echo "Install dengan:"
    echo "  $PYTHON_CMD -m pip install openpyxl"
    echo ""
    exit 1
fi

# ============================================================
# CREATE TEMP MASTER DATA
# ============================================================

TMP_DIR="$(mktemp -d)"

cleanup() {
    rm -rf "$TMP_DIR"
}
trap cleanup EXIT

MASTER_TABLES="$TMP_DIR/master_tables.tsv"
MASTER_COLUMNS="$TMP_DIR/master_columns.tsv"
MASTER_RELATIONS="$TMP_DIR/master_relations.tsv"

echo "Membaca Excel Master..."
echo ""

"$PYTHON_CMD" - "$MASTER_FILE" "$MASTER_TABLES" "$MASTER_COLUMNS" "$MASTER_RELATIONS" <<'PY'
import sys
import csv
import re
from openpyxl import load_workbook

xlsx = sys.argv[1]
tables_file = sys.argv[2]
columns_file = sys.argv[3]
relations_file = sys.argv[4]

wb = load_workbook(xlsx, read_only=True, data_only=True)

# ============================================================
# FIND COLUMNS SHEET
# ============================================================

sheet = None

for name in wb.sheetnames:
    if name.strip().lower() == "columns":
        sheet = wb[name]
        break

if sheet is None:
    for name in wb.sheetnames:
        if "column" in name.lower():
            sheet = wb[name]
            break

if sheet is None:
    print("ERROR: Sheet Columns tidak ditemukan.", file=sys.stderr)
    print("Available sheets:", wb.sheetnames, file=sys.stderr)
    sys.exit(2)

rows = sheet.iter_rows(values_only=True)

try:
    header = next(rows)
except StopIteration:
    print("ERROR: Sheet Columns kosong.", file=sys.stderr)
    sys.exit(3)

header = [
    str(x).strip() if x is not None else ""
    for x in header
]

index = {
    name.lower(): i
    for i, name in enumerate(header)
}

required = [
    "table",
    "column",
    "data type",
    "key",
    "nullable",
    "unique",
    "default",
    "reference"
]

missing = [
    x for x in required
    if x not in index
]

if missing:
    print(
        "ERROR: Kolom Master tidak lengkap: " +
        ", ".join(missing),
        file=sys.stderr
    )
    sys.exit(4)

def val(row, key):
    i = index.get(key)
    if i is None or i >= len(row):
        return ""
    x = row[i]
    return "" if x is None else str(x).strip()

records = []

for row in rows:
    table = val(row, "table")
    column = val(row, "column")

    if not table or not column:
        continue

    records.append({
        "table": table,
        "column": column,
        "type": val(row, "data type"),
        "key": val(row, "key"),
        "nullable": val(row, "nullable"),
        "unique": val(row, "unique"),
        "default": val(row, "default"),
        "reference": val(row, "reference")
    })

# ============================================================
# UNIQUE TABLE LIST
# ============================================================

tables = []

seen = set()

for r in records:
    t = r["table"]

    if t not in seen:
        seen.add(t)
        tables.append(t)

# ============================================================
# WRITE TABLE MASTER
# ============================================================

with open(tables_file, "w", newline="", encoding="utf-8") as f:
    writer = csv.writer(
        f,
        delimiter="\t",
        lineterminator="\n"
    )

    for no, table in enumerate(tables, 1):
        writer.writerow([no, table])

# ============================================================
# WRITE COLUMN MASTER
# ============================================================

with open(columns_file, "w", newline="", encoding="utf-8") as f:
    writer = csv.writer(
        f,
        delimiter="\t",
        lineterminator="\n"
    )

    for r in records:
        writer.writerow([
            r["table"],
            r["column"],
            r["type"],
            r["key"],
            r["nullable"],
            r["unique"],
            r["default"],
            r["reference"]
        ])

# ============================================================
# WRITE RELATION MASTER
# ============================================================

with open(relations_file, "w", newline="", encoding="utf-8") as f:
    writer = csv.writer(
        f,
        delimiter="\t",
        lineterminator="\n"
    )

    for r in records:
        ref = r["reference"]

        if ref and (
            "FK" in r["key"].upper()
            or ref
        ):
            writer.writerow([
                r["table"],
                r["column"],
                r["reference"]
            ])

print(f"MASTER_TABLES={len(tables)}")
print(f"MASTER_COLUMNS={len(records)}")

PY

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Gagal membaca Excel Master."
    exit 1
fi

MASTER_TABLE_COUNT="$(wc -l < "$MASTER_TABLES" | tr -d ' ')"
MASTER_COLUMN_COUNT="$(wc -l < "$MASTER_COLUMNS" | tr -d ' ')"
MASTER_RELATION_COUNT="$(wc -l < "$MASTER_RELATIONS" | tr -d ' ')"

echo "Master tables     : $MASTER_TABLE_COUNT"
echo "Master columns    : $MASTER_COLUMN_COUNT"
echo "Master references : $MASTER_RELATION_COUNT"
echo ""

# ============================================================
# GENERATE SQL MASTER VALUES
# ============================================================

MASTER_TABLE_SQL="$TMP_DIR/master_tables.sql"

"$PYTHON_CMD" - "$MASTER_TABLES" "$MASTER_TABLE_SQL" <<'PY'
import sys
import csv

src = sys.argv[1]
dst = sys.argv[2]

with open(src, encoding="utf-8") as f, open(dst, "w", encoding="utf-8") as out:
    reader = csv.reader(f, delimiter="\t")

    for no, table in reader:
        table = table.replace("'", "''")
        out.write(
            f"({int(no)}, '{table}'),\n"
        )
PY

# Remove final comma and add semicolon-compatible content
sed -i '' '$ s/,$//' "$MASTER_TABLE_SQL" 2>/dev/null || \
sed -i '$ s/,$//' "$MASTER_TABLE_SQL"

# ============================================================
# RUN POSTGRESQL AUDIT
# ============================================================

psql -d "$DATABASE" -v ON_ERROR_STOP=1 \
    -v master_table_count="$MASTER_TABLE_COUNT" \
    -v master_column_count="$MASTER_COLUMN_COUNT" \
    -v expected_owner="$EXPECTED_OWNER" <<SQL

\pset pager off
\timing off

BEGIN READ ONLY;

-- ============================================================
-- TEMP MASTER TABLE
-- ============================================================

CREATE TEMP TABLE tmp_master_tables (
    no INTEGER,
    table_name TEXT
) ON COMMIT DROP;

\copy tmp_master_tables(no, table_name) FROM '$MASTER_TABLES' WITH (FORMAT csv, DELIMITER E'\t');

-- ============================================================
-- DATABASE INFORMATION
-- ============================================================

\echo ''
\echo '============================================================'
\echo '1. DATABASE INFORMATION'
\echo '============================================================'

SELECT
    current_database() AS database_name,
    current_user AS database_user,
    current_schema() AS schema_name,
    version() AS postgresql_version;

-- ============================================================
-- MASTER COUNT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '2. MASTER COUNT'
\echo '============================================================'

SELECT
    $MASTER_TABLE_COUNT::INTEGER AS master_tables,
    $MASTER_COLUMN_COUNT::INTEGER AS master_columns;

-- ============================================================
-- ACTUAL TABLE COUNT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '3. ACTUAL PUBLIC TABLE COUNT'
\echo '============================================================'

SELECT COUNT(*) AS actual_tables
FROM pg_tables
WHERE schemaname = 'public';

-- ============================================================
-- TABLE EXISTENCE
-- ============================================================

\echo ''
\echo '============================================================'
\echo '4. MASTER TABLE vs DATABASE'
\echo '============================================================'

SELECT
    m.no,
    m.table_name,
    CASE
        WHEN p.tablename IS NULL
        THEN 'MISSING'
        ELSE 'EXISTS'
    END AS status,
    COALESCE(p.tableowner, '-') AS owner,
    COALESCE(c.column_count, 0) AS actual_columns
FROM tmp_master_tables m

LEFT JOIN pg_tables p
    ON p.schemaname = 'public'
   AND p.tablename = m.table_name

LEFT JOIN (
    SELECT
        table_name,
        COUNT(*) AS column_count
    FROM information_schema.columns
    WHERE table_schema = 'public'
    GROUP BY table_name
) c
    ON c.table_name = m.table_name

ORDER BY m.no;

-- ============================================================
-- MISSING TABLES
-- ============================================================

\echo ''
\echo '============================================================'
\echo '5. MISSING TABLES'
\echo '============================================================'

SELECT
    m.no,
    m.table_name
FROM tmp_master_tables m
LEFT JOIN pg_tables p
    ON p.schemaname = 'public'
   AND p.tablename = m.table_name
WHERE p.tablename IS NULL
ORDER BY m.no;

-- ============================================================
-- EXTRA TABLES
-- ============================================================

\echo ''
\echo '============================================================'
\echo '6. EXTRA PUBLIC TABLES'
\echo '============================================================'

SELECT
    p.tablename AS table_name,
    p.tableowner AS owner
FROM pg_tables p
LEFT JOIN tmp_master_tables m
    ON m.table_name = p.tablename
WHERE p.schemaname = 'public'
  AND m.table_name IS NULL
ORDER BY p.tablename;

-- ============================================================
-- COLUMN COUNT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '7. COLUMN COUNT PER MASTER TABLE'
\echo '============================================================'

SELECT
    m.no,
    m.table_name,
    COUNT(c.column_name) AS actual_column_count
FROM tmp_master_tables m
LEFT JOIN information_schema.columns c
    ON c.table_schema = 'public'
   AND c.table_name = m.table_name
GROUP BY
    m.no,
    m.table_name
ORDER BY m.no;

-- ============================================================
-- FULL ACTUAL COLUMN STRUCTURE
-- ============================================================

\echo ''
\echo '============================================================'
\echo '8. FULL ACTUAL COLUMN STRUCTURE'
\echo '============================================================'

SELECT
    m.no,
    c.table_name,
    c.ordinal_position,
    c.column_name,
    CASE
        WHEN c.data_type = 'character varying'
             AND c.character_maximum_length IS NOT NULL
        THEN
            'VARCHAR(' ||
            c.character_maximum_length ||
            ')'

        WHEN c.data_type = 'numeric'
             AND c.numeric_precision IS NOT NULL
        THEN
            'NUMERIC(' ||
            c.numeric_precision ||
            ',' ||
            COALESCE(c.numeric_scale, 0) ||
            ')'

        WHEN c.data_type = 'timestamp without time zone'
        THEN 'TIMESTAMP'

        ELSE UPPER(c.data_type)
    END AS postgres_type,

    c.is_nullable,
    c.column_default

FROM tmp_master_tables m
JOIN information_schema.columns c
    ON c.table_schema = 'public'
   AND c.table_name = m.table_name

ORDER BY
    m.no,
    c.ordinal_position;

-- ============================================================
-- PRIMARY KEYS
-- ============================================================

\echo ''
\echo '============================================================'
\echo '9. PRIMARY KEYS'
\echo '============================================================'

SELECT
    tc.table_name,
    tc.constraint_name,
    string_agg(
        kcu.column_name,
        ', '
        ORDER BY kcu.ordinal_position
    ) AS columns
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu
    ON tc.constraint_name = kcu.constraint_name
   AND tc.table_schema = kcu.table_schema
   AND tc.table_name = kcu.table_name
WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'PRIMARY KEY'
GROUP BY
    tc.table_name,
    tc.constraint_name
ORDER BY tc.table_name;

-- ============================================================
-- UNIQUE
-- ============================================================

\echo ''
\echo '============================================================'
\echo '10. UNIQUE CONSTRAINTS'
\echo '============================================================'

SELECT
    tc.table_name,
    tc.constraint_name,
    string_agg(
        kcu.column_name,
        ', '
        ORDER BY kcu.ordinal_position
    ) AS columns
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu
    ON tc.constraint_name = kcu.constraint_name
   AND tc.table_schema = kcu.table_schema
   AND tc.table_name = kcu.table_name
WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'UNIQUE'
GROUP BY
    tc.table_name,
    tc.constraint_name
ORDER BY
    tc.table_name,
    tc.constraint_name;

-- ============================================================
-- CHECK CONSTRAINTS
-- ============================================================

\echo ''
\echo '============================================================'
\echo '11. CHECK CONSTRAINTS'
\echo '============================================================'

SELECT
    tc.table_name,
    tc.constraint_name,
    pg_get_constraintdef(pc.oid) AS definition
FROM information_schema.table_constraints tc
JOIN pg_constraint pc
    ON pc.conname = tc.constraint_name
   AND pc.conrelid = (
       tc.table_schema || '.' || tc.table_name
   )::regclass
WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'CHECK'
ORDER BY
    tc.table_name,
    tc.constraint_name;

-- ============================================================
-- ALL FOREIGN KEYS
-- ============================================================

\echo ''
\echo '============================================================'
\echo '12. ALL FOREIGN KEYS'
\echo '============================================================'

SELECT
    tc.table_name AS source_table,
    tc.constraint_name,
    kcu.column_name AS source_column,
    ccu.table_name AS referenced_table,
    ccu.column_name AS referenced_column,
    rc.delete_rule,
    rc.update_rule

FROM information_schema.table_constraints tc

JOIN information_schema.key_column_usage kcu
    ON tc.constraint_name = kcu.constraint_name
   AND tc.table_schema = kcu.table_schema
   AND tc.table_name = kcu.table_name

JOIN information_schema.constraint_column_usage ccu
    ON ccu.constraint_name = tc.constraint_name
   AND ccu.constraint_schema = tc.table_schema

LEFT JOIN information_schema.referential_constraints rc
    ON rc.constraint_name = tc.constraint_name
   AND rc.constraint_schema = tc.table_schema

WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'FOREIGN KEY'

ORDER BY
    tc.table_name,
    tc.constraint_name;

-- ============================================================
-- FK COUNT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '13. FK COUNT'
\echo '============================================================'

SELECT
    COUNT(*) AS total_foreign_keys
FROM information_schema.table_constraints
WHERE table_schema = 'public'
  AND constraint_type = 'FOREIGN KEY';

-- ============================================================
-- FK INTEGRITY
-- ============================================================

\echo ''
\echo '============================================================'
\echo '14. FK INTEGRITY'
\echo '============================================================'

SELECT
    COUNT(*) AS invalid_fk_references
FROM information_schema.table_constraints tc
LEFT JOIN information_schema.constraint_column_usage ccu
    ON ccu.constraint_name = tc.constraint_name
   AND ccu.constraint_schema = tc.table_schema
WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'FOREIGN KEY'
  AND ccu.table_name IS NULL;

-- ============================================================
-- OWNER
-- ============================================================

\echo ''
\echo '============================================================'
\echo '15. OWNER CHECK'
\echo '============================================================'

SELECT
    m.no,
    m.table_name,
    p.tableowner AS owner,
    CASE
        WHEN p.tableowner = '$EXPECTED_OWNER'
        THEN 'PASS'
        ELSE 'REVIEW'
    END AS status
FROM tmp_master_tables m
JOIN pg_tables p
    ON p.schemaname = 'public'
   AND p.tablename = m.table_name
ORDER BY m.no;

-- ============================================================
-- SEQUENCES
-- ============================================================

\echo ''
\echo '============================================================'
\echo '16. PUBLIC SEQUENCES'
\echo '============================================================'

SELECT
    sequence_name
FROM information_schema.sequences
WHERE sequence_schema = 'public'
ORDER BY sequence_name;

-- ============================================================
-- INDEXES
-- ============================================================

\echo ''
\echo '============================================================'
\echo '17. PUBLIC INDEXES'
\echo '============================================================'

SELECT
    tablename,
    indexname,
    indexdef
FROM pg_indexes
WHERE schemaname = 'public'
ORDER BY
    tablename,
    indexname;

-- ============================================================
-- TABLE SIZES
-- ============================================================

\echo ''
\echo '============================================================'
\echo '18. TABLE SIZE'
\echo '============================================================'

SELECT
    tablename,
    pg_size_pretty(
        pg_total_relation_size(
            ('public.' || tablename)::regclass
        )
    ) AS total_size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY
    pg_total_relation_size(
        ('public.' || tablename)::regclass
    ) DESC;

-- ============================================================
-- COUNTS
-- ============================================================

\echo ''
\echo '============================================================'
\echo '19. CONSTRAINT SUMMARY'
\echo '============================================================'

SELECT
    COUNT(*) FILTER (
        WHERE constraint_type = 'PRIMARY KEY'
    ) AS primary_keys,

    COUNT(*) FILTER (
        WHERE constraint_type = 'UNIQUE'
    ) AS unique_constraints,

    COUNT(*) FILTER (
        WHERE constraint_type = 'CHECK'
    ) AS check_constraints,

    COUNT(*) FILTER (
        WHERE constraint_type = 'FOREIGN KEY'
    ) AS foreign_keys

FROM information_schema.table_constraints
WHERE table_schema = 'public';

-- ============================================================
-- FINAL SUMMARY
-- ============================================================

\echo ''
\echo '============================================================'
\echo '                 FINAL SUMMARY'
\echo '============================================================'

WITH
actual_tables AS (
    SELECT COUNT(*) AS n
    FROM pg_tables
    WHERE schemaname = 'public'
),

missing_tables AS (
    SELECT COUNT(*) AS n
    FROM tmp_master_tables m
    LEFT JOIN pg_tables p
        ON p.schemaname = 'public'
       AND p.tablename = m.table_name
    WHERE p.tablename IS NULL
),

extra_tables AS (
    SELECT COUNT(*) AS n
    FROM pg_tables p
    LEFT JOIN tmp_master_tables m
        ON m.table_name = p.tablename
    WHERE p.schemaname = 'public'
      AND m.table_name IS NULL
),

actual_columns AS (
    SELECT COUNT(*) AS n
    FROM information_schema.columns
    WHERE table_schema = 'public'
),

primary_keys AS (
    SELECT COUNT(*) AS n
    FROM information_schema.table_constraints
    WHERE table_schema = 'public'
      AND constraint_type = 'PRIMARY KEY'
),

unique_constraints AS (
    SELECT COUNT(*) AS n
    FROM information_schema.table_constraints
    WHERE table_schema = 'public'
      AND constraint_type = 'UNIQUE'
),

check_constraints AS (
    SELECT COUNT(*) AS n
    FROM information_schema.table_constraints
    WHERE table_schema = 'public'
      AND constraint_type = 'CHECK'
),

foreign_keys AS (
    SELECT COUNT(*) AS n
    FROM information_schema.table_constraints
    WHERE table_schema = 'public'
      AND constraint_type = 'FOREIGN KEY'
),

wrong_owner AS (
    SELECT COUNT(*) AS n
    FROM pg_tables p
    JOIN tmp_master_tables m
        ON m.table_name = p.tablename
    WHERE p.schemaname = 'public'
      AND p.tableowner <> '$EXPECTED_OWNER'
)

SELECT
    $MASTER_TABLE_COUNT::INTEGER AS master_tables,
    actual_tables.n AS actual_tables,

    $MASTER_COLUMN_COUNT::INTEGER AS master_columns,
    actual_columns.n AS actual_columns,

    foreign_keys.n AS total_foreign_keys,
    primary_keys.n AS total_primary_keys,
    unique_constraints.n AS total_unique_constraints,
    check_constraints.n AS total_check_constraints,

    missing_tables.n AS missing_tables,
    extra_tables.n AS extra_tables,
    wrong_owner.n AS wrong_owner,

    CASE
        WHEN actual_tables.n = $MASTER_TABLE_COUNT::INTEGER
         AND missing_tables.n = 0
         AND extra_tables.n = 0
         AND actual_columns.n = $MASTER_COLUMN_COUNT::INTEGER
         AND primary_keys.n = $MASTER_TABLE_COUNT::INTEGER
         AND wrong_owner.n = 0
        THEN 'PASS'
        ELSE 'REVIEW REQUIRED'
    END AS audit_status

FROM actual_tables,
     missing_tables,
     extra_tables,
     actual_columns,
     primary_keys,
     unique_constraints,
     check_constraints,
     foreign_keys,
     wrong_owner;

-- ============================================================
-- EXPECTED MASTER
-- ============================================================

\echo ''
\echo '============================================================'
\echo '                EXPECTED MASTER'
\echo '============================================================'
\echo 'Tables             : 43'
\echo 'Column definitions : 335'
\echo 'Relationships       : 64'
\echo 'Owner              : admin'
\echo ''
\echo 'DATABASE AUDIT MODE : READ ONLY'
\echo '============================================================'

COMMIT;

SQL

EXIT_CODE=$?

echo ""
echo "============================================================"

if [ "$EXIT_CODE" -eq 0 ]; then
    echo "     LUCKSPINQUEST FULL AUDIT SELESAI"
    echo "============================================================"
    echo ""
    echo "Database tidak dimodifikasi."
else
    echo "     LUCKSPINQUEST AUDIT GAGAL"
    echo "============================================================"
    echo ""
    echo "Periksa ERROR PostgreSQL di atas."
fi

exit "$EXIT_CODE"
