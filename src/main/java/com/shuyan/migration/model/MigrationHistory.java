package com.shuyan.migration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MigrationHistory {
    private String version;
    private String description;
    private String fileName;
    private String checksum;
    private LocalDateTime executedAt;
    private String script;
    private boolean success;

    public MigrationHistory(String version, String description, String filename, String checksum) {
        this.version = version;
        this.description = description;
        this.fileName = filename;
        this.checksum = checksum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MigrationHistory migration = (MigrationHistory) o;
        return Objects.equals(version, migration.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    public String toString() {
        return "Migration{" +
                "version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", filename='" + fileName + '\'' +
                '}';
    }
}
