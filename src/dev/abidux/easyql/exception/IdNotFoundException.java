package dev.abidux.easyql.exception;

import dev.abidux.easyql.util.Table;

public class IdNotFoundException extends RuntimeException {

    private Class<?> tableClass;
    public IdNotFoundException(Class<? extends Table> tableClass) {
        this.tableClass = tableClass;
    }

    @Override
    public String toString() {
        return tableClass.getSimpleName() + " has no id";
    }
}