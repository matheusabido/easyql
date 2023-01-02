package dev.abidux.easyql;

import dev.abidux.easyql.util.Column;
import dev.abidux.easyql.util.Table;
import dev.abidux.easyql.util.serializer.Serializers;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ExampleTable extends Table<ExampleTable.Example> {

    public static final Column<Integer> ID = new Column<>("id", Serializers.INTEGER_SERIALIZER).id().autoIncrement();
    public static final Column<String> NAME = new Column<>("name", Serializers.STRING_SERIALIZER);
    public static final Column<Float> MONEY = new Column<>("money", Serializers.FLOAT_SERIALIZER);
    public static final Column<ArrayList<String>> ITEMS = new Column<>("items", Serializers.STRING_ARRAYLIST_SERIALIZER);
    public static final Column<ArrayList<Integer>> PLOTS = new Column<>("plots", Serializers.INTEGER_ARRAYLIST_SERIALIZER);
    public ExampleTable() {
        super(rs -> new Example(read(ID, rs), read(NAME, rs), read(MONEY, rs), read(ITEMS, rs), read(PLOTS, rs)));
    }

    @Override
    public String getName() {
        return "example";
    }

    @Override
    public Column[] getColumns() {
        return new Column[] {ID, NAME, MONEY, ITEMS, PLOTS};
    }

    public static class Example implements TableObject {

        public final int id;
        public String name;
        public float money;
        public final ArrayList<String> items;
        public final ArrayList<Integer> plots;
        public Example(int id, String name, float money, ArrayList<String> items, ArrayList<Integer> plots) {
            this.id = id;
            this.name = name;
            this.money = money;
            this.items = items;
            this.plots = plots;
        }

        @Override
        public Object getColumnValue(Column column) {
            switch(column.name) {
                case "id": return id;
                case "name": return name;
                case "money": return money;
                case "items": return items;
                case "plots": return plots;
            }
            return null;
        }

        @Override
        public String toString() {
            return "[" + id + "] " + name + " (" + money + "): " + items.stream().collect(Collectors.joining(", ")) + " | " + plots.stream().map(String::valueOf).collect(Collectors.joining(";"));
        }
    }

}