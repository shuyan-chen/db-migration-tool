package com.shuyan.migration;

import com.shuyan.migration.config.DatabaseConfig;
import com.shuyan.migration.core.MigrationManager;
import com.shuyan.migration.exception.MigrationException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

@Command(name = "db-migration-tool", mixinStandardHelpOptions = true, version = "1.0",
        description = "A CLI tool for managing database migrations.")
public class Main implements Callable<Integer> {



        @Option(names = {"-m", "--migrations"},
                description = "Path to the directory containing migration scripts.",
                defaultValue = "src/main/resources/migrations")
        private String migrationPath;

        @Option(names = {"-c", "--config"},
            description = "Path to the configuration file.",
            required = true)
        private String configFilePath;

    @Override
    public Integer call() {
        try {
            DataSource dataSource = DatabaseConfig.getDataSource(configFilePath);
            try (Connection connection = dataSource.getConnection()) {
                MigrationManager manager = new MigrationManager(migrationPath);
                manager.migrate(connection);
                return 0;
            }
        } catch (IOException e) {
            return 1;
        } catch (MigrationException e) {
            return 2;
        } catch (SQLException e) {
            return 3;
        } catch (Exception e) {
            return 4;
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}