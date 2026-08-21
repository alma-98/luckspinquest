#!/bin/bash

set -e

DB_NAME="luckspinquest"

echo ""
echo "============================================================"
echo "        LUCKSPINQUEST - FULL DATABASE AUDIT"
echo "============================================================"
echo "DATABASE : $DB_NAME"
echo "MODE     : READ ONLY"
echo "============================================================"
echo ""

# ============================================================
# CHECK DATABASE CONNECTION
# ============================================================

psql -d "$DB_NAME" -v ON_ERROR_STOP=1 <<'SQL'

\pset pager off
\timing off

-- ============================================================
-- EXPECTED MASTER TABLES #1 - #43
-- ============================================================

DROP TABLE IF EXISTS tmp_expected_tables;

CREATE TEMP TABLE tmp_expected_tables (
    no INTEGER,
    table_name TEXT
);

INSERT INTO tmp_expected_tables (no, table_name) VALUES
(1,  'users'),
(2,  'user_profiles'),
(3,  'roles'),
(4,  'user_roles'),
(5,  'wallets'),
(6,  'wallet_transactions'),
(7,  'payment_accounts'),
(8,  'topup_transactions'),
(9,  'withdrawal_transactions'),
(10, 'withdrawal_reserves'),
(11, 'coin_packages'),
(12, 'spin_wheels'),
(13, 'spin_segments'),
(14, 'spin_rules'),
(15, 'spin_rule_versions'),
(16, 'spin_sessions'),
(17, 'spin_results'),
(18, 'game_bots'),
(19, 'game_bot_configs'),
(20, 'player_game_configs'),
(21, 'rewards'),
(22, 'reward_inventory'),
(23, 'redemptions'),
(24, 'quests'),
(25, 'user_quests'),
(26, 'events'),
(27, 'event_participants'),
(28, 'referral_codes'),
(29, 'referrals'),
(30, 'leaderboards'),
(31, 'leaderboard_entries'),
(32, 'reward_budgets'),
(33, 'game_economy_configs'),
(34, 'account_restrictions'),
(35, 'risk_flags'),
(36, 'notifications'),
(37, 'audit_logs'),
(38, 'app_settings'),
(39, 'admin_actions'),
(40, 'admin_notes'),
(41, 'refresh_tokens'),
(42, 'password_reset_tokens'),
(43, 'email_verification_tokens');


-- ============================================================
-- 1. DATABASE INFO
-- ============================================================

\echo ''
\echo '============================================================'
\echo '1. DATABASE INFORMATION'
\echo '============================================================'

SELECT
    current_database() AS database_name,
    current_user AS current_user,
    current_schema() AS current_schema,
    version() AS postgresql_version;


-- ============================================================
-- 2. MASTER TABLE COUNT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '2. MASTER TABLE COUNT'
\echo '============================================================'

SELECT COUNT(*) AS expected_tables
FROM tmp_expected_tables;


-- ============================================================
-- 3. ACTUAL TABLE COUNT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '3. ACTUAL PUBLIC TABLE COUNT'
\echo '============================================================'

SELECT
    COUNT(*) AS actual_tables
FROM pg_tables
WHERE schemaname = 'public';


-- ============================================================
-- 4. TABLE EXISTENCE #1 - #43
-- ============================================================

\echo ''
\echo '============================================================'
\echo '4. TABLE EXISTENCE #1 - #43'
\echo '============================================================'

SELECT
    e.no,
    e.table_name,
    CASE
        WHEN p.tablename IS NULL THEN 'MISSING'
        ELSE 'EXISTS'
    END AS status,
    COALESCE(p.tableowner, '-') AS owner
FROM tmp_expected_tables e
LEFT JOIN pg_tables p
    ON p.schemaname = 'public'
   AND p.tablename = e.table_name
ORDER BY e.no;


-- ============================================================
-- 5. MISSING TABLES
-- ============================================================

\echo ''
\echo '============================================================'
\echo '5. MISSING TABLES'
\echo '============================================================'

SELECT
    e.no,
    e.table_name
FROM tmp_expected_tables e
LEFT JOIN pg_tables p
    ON p.schemaname = 'public'
   AND p.tablename = e.table_name
WHERE p.tablename IS NULL
ORDER BY e.no;


-- ============================================================
-- 6. EXTRA TABLES
-- ============================================================

\echo ''
\echo '============================================================'
\echo '6. EXTRA PUBLIC TABLES'
\echo '============================================================'

SELECT
    p.tablename AS table_name,
    p.tableowner AS owner
FROM pg_tables p
LEFT JOIN tmp_expected_tables e
    ON e.table_name = p.tablename
WHERE p.schemaname = 'public'
  AND e.table_name IS NULL
ORDER BY p.tablename;


-- ============================================================
-- 7. COLUMN COUNT PER TABLE
-- ============================================================

\echo ''
\echo '============================================================'
\echo '7. COLUMN COUNT PER TABLE'
\echo '============================================================'

SELECT
    e.no,
    e.table_name,
    COALESCE(COUNT(c.column_name), 0) AS column_count
FROM tmp_expected_tables e
LEFT JOIN information_schema.columns c
    ON c.table_schema = 'public'
   AND c.table_name = e.table_name
GROUP BY e.no, e.table_name
ORDER BY e.no;


-- ============================================================
-- 8. TOTAL COLUMNS
-- ============================================================

\echo ''
\echo '============================================================'
\echo '8. TOTAL COLUMN COUNT'
\echo '============================================================'

SELECT
    COUNT(*) AS total_columns
FROM information_schema.columns
WHERE table_schema = 'public';


-- ============================================================
-- 9. FULL COLUMN STRUCTURE
-- ============================================================

\echo ''
\echo '============================================================'
\echo '9. FULL COLUMN STRUCTURE'
\echo '============================================================'

SELECT
    e.no,
    c.table_name,
    c.ordinal_position AS column_no,
    c.column_name,
    CASE
        WHEN c.data_type = 'character varying'
            AND c.character_maximum_length IS NOT NULL
        THEN
            'VARCHAR(' || c.character_maximum_length || ')'

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
FROM tmp_expected_tables e
JOIN information_schema.columns c
    ON c.table_schema = 'public'
   AND c.table_name = e.table_name
ORDER BY
    e.no,
    c.ordinal_position;


-- ============================================================
-- 10. TABLE OWNER
-- ============================================================

\echo ''
\echo '============================================================'
\echo '10. TABLE OWNER'
\echo '============================================================'

SELECT
    e.no,
    e.table_name,
    p.tableowner AS owner,
    CASE
        WHEN p.tableowner = 'admin'
        THEN 'PASS'
        ELSE 'REVIEW'
    END AS status
FROM tmp_expected_tables e
JOIN pg_tables p
    ON p.schemaname = 'public'
   AND p.tablename = e.table_name
ORDER BY e.no;


-- ============================================================
-- 11. PRIMARY KEYS
-- ============================================================

\echo ''
\echo '============================================================'
\echo '11. PRIMARY KEYS'
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
-- 12. PRIMARY KEY COUNT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '12. PRIMARY KEY COUNT'
\echo '============================================================'

SELECT
    COUNT(*) AS total_primary_keys
FROM information_schema.table_constraints
WHERE table_schema = 'public'
  AND constraint_type = 'PRIMARY KEY';


-- ============================================================
-- 13. UNIQUE CONSTRAINTS
-- ============================================================

\echo ''
\echo '============================================================'
\echo '13. UNIQUE CONSTRAINTS'
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
-- 14. UNIQUE COUNT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '14. UNIQUE CONSTRAINT COUNT'
\echo '============================================================'

SELECT
    COUNT(*) AS total_unique_constraints
FROM information_schema.table_constraints
WHERE table_schema = 'public'
  AND constraint_type = 'UNIQUE';


-- ============================================================
-- 15. CHECK CONSTRAINTS
-- ============================================================

\echo ''
\echo '============================================================'
\echo '15. CHECK CONSTRAINTS'
\echo '============================================================'

SELECT
    tc.table_name,
    tc.constraint_name,
    pg_get_constraintdef(pc.oid) AS definition
FROM information_schema.table_constraints tc
JOIN pg_constraint pc
    ON pc.conname = tc.constraint_name
WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'CHECK'
ORDER BY
    tc.table_name,
    tc.constraint_name;


-- ============================================================
-- 16. CHECK COUNT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '16. CHECK CONSTRAINT COUNT'
\echo '============================================================'

SELECT
    COUNT(*) AS total_check_constraints
FROM information_schema.table_constraints
WHERE table_schema = 'public'
  AND constraint_type = 'CHECK';


-- ============================================================
-- 17. ALL FOREIGN KEYS
-- ============================================================

\echo ''
\echo '============================================================'
\echo '17. ALL FOREIGN KEYS'
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
-- 18. FOREIGN KEY COUNT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '18. FOREIGN KEY COUNT'
\echo '============================================================'

SELECT
    COUNT(*) AS total_foreign_keys
FROM information_schema.table_constraints
WHERE table_schema = 'public'
  AND constraint_type = 'FOREIGN KEY';


-- ============================================================
-- 19. REFERENCED TABLES / RELATIONSHIP MAP
-- ============================================================

\echo ''
\echo '============================================================'
\echo '19. RELATIONSHIP MAP'
\echo '============================================================'

SELECT
    tc.table_name AS child_table,
    kcu.column_name AS child_column,
    ccu.table_name AS parent_table,
    ccu.column_name AS parent_column
FROM information_schema.table_constraints tc

JOIN information_schema.key_column_usage kcu
    ON tc.constraint_name = kcu.constraint_name
   AND tc.table_schema = kcu.table_schema

JOIN information_schema.constraint_column_usage ccu
    ON ccu.constraint_name = tc.constraint_name
   AND ccu.constraint_schema = tc.table_schema

WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'FOREIGN KEY'

ORDER BY
    tc.table_name,
    kcu.column_name;


-- ============================================================
-- 20. INVALID / BROKEN FK CHECK
-- ============================================================

\echo ''
\echo '============================================================'
\echo '20. FK INTEGRITY CHECK'
\echo '============================================================'

SELECT
    COUNT(*) AS foreign_keys_without_valid_reference
FROM information_schema.table_constraints tc

JOIN information_schema.key_column_usage kcu
    ON tc.constraint_name = kcu.constraint_name
   AND tc.table_schema = kcu.table_schema

LEFT JOIN information_schema.constraint_column_usage ccu
    ON ccu.constraint_name = tc.constraint_name
   AND ccu.constraint_schema = tc.table_schema

WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'FOREIGN KEY'
  AND ccu.table_name IS NULL;


-- ============================================================
-- 21. SEQUENCES
-- ============================================================

\echo ''
\echo '============================================================'
\echo '21. SERIAL / ID SEQUENCES'
\echo '============================================================'

SELECT
    sequence_schema,
    sequence_name
FROM information_schema.sequences
WHERE sequence_schema = 'public'
ORDER BY sequence_name;


-- ============================================================
-- 22. INDEX SUMMARY
-- ============================================================

\echo ''
\echo '============================================================'
\echo '22. INDEX SUMMARY'
\echo '============================================================'

SELECT
    schemaname,
    tablename,
    indexname,
    indexdef
FROM pg_indexes
WHERE schemaname = 'public'
ORDER BY
    tablename,
    indexname;


-- ============================================================
-- 23. TABLE SIZE
-- ============================================================

\echo ''
\echo '============================================================'
\echo '23. TABLE SIZE'
\echo '============================================================'

SELECT
    schemaname,
    tablename,
    pg_size_pretty(
        pg_total_relation_size(
            schemaname || '.' || tablename
        )
    ) AS total_size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY
    pg_total_relation_size(
        schemaname || '.' || tablename
    ) DESC;


-- ============================================================
-- 24. FINAL SUMMARY
-- ============================================================

\echo ''
\echo ''
\echo '============================================================'
\echo '                 FINAL SUMMARY'
\echo '============================================================'

WITH
expected AS (
    SELECT COUNT(*) AS total
    FROM tmp_expected_tables
),
actual AS (
    SELECT COUNT(*) AS total
    FROM pg_tables
    WHERE schemaname = 'public'
),
missing AS (
    SELECT COUNT(*) AS total
    FROM tmp_expected_tables e
    LEFT JOIN pg_tables p
        ON p.schemaname = 'public'
       AND p.tablename = e.table_name
    WHERE p.tablename IS NULL
),
extra AS (
    SELECT COUNT(*) AS total
    FROM pg_tables p
    LEFT JOIN tmp_expected_tables e
        ON e.table_name = p.tablename
    WHERE p.schemaname = 'public'
      AND e.table_name IS NULL
),
columns_count AS (
    SELECT COUNT(*) AS total
    FROM information_schema.columns
    WHERE table_schema = 'public'
),
pks AS (
    SELECT COUNT(*) AS total
    FROM information_schema.table_constraints
    WHERE table_schema = 'public'
      AND constraint_type = 'PRIMARY KEY'
),
uniques AS (
    SELECT COUNT(*) AS total
    FROM information_schema.table_constraints
    WHERE table_schema = 'public'
      AND constraint_type = 'UNIQUE'
),
checks AS (
    SELECT COUNT(*) AS total
    FROM information_schema.table_constraints
    WHERE table_schema = 'public'
      AND constraint_type = 'CHECK'
),
fks AS (
    SELECT COUNT(*) AS total
    FROM information_schema.table_constraints
    WHERE table_schema = 'public'
      AND constraint_type = 'FOREIGN KEY'
)

SELECT
    expected.total AS expected_tables,
    actual.total AS actual_tables,
    columns_count.total AS total_columns,
    fks.total AS total_foreign_keys,
    pks.total AS total_primary_keys,
    uniques.total AS total_unique_constraints,
    checks.total AS total_check_constraints,
    missing.total AS missing_tables,
    extra.total AS extra_tables,

    CASE
        WHEN expected.total = 43
         AND actual.total = 43
         AND missing.total = 0
         AND extra.total = 0
         AND pks.total = 43
         AND fks.total = 57
        THEN 'PASS'
        ELSE 'REVIEW REQUIRED'
    END AS audit_status

FROM expected,
     actual,
     columns_count,
     fks,
     pks,
     uniques,
     checks,
     missing,
     extra;


-- ============================================================
-- 25. EXPECTED RESULT
-- ============================================================

\echo ''
\echo '============================================================'
\echo '                 EXPECTED MASTER'
\echo '============================================================'
\echo 'Tables                  : 43'
\echo 'Missing Tables           : 0'
\echo 'Extra Tables             : 0'
\echo 'Primary Keys             : 43'
\echo 'Foreign Keys             : 57'
\echo 'Owner                    : admin'
\echo ''
\echo 'Audit ini READ ONLY.'
\echo 'Tidak ada CREATE / ALTER / DROP / INSERT / UPDATE / DELETE.'
\echo '============================================================'

SQL

echo ""
echo "============================================================"
echo "        LUCKSPINQUEST AUDIT SELESAI"
echo "============================================================"
