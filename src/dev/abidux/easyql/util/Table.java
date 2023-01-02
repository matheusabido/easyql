package dev.abidux.easyql.util;

import dev.abidux.easyql.util.serializer.Serializer;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Table<E extends Table.TableObject> {

    public final Reader<E> reader;
    public Table(Reader<E> reader) {
        this.reader = reader;
    }

    public abstract String getName();
    public abstract Column[] getColumns();

    public static <T> T read(Column<T> column, ResultSet rs) throws SQLException {
        return ((Serializer<T, Object>) column.serializer).decoder.decode(rs.getObject(column.name));
    }

    public interface TableObject {
        Object getColumnValue(Column column);
    }

}