-- Flyway migration V2: ensure pic column is LONGBLOB (idempotent for MySQL)
-- This migration will attempt to modify the column type only if necessary.
ALTER TABLE image MODIFY COLUMN pic LONGBLOB;

