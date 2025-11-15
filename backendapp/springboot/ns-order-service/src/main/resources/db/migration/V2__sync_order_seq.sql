-- Sync order_seq with current max id across cust_order and order_detail (MySQL 8+)
-- This migration sets the sequence to GREATEST(max(cust_order.id), max(order_detail.order_detail_id)) + 1
-- It is safe if the sequence does not exist (will be a no-op).

-- check if the sequence exists in current schema
SET @exists := (SELECT COUNT(*) FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_NAME = 'order_seq' AND SEQUENCE_SCHEMA = DATABASE());

-- compute next value (max id + 1)
SET @max_order := (SELECT COALESCE(MAX(id), 0) FROM cust_order);
SET @max_detail := (SELECT COALESCE(MAX(order_detail_id), 0) FROM order_detail);
SET @next := GREATEST(@max_order, @max_detail) + 1;

-- build SQL: either ALTER SEQUENCE ... or a harmless SELECT
SET @sql := IF(@exists > 0, CONCAT('ALTER SEQUENCE order_seq RESTART WITH ', @next), 'SELECT 1');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
