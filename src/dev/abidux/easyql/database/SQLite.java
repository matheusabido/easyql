package dev.abidux.easyql.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends SQL {

    public final File databaseFile;
    public SQLite(File databaseFile) {
        this.databaseFile = databaseFile;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
    }
}