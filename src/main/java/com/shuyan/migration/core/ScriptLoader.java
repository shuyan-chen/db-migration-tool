package com.shuyan.migration.core;

import com.shuyan.migration.model.MigrationHistory;
import com.shuyan.migration.util.ChecksumUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScriptLoader {
    private static final Pattern FILENAME_PATTERN = Pattern.compile("V(\\d+(?:\\.\\d+)?)__(.*)\\.sql");
    private final String migrationPath;

    public ScriptLoader(String migrationPath) {
        this.migrationPath = migrationPath;
    }

    public List<MigrationHistory> loadMigrationScripts() throws IOException {
        List<MigrationHistory> migrations = new ArrayList<>();
        Path migrationsDir = Paths.get(migrationPath);

        if (!Files.exists(migrationsDir) || !Files.isDirectory(migrationsDir)) {
            return Collections.emptyList();
        }

        try (Stream<Path> paths = Files.walk(migrationsDir)) {
            List<Path> sqlFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".sql"))
                    .collect(Collectors.toList());

            for (Path sqlFile : sqlFiles) {
                String filename = sqlFile.getFileName().toString();
                Matcher matcher = FILENAME_PATTERN.matcher(filename);

                if (matcher.matches()) {
                    String version = matcher.group(1);
                    String description = matcher.group(2).replace('_', ' ');
                    String script = Files.readString(sqlFile, StandardCharsets.UTF_8);
                    String checksum = ChecksumUtil.getMD5(script);

                    MigrationHistory migration = new MigrationHistory(version, description, filename, checksum);
                    migration.setScript(script);

                    migrations.add(migration);
                }
            }
        }
        migrations.sort((m1, m2) -> compareVersions(m1.getVersion(), m2.getVersion()));
        return migrations;
    }

    public MigrationHistory loadMigrationFromResources(String filename) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("migrations/" + filename)) {
            if (is == null) {
                return null;
            }

            String script = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            Matcher matcher = FILENAME_PATTERN.matcher(filename);
            if (matcher.matches()) {
                String version = matcher.group(1);
                String description = matcher.group(2).replace('_', ' ');
                String checksum = ChecksumUtil.getMD5(script);

                MigrationHistory migration = new MigrationHistory(version, description, filename, checksum);
                migration.setScript(script);

                return migration;
            }
        }
        return null;
    }


    private int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");

        int length = Math.max(parts1.length, parts2.length);

        for (int i = 0; i < length; i++) {
            int p1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int p2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;

            if (p1 != p2) {
                return Integer.compare(p1, p2);
            }
        }
        return 0;
    }

}
