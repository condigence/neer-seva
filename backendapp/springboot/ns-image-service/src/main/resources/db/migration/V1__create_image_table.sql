-- Flyway migration V1: create image table (generic SQL compatible with H2 and MySQL)
CREATE TABLE IF NOT EXISTS image (
  id BIGINT NOT NULL AUTO_INCREMENT,
  unique_name VARCHAR(255),
  path VARCHAR(255),
  size BIGINT,
  module_name VARCHAR(255),
  name VARCHAR(255),
  pic BLOB,
  type VARCHAR(255),
  PRIMARY KEY (id)
);
