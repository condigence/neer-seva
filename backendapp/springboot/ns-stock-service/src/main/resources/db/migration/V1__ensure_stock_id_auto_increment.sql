-- Flyway migration: make stock.id AUTO_INCREMENT if not already
-- This script is safe to run multiple times: it checks information_schema and only alters when necessary.
SET @schema_name = DATABASE();
SELECT CONCAT('Schema: ', @schema_name) AS info;

SELECT COLUMN_NAME, COLUMN_TYPE, EXTRA, COLUMN_KEY
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = @schema_name
  AND TABLE_NAME = 'stock'
  AND COLUMN_NAME = 'id'
\G

-- If the column exists and doesn't have 'auto_increment' in EXTRA, alter it
SET @has_ai = (
  SELECT CASE WHEN EXTRA LIKE '%auto_increment%' THEN 1 ELSE 0 END
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @schema_name
    AND TABLE_NAME = 'stock'
    AND COLUMN_NAME = 'id'
);

-- If column doesn't exist, we do nothing here (assume other migrations manage it)

SET @sql = NULL;
IF @has_ai = 0 THEN
  -- Determine if id is primary key; if not, we'll make it primary key as part of the modify
  SET @is_pk = (
    SELECT CASE WHEN COLUMN_KEY = 'PRI' THEN 1 ELSE 0 END
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name
      AND TABLE_NAME = 'stock'
      AND COLUMN_NAME = 'id'
  );

  IF @is_pk = 1 THEN
    SET @sql = 'ALTER TABLE `stock` MODIFY COLUMN `id` BIGINT NOT NULL AUTO_INCREMENT;';
  ELSE
    SET @sql = 'ALTER TABLE `stock` MODIFY COLUMN `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY;';
  END IF;

  PREPARE stmt FROM @sql;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;
END IF;

-- Ensure AUTO_INCREMENT counter is greater than max(id) to avoid duplicates
SET @max_id = (SELECT IFNULL(MAX(id), 0) FROM stock);
SET @next_ai = @max_id + 1;
SET @set_ai_sql = CONCAT('ALTER TABLE `stock` AUTO_INCREMENT = ', @next_ai, ';');
PREPARE stmt2 FROM @set_ai_sql;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;

SELECT 'migration-complete' as status;

