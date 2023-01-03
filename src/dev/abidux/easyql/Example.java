package dev.abidux.easyql;

import dev.abidux.easyql.database.MySQL;
import dev.abidux.easyql.statements.QueryFilter;
import dev.abidux.easyql.statements.Statements;
import dev.abidux.easyql.util.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Example {

    private static final MySQL SQL = new MySQL("localhost", "3306", "test", "root", "");
    private static final Table<ExampleTable.Example> EXAMPLE_TABLE = new ExampleTable();

    public static void main(String[] args) {
        // OPTIONAL: takes some time to run
        if (!EaSyQL.isUpdated()) {
            EaSyQL.sendOutdatedMessage();
        }

        // USAGE
        ArrayList<ExampleTable.Example> list = new ArrayList<>();
        SQL.connect(connection -> {
            Statements statements = new Statements(connection);

            System.out.println("Creating example table...");
            statements.create(EXAMPLE_TABLE);
            System.out.println("Table created.");

            // populate if empty
            if (statements.select(EXAMPLE_TABLE).size() == 0) {
                System.out.println("Populating example table...");
                populateExampleTable(statements);
                System.out.println("Table populated.");
            }

            System.out.println("Reading data...");
            statements.select(EXAMPLE_TABLE).forEach(list::add);
            System.out.println("Read.");

            list.forEach(System.out::println);

            if (list.get(1).money <= 50) {
                System.out.println("Updating " + list.get(1).name + "'s money");
                list.get(1).money = 10000;
                statements.update(EXAMPLE_TABLE, list.get(1));
                System.out.println("Updated.");
            }

            System.out.println("Deleting chicken (if he's still alive)...");
            if (list.size() == 3) {
                statements.delete(EXAMPLE_TABLE, new QueryFilter().where(ExampleTable.ID, 3));
                System.out.println("Chicken deleted.");
            }
        });
    }

    private static void populateExampleTable(Statements statements) {
        List.of(
                new ExampleTable.Example(0, "abidux", 100, randomItems(), randomPlots()),
                new ExampleTable.Example(0, "gallico", 50, randomItems(), randomPlots()),
                new ExampleTable.Example(0, "chicken", 1000, randomItems(), randomPlots())
        ).forEach(e -> statements.insert(EXAMPLE_TABLE, e));
    }

    private static final String[] items = {"SWORD", "ARROW", "BOW", "POTION", "ARMOR"};
    private static ArrayList<String> randomItems() {
        ArrayList<String> items = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 3; i++) items.add(Example.items[random.nextInt(Example.items.length)]);
        return items;
    }

    private static ArrayList<Integer> randomPlots() {
        ArrayList<Integer> plots = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 3; i++) plots.add(random.nextInt(100));
        return plots;
    }

}