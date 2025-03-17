package com.shuyan.migration.repository;

import com.shuyan.migration.config.DatabaseConfig;
import com.shuyan.migration.model.MigrationHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MigrationRepository {
    private Connection connection;
    private static final String MIGRATION_TABLE = "migration_history";

    public MigrationRepository(Connection connection) {
        this.connection = DatabaseConfig.getConnection();
    }

    public void initMigrationTable() throws SQLException {

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, MIGRATION_TABLE, null);

        if (!tables.next()) {

            String createTableSql =
                    "CREATE TABLE " + MIGRATION_TABLE + " (" +
                            "    id INT AUTO_INCREMENT PRIMARY KEY," +
                            "    version VARCHAR(50) NOT NULL," +
                            "    description VARCHAR(200)," +
                            "    filename VARCHAR(200) NOT NULL," +
                            "    checksum VARCHAR(100) NOT NULL," +
                            "    script TEXT," +
                            "    executed_at TIMESTAMP NOT NULL," +
                            "    success BOOLEAN NOT NULL," +
                            "    UNIQUE (version)" +
                            ")";

            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableSql);
            }
        }
    }

    public List<MigrationHistory> getAllMigrations() throws SQLException {
        List<MigrationHistory> migrations = new ArrayList<>();
        String sql = "SELECT * FROM " + MIGRATION_TABLE + " ORDER BY version";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MigrationHistory migration = new MigrationHistory(
                        rs.getString("version"),
                        rs.getString("description"),
                        rs.getString("filename"),
                        rs.getString("checksum")
                );
                migration.setExecutedAt(rs.getTimestamp("executed_at").toLocalDateTime());
                migration.setScript(rs.getString("script"));
                migration.setSuccess(rs.getBoolean("success"));

                migrations.add(migration);
            }
        }

        return migrations;
    }

    public void saveMigration(MigrationHistory migration) throws SQLException {
        String sql = "INSERT INTO " + MIGRATION_TABLE +
                " (version, description, filename, checksum, script, executed_at,success) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, migration.getVersion());
            pstmt.setString(2, migration.getDescription());
            pstmt.setString(3, migration.getFileName());
            pstmt.setString(4, migration.getChecksum());
            pstmt.setString(5, migration.getScript());
            pstmt.setTimestamp(6, Timestamp.valueOf(migration.getExecutedAt()));
            pstmt.setBoolean(7, migration.isSuccess());
            pstmt.executeUpdate();
        }
    }

    public MigrationHistory getMigrationByVersion(String version) throws SQLException {
        String sql = "SELECT * FROM " + MIGRATION_TABLE + " WHERE version = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, version);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    MigrationHistory migration = new MigrationHistory(
                            rs.getString("version"),
                            rs.getString("description"),
                            rs.getString("filename"),
                            rs.getString("checksum")
                    );
                    migration.setExecutedAt(rs.getTimestamp("executed_at").toLocalDateTime());
                    migration.setScript(rs.getString("script"));
                    migration.setSuccess(rs.getBoolean("success"));

                    return migration;
                }
            }
        }

        return null;
    }
}
