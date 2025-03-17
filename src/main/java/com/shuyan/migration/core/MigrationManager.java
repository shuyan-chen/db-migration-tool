package com.shuyan.migration.core;

import com.shuyan.migration.exception.MigrationException;
import com.shuyan.migration.model.MigrationHistory;
import com.shuyan.migration.repository.MigrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MigrationManager {

    private final String migrationPath;

    public MigrationManager(String migrationPath) {
        this.migrationPath = migrationPath;
    }

    public void migrate(Connection connection) throws MigrationException {

        try {
            MigrationRepository repository = new MigrationRepository(connection);
            repository.initMigrationTable();


            ScriptLoader scriptLoader = new ScriptLoader(migrationPath);
            List<MigrationHistory> availableMigrations;

            try {
                availableMigrations = scriptLoader.loadMigrationScripts();

            } catch (IOException e) {

                throw new MigrationException("Failed to load migration scripts: " + e.getMessage());
            }

            if (availableMigrations.isEmpty()) {

                return;
            }

            List<MigrationHistory> appliedMigrations;
            try {
                appliedMigrations = repository.getAllMigrations();

            } catch (SQLException e) {

                throw new MigrationException("Failed to retrieve applied migrations: " + e.getMessage());
            }

            Map<String, MigrationHistory> appliedMigrationsMap = appliedMigrations.stream()
                    .collect(Collectors.toMap(MigrationHistory::getVersion, Function.identity()));

            validateAppliedMigrations(availableMigrations, appliedMigrationsMap);

            List<MigrationHistory> pendingMigrations = new ArrayList<>();
            for (MigrationHistory migration : availableMigrations) {
                if (!appliedMigrationsMap.containsKey(migration.getVersion())) {
                    pendingMigrations.add(migration);
                }
            }

            if (pendingMigrations.isEmpty()) {

                return;
            }

            MigrationExecutor executor = new MigrationExecutor(connection);

            for (MigrationHistory migration : pendingMigrations) {
                try {

                    executor.execute(migration);
                    repository.saveMigration(migration);

                } catch (SQLException e) {

                    throw new MigrationException("Failed to save migration record: " + e.getMessage());
                }
            }

        } catch (SQLException e) {

            throw new MigrationException("Database connection error: " + e.getMessage(), e);
        }
    }

    private void validateAppliedMigrations(List<MigrationHistory> availableMigrations, Map<String, MigrationHistory> appliedMigrationsMap)
            throws MigrationException {

        List<String> modifiedMigrations = new ArrayList<>();

        for (MigrationHistory available : availableMigrations) {
            MigrationHistory applied = appliedMigrationsMap.get(available.getVersion());
            if (applied != null && !applied.getChecksum().equals(available.getChecksum())) {
                modifiedMigrations.add(
                        "Version: " + available.getVersion() +
                                ", Expected: " + applied.getChecksum() +
                                ", Actual: " + available.getChecksum());
            }
        }

        if (!modifiedMigrations.isEmpty()) {

            throw new MigrationException("Modified migrations detected:\n" + String.join("\n", modifiedMigrations));
        }
    }
}