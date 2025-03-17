# DB Migration Tool

## Introduction
This is a database migration tool that supports running via Docker. The tool can connect to different databases based on user-provided configuration files.

## Prerequisites
1. Ensure Docker is installed on your computer.
2. Clone the project repository to your local machine:

   ```sh
   git clone <repo-url>
   cd db-migration-tool
   ```

3. Create a `.env` file and add your database credentials:

   ```properties
   db.username=your_database_username
   db.password=your_database_password
   ```

4. Modify the configuration file `src/main/resources/application.properties` to fit your database environment.

## Build and Run with Docker
### Build Docker Image
1. Build the Docker image:

   ```sh
   docker build -t db-migration-tool .
   ```

### Run Docker Container
2. Run the Docker container using the `.env` file for environment variables:

   ```sh
   docker run --rm -it --env-file .env \
       -v $(pwd)/src/main/resources/application.properties:/app/application.properties \
       db-migration-tool --config=/app/application.properties --migrations=/app/migrations
   ```


## Features

- Execute database migrations from scripts(support .sql only, format eg. V1__create_tables.sql).


## Getting Started

### Using as a CLI Tool

To use the CLI tool, run the following command:

```sh
java -jar db-migration-tool-1.0-SNAPSHOT.jar --migrations=path/to/migrations
```

### Integrating with Other Java Applications

To integrate the DB Migration Tool with your Java application, follow these steps:

1. Add the JAR file to your project's dependencies.

2. Use the `MigrationManager` class to execute migrations.

#### Example

```java
import com.example.dbmigration.MigrationManager;

public class MainApp {
    public static void main(String[] args) {
        String migrationsPath = "src/main/resources/migrations";
        MigrationManager migrationManager = new MigrationManager(migrationsPath);
        migrationManager.executeMigrations();
    }
}
```

## Building the Project

To build the project and generate the JAR file, run:

```sh
mvn clean package
```

This will create a JAR file in the `target` directory.