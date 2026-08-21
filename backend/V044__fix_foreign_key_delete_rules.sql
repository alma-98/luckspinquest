BEGIN;

\echo ''
\echo '============================================================'
\echo ' LUCKYSPINQUEST - V044 FK DELETE RULE FIX'
\echo '============================================================'

-- ============================================================
-- 1. WALLET TRANSACTIONS
-- Financial ledger/history must NEVER be deleted automatically.
-- ============================================================

ALTER TABLE wallet_transactions
    DROP CONSTRAINT IF EXISTS fk_wallet_transactions_user;

ALTER TABLE wallet_transactions
    ADD CONSTRAINT fk_wallet_transactions_user
    FOREIGN KEY (user_id)
    REFERENCES users(user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION;

ALTER TABLE wallet_transactions
    DROP CONSTRAINT IF EXISTS fk_wallet_transactions_wallet;

ALTER TABLE wallet_transactions
    ADD CONSTRAINT fk_wallet_transactions_wallet
    FOREIGN KEY (wallet_id)
    REFERENCES wallets(wallet_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION;


-- ============================================================
-- 2. TOPUP TRANSACTIONS
-- Topup is financial history.
-- Payment account may disappear, but transaction history remains.
-- ============================================================

ALTER TABLE topup_transactions
    DROP CONSTRAINT IF EXISTS fk_topup_transactions_user;

ALTER TABLE topup_transactions
    ADD CONSTRAINT fk_topup_transactions_user
    FOREIGN KEY (user_id)
    REFERENCES users(user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION;


-- ============================================================
-- 3. WITHDRAWAL TRANSACTIONS
-- Financial withdrawal history must be preserved.
-- ============================================================

ALTER TABLE withdrawal_transactions
    DROP CONSTRAINT IF EXISTS fk_withdrawal_transactions_user;

ALTER TABLE withdrawal_transactions
    ADD CONSTRAINT fk_withdrawal_transactions_user
    FOREIGN KEY (user_id)
    REFERENCES users(user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION;

ALTER TABLE withdrawal_transactions
    DROP CONSTRAINT IF EXISTS fk_withdrawal_transactions_wallet;

ALTER TABLE withdrawal_transactions
    ADD CONSTRAINT fk_withdrawal_transactions_wallet
    FOREIGN KEY (wallet_id)
    REFERENCES wallets(wallet_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION;


-- ============================================================
-- 4. WITHDRAWAL RESERVES
-- Reserve is operational data.
-- Withdrawal owns the reserve lifecycle.
-- ============================================================

ALTER TABLE withdrawal_reserves
    DROP CONSTRAINT IF EXISTS fk_withdrawal_reserves_user;

ALTER TABLE withdrawal_reserves
    ADD CONSTRAINT fk_withdrawal_reserves_user
    FOREIGN KEY (user_id)
    REFERENCES users(user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION;

ALTER TABLE withdrawal_reserves
    DROP CONSTRAINT IF EXISTS fk_withdrawal_reserves_wallet;

ALTER TABLE withdrawal_reserves
    ADD CONSTRAINT fk_withdrawal_reserves_wallet
    FOREIGN KEY (wallet_id)
    REFERENCES wallets(wallet_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION;

-- withdrawal_id CASCADE is intentionally preserved.
-- A reserve belongs to a withdrawal and has no independent lifecycle.


-- ============================================================
-- 5. SPIN RULE VERSIONS
-- Version history must not disappear with the master wheel.
-- ============================================================

ALTER TABLE spin_rule_versions
    DROP CONSTRAINT IF EXISTS fk_spin_rule_versions_wheel;

ALTER TABLE spin_rule_versions
    ADD CONSTRAINT fk_spin_rule_versions_wheel
    FOREIGN KEY (wheel_id)
    REFERENCES spin_wheels(wheel_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION;


COMMIT;


\echo ''
\echo '============================================================'
\echo ' V044 COMPLETED'
\echo '============================================================'

\echo ''
\echo '===== UPDATED FK DELETE RULES ====='

SELECT
    tc.table_name AS source_table,
    kcu.column_name AS source_column,
    ccu.table_name AS referenced_table,
    ccu.column_name AS referenced_column,
    rc.delete_rule,
    rc.update_rule
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu
    ON tc.constraint_name = kcu.constraint_name
    AND tc.table_schema = kcu.table_schema
JOIN information_schema.constraint_column_usage ccu
    ON ccu.constraint_name = tc.constraint_name
    AND ccu.table_schema = tc.table_schema
JOIN information_schema.referential_constraints rc
    ON rc.constraint_name = tc.constraint_name
    AND rc.constraint_schema = tc.table_schema
WHERE tc.table_schema = 'public'
  AND (
        tc.table_name IN (
            'wallet_transactions',
            'topup_transactions',
            'withdrawal_transactions',
            'withdrawal_reserves',
            'spin_rule_versions'
        )
      )
ORDER BY tc.table_name, kcu.column_name;


\echo ''
\echo '===== REMAINING CASCADE FK ====='

SELECT
    tc.table_name AS source_table,
    kcu.column_name AS source_column,
    ccu.table_name AS referenced_table,
    ccu.column_name AS referenced_column,
    rc.delete_rule
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu
    ON tc.constraint_name = kcu.constraint_name
    AND tc.table_schema = kcu.table_schema
JOIN information_schema.constraint_column_usage ccu
    ON ccu.constraint_name = tc.constraint_name
    AND ccu.table_schema = tc.table_schema
JOIN information_schema.referential_constraints rc
    ON rc.constraint_name = tc.constraint_name
    AND rc.constraint_schema = tc.table_schema
WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'FOREIGN KEY'
  AND rc.delete_rule = 'CASCADE'
ORDER BY tc.table_name, kcu.column_name;


\echo ''
\echo '============================================================'
\echo ' EXPECTED IMPORTANT HISTORY RULES'
\echo ' wallet_transactions  : RESTRICT'
\echo ' topup_transactions   : RESTRICT'
\echo ' withdrawal_transactions: RESTRICT'
\echo ' withdrawal_reserves  : RESTRICT / CASCADE(withdrawal)'
\echo ' spin_rule_versions   : RESTRICT'
\echo '============================================================'
