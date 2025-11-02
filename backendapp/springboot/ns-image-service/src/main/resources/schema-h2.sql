-- H2 initialization script: create image table for local/dev
CREATE TABLE IF NOT EXISTS image (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  unique_name VARCHAR(255),
  path VARCHAR(512),
  size BIGINT,
  module_name VARCHAR(255),
  name VARCHAR(255),
  pic BLOB,
  type VARCHAR(255)
);

