package dev.abidux.easyql.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

public abstract class SQL {

    public abstract Connection getConnection() throws SQLException;

    public void connect(Consumer<Connection> connectionConsumer) {
        try (Connection connection = getConnection()) {
            connectionConsumer.accept(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}