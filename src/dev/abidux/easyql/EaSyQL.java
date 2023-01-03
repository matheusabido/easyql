package dev.abidux.easyql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

public class EaSyQL {

    private static String lastVersion;
    public static boolean isUpdated() {
        return getLastVersion().equals(getVersion());
    }

    public static String getLastVersion() {
        if (lastVersion == null) {
            try (InputStream input = new URL("https://raw.githubusercontent.com/mabidux/easyql/main/README.md").openStream()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                    String version = reader.lines().filter(l -> l.startsWith("Current Version: ")).map(l -> l.split("Current Version: ")[1]).collect(Collectors.joining());
                    lastVersion = version;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return lastVersion;
    }

    public static void sendOutdatedMessage() {
        System.out.println(String.format("eaSyQL v%s is outdated. Please, update it to v%s in the official github page.", getVersion(), getLastVersion()));
    }

    public static String getVersion() {
        return "1.0.1";
    }

}