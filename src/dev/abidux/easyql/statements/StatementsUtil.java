package dev.abidux.easyql.statements;

import dev.abidux.easyql.exception.IdNotFoundException;
import dev.abidux.easyql.util.Column;
import dev.abidux.easyql.util.Table;
import dev.abidux.easyql.util.serializer.Serializer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatementsUtil {

    public static String createCreateQuery(Table table) {
        Column[] columns = table.getColumns();
        StringBuffer buffer = new StringBuffer("create table if not exists ").append(table.getName()).append(" (");
        for (Column column : columns) {
            buffer.append(column.name).append(" ").append(column.serializer.sqlType);
            if (column.isAutoIncrement()) buffer.append(" not null auto_increment");
            if (column.isId()) buffer.append(" primary key");
            buffer.append(",");
        }
        return buffer.deleteCharAt(buffer.length() - 1).append(")").toString();
    }

    public static String createInsertQuery(Table table) {
        List<Column> columns = StatementsUtil.getTargetColumns(table).collect(Collectors.toList());

        StringBuffer buffer = new StringBuffer("insert into ").append(table.getName()).append(" (");
        buffer.append(columns.stream().map(c -> c.name).collect(Collectors.joining(","))); // add columns
        buffer.append(") values (");
        buffer.append(columns.stream().map(c ->"?").collect(Collectors.joining(","))); // add all the "?" needed
        buffer.append(")");
        return buffer.toString();
    }

    public static String createUpdateQuery(Table table) {
        StringBuffer buffer = new StringBuffer("update ").append(table.getName()).append(" set ");
        getTargetColumns(table).forEach(column -> buffer.append(column.name).append("=").append("?,"));

        Optional<Column> id = getIdColumn(table);
        if (!id.isPresent()) throw new IdNotFoundException(table.getClass());

        buffer.deleteCharAt(buffer.length() - 1).append(" where ").append(id.get().name).append("=?");
        return buffer.toString();
    }

    public static String createExistsQuery(Table table) {
        Optional<Column> id = getIdColumn(table);
        if (!id.isPresent()) throw new IdNotFoundException(table.getClass());

        StringBuffer buffer = new StringBuffer("select ").append(id.get().name).append(" from ").append(table.getName()).append(" where ").append(id.get().name).append("=?");
        return buffer.toString();
    }

    public static String createDeleteQuery(Table table) {
        Optional<Column> id = getIdColumn(table);
        if (!id.isPresent()) throw new IdNotFoundException(table.getClass());

        StringBuffer buffer = new StringBuffer("delete from ").append(table.getName()).append(" where ").append(id.get().name).append("=?");
        return buffer.toString();
    }

    public static void setupFilter(PreparedStatement statement, QueryFilter filter) throws SQLException {
        int id = 1;
        statement.setObject(id++, ((Serializer<Object, Object>) filter.whereFilter.column.serializer).encoder.encode(filter.whereFilter.value));
        for (QueryFilter.Filter f : filter.andFilters)
            statement.setObject(id++, ((Serializer<Object, Object>) f.column.serializer).encoder.encode(f.value));
        for (QueryFilter.Filter f : filter.orFilters)
            statement.setObject(id++, ((Serializer<Object, Object>) f.column.serializer).encoder.encode(f.value));
    }

    public static Optional<Column> getIdColumn(Table table) {
        return Arrays.stream(table.getColumns()).filter(Column::isId).findFirst();
    }

    public static Stream<Column> getTargetColumns(Table table) {
        return getTargetColumns(table.getColumns());
    }

    public static Stream<Column> getTargetColumns(Column[] columns) {
        return Arrays.stream(columns).filter(c -> !c.isAutoIncrement());
    }

}