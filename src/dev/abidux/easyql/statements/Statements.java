package dev.abidux.easyql.statements;

import dev.abidux.easyql.util.Column;
import dev.abidux.easyql.util.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Statements {

    private final Connection connection;
    public Statements(Connection connection) {
        this.connection = connection;
    }

    public boolean create(Table table) {
        String query = StatementsUtil.createCreateQuery(table);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T extends Table.TableObject> boolean insert(Table<T> table, T object) {
        List<Column> columns = StatementsUtil.getTargetColumns(table).collect(Collectors.toList());
        String query = StatementsUtil.createInsertQuery(table);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                Object value = object.getColumnValue(column);
                Object encodedValue = column.serializer.encoder.encode(value);
                statement.setObject(i+1, encodedValue);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T extends Table.TableObject> boolean update(Table<T> table, T object) {
        List<Column> columns = StatementsUtil.getTargetColumns(table).collect(Collectors.toList());
        String query = StatementsUtil.createUpdateQuery(table);
        Column id = StatementsUtil.getIdColumn(table).get();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int i;
            for (i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                Object value = object.getColumnValue(column);
                Object encodedValue = column.serializer.encoder.encode(value);
                statement.setObject(i+1, encodedValue);
            }
            statement.setObject(i+1, id.serializer.encoder.encode(object.getColumnValue(id)));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T extends Table.TableObject> boolean exists(Table<T> table, T object) {
        String query = StatementsUtil.createExistsQuery(table);
        Column id = StatementsUtil.getIdColumn(table).get();

        boolean exists = false;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id.serializer.encoder.encode(object.getColumnValue(id)));
            try (ResultSet rs = statement.executeQuery()) {
                exists = rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public <T extends Table.TableObject> boolean exists(Table<T> table, QueryFilter filter) {
        String query = new StringBuffer("select * from ").append(table.getName()).append(" ").append(filter.build()).toString();

        boolean exists = false;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            StatementsUtil.setupFilter(statement, filter);
            try (ResultSet rs = statement.executeQuery()) {
                exists = rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public <T extends Table.TableObject> boolean save(Table<T> table, T object) {
        return exists(table, object) ? update(table, object) : insert(table, object);
    }

    public <T extends Table.TableObject> ArrayList<T> select(Table<T> table) {
        return select(table, null);
    }

    public <T extends Table.TableObject> ArrayList<T> select(Table<T> table, QueryFilter filter) {
        ArrayList<T> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer("select * from ").append(table.getName());
        if (filter != null) buffer.append(" ").append(filter.build());
        String query = buffer.toString();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (filter != null) StatementsUtil.setupFilter(statement, filter);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    list.add(table.reader.read(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public <T extends Table.TableObject> boolean delete(Table<T> table, T object) {
        String query = StatementsUtil.createDeleteQuery(table);
        Column id = StatementsUtil.getIdColumn(table).get();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id.serializer.encoder.encode(object.getColumnValue(id)));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T extends Table.TableObject> boolean delete(Table<T> table, QueryFilter filter) {
        String query = new StringBuffer("delete from ").append(table.getName()).append(" ").append(filter.build()).toString();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            StatementsUtil.setupFilter(statement, filter);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}