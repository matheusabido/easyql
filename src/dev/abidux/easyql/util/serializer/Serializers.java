package dev.abidux.easyql.util.serializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Serializers {

    public static final Serializer<String, String> STRING_SERIALIZER = new Serializer<>("text", s -> s, s -> s);
    public static final Serializer<Integer, Integer> INTEGER_SERIALIZER = new Serializer<>("int", i -> i, i -> i);
    public static final Serializer<Float, Float> FLOAT_SERIALIZER = new Serializer<>("float", f -> f, f -> f);
    public static final Serializer<ArrayList<String>, String> STRING_ARRAYLIST_SERIALIZER = new Serializer<>("text", list -> list.stream().collect(Collectors.joining("/sep/")), Serializers::decodeStringArrayList);
    public static final Serializer<ArrayList<Integer>, String> INTEGER_ARRAYLIST_SERIALIZER = new Serializer<>("text", list -> list.stream().map(String::valueOf).collect(Collectors.joining(";")), Serializers::decodeIntegerArrayList);

    private static ArrayList<String> decodeStringArrayList(String encodedList) {
        ArrayList<String> list = new ArrayList<>();
        Arrays.stream(encodedList.split("/sep/")).forEach(list::add);
        return list;
    }

    private static ArrayList<Integer> decodeIntegerArrayList(String encodedList) {
        ArrayList<Integer> list = new ArrayList<>();
        Arrays.stream(encodedList.split(";")).map(Integer::parseInt).forEach(list::add);
        return list;
    }

}