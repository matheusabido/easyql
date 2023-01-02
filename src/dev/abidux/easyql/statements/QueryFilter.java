package dev.abidux.easyql.statements;

import dev.abidux.easyql.exception.WhereNotFoundException;
import dev.abidux.easyql.util.Column;

import java.util.ArrayList;

public class QueryFilter {

    protected Filter<?> whereFilter;
    public <T> QueryFilter where(Column<T> column, T value) {
        this.whereFilter = new Filter(column, value);
        return this;
    }

    protected final ArrayList<Filter<?>> andFilters = new ArrayList<>();
    public <T> QueryFilter and(Column<T> column, T value) {
        andFilters.add(new Filter(column, value));
        return this;
    }

    protected final ArrayList<Filter<?>> orFilters = new ArrayList<>();
    public <T> QueryFilter or(Column<T> column, T value) {
        orFilters.add(new Filter(column, value));
        return this;
    }

    public String build() {
        if (whereFilter == null) throw new WhereNotFoundException();
        StringBuffer buffer = new StringBuffer("where ").append(whereFilter.column.name).append("=?");
        andFilters.stream().forEach(f -> buffer.append(" and ").append(f.column.name).append("=?"));
        orFilters.stream().forEach(f -> buffer.append(" or ").append(f.column.name).append("=?"));
        return buffer.toString();
    }

    public static class Filter<T> {

        public final Column<T> column;
        public final T value;
        public Filter(Column<T> column, T value) {
            this.column = column;
            this.value = value;
        }

    }

}