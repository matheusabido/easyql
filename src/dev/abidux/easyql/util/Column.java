package dev.abidux.easyql.util;

import dev.abidux.easyql.util.serializer.Serializer;

public class Column<E> {

    public final String name;
    private boolean autoIncrement, id;
    public final Serializer<E, ?> serializer;
    public Column(String name, Serializer<E, ?> serializer) {
        this.name = name;
        this.serializer = serializer;
    }

    public Column id() {
        this.id = true;
        return this;
    }

    public Column autoIncrement() {
        this.autoIncrement = true;
        return this;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public boolean isId() {
        return id;
    }
}