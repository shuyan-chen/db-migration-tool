package com.shuyan.migration.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static HikariDataSource dataSource;

    public static DataSource getDataSource(String configFilePath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(System.getenv("db.username"));
        config.setPassword(System.getenv("db.password"));
        config.setDriverClassName(properties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.pool.maxSize", "10")));
        config.setConnectionTimeout(Long.parseLong(properties.getProperty("db.pool.connectionTimeout", "30000")));
        config.setIdleTimeout(Long.parseLong(properties.getProperty("db.pool.idleTimeout", "600000")));

        return new HikariDataSource(config);
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error getting database connection", e);
        }
    }


}
