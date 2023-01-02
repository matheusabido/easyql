package dev.abidux.easyql.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends SQL {

    private final String url, user, password;
    public MySQL(String host, String port, String database, String user, String password) {
        this.url = String.format("jdbc:mysql://%s:%s/%s", host, port, database);
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}