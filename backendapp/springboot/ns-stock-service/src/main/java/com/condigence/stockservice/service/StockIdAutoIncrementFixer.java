package com.condigence.stockservice.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockIdAutoIncrementFixer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StockIdAutoIncrementFixer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment env;

    @Override
    public void run(String... args) throws Exception {
        try {
            String schema = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
            log.info("Current schema/database: {}", schema);

            Integer hasAi = jdbcTemplate.queryForObject(
                    "SELECT CASE WHEN EXTRA LIKE '%auto_increment%' THEN 1 ELSE 0 END AS has_ai " +
                            "FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = 'stock' AND COLUMN_NAME = 'id'",
                    Integer.class, schema);

            if (hasAi != null && hasAi == 1) {
                log.info("stock.id already has AUTO_INCREMENT. No action needed.");
            } else {
                log.warn("stock.id does not have AUTO_INCREMENT. Attempting to modify column.");

                Integer isPk = jdbcTemplate.queryForObject(
                        "SELECT CASE WHEN COLUMN_KEY = 'PRI' THEN 1 ELSE 0 END FROM information_schema.COLUMNS " +
                                "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = 'stock' AND COLUMN_NAME = 'id'",
                        Integer.class, schema);

                String alterSql;
                if (isPk != null && isPk == 1) {
                    alterSql = "ALTER TABLE `stock` MODIFY COLUMN `id` BIGINT NOT NULL AUTO_INCREMENT;";
                } else {
                    alterSql = "ALTER TABLE `stock` MODIFY COLUMN `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY;";
                }

                log.info("Running SQL: {}", alterSql);
                jdbcTemplate.execute(alterSql);

                Integer maxId = jdbcTemplate.queryForObject("SELECT IFNULL(MAX(id),0) FROM stock", Integer.class);
                int nextAi = (maxId != null ? maxId : 0) + 1;
                String setAiSql = "ALTER TABLE `stock` AUTO_INCREMENT = " + nextAi + ";";
                log.info("Setting AUTO_INCREMENT to {}", nextAi);
                jdbcTemplate.execute(setAiSql);
                log.info("Successfully modified stock.id to AUTO_INCREMENT and set next value to {}", nextAi);
            }
        } catch (Exception ex) {
            log.error("An error occurred while attempting to ensure stock.id is AUTO_INCREMENT: {}", ex.getMessage(), ex);
            // Do not rethrow; we don't want schema repair failures to prevent app startup
        }
    }
}

