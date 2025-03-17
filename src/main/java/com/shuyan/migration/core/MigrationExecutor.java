package com.shuyan.migration.core;

import com.shuyan.migration.config.DatabaseConfig;
import com.shuyan.migration.exception.MigrationException;
import com.shuyan.migration.model.MigrationHistory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class MigrationExecutor {

    private final Connection connection;

    public MigrationExecutor(Connection connection) {
        this.connection = DatabaseConfig.getConnection();
    }

    public void execute(MigrationHistory migration) throws MigrationException {
        boolean originalAutoCommit;
        try (Connection connection = DatabaseConfig.getConnection()) {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (Statement stmt = connection.createStatement()) {
                String[] statements = migration.getScript().split(";");
                for (String statement : statements) {
                    String trimmedStatement = statement.trim();
                    if (!trimmedStatement.isEmpty()) {
                        try {
                            stmt.execute(trimmedStatement);
                        } catch (SQLException e) {
                            connection.rollback();
                            migration.setSuccess(false);
                            throw new MigrationException("Failed to execute SQL: " + trimmedStatement + ", Error: " + e.getMessage(), e);
                        }
                    }
                }

                migration.setExecutedAt(LocalDateTime.now());
                migration.setSuccess(true);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                migration.setSuccess(false);
                throw new MigrationException("Transaction failed: " + e.getMessage(), e);
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (SQLException e) {
            throw new MigrationException("Database error: " + e.getMessage(), e);
        }
    }
}

