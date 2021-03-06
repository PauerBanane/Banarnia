package de.banarnia.api.util;

import org.apache.commons.lang.Validate;

import java.util.List;

/* UtilString
 * Utility-Klasse für diverse String-Methoden.
 */
public class UtilString {

    // String-Vergleich mit mehreren Strings
    public static boolean equals(String input, String... compare) {
        for (String s : compare)
            if (s.equals(input))
                return true;

        return false;
    }

    // String-Vergleich mit mehreren Strings, unabhängig von Groß- und Kleinschreibung
    public static boolean equalsIgnoreCase(String input, String... compare) {
        for (String s : compare)
            if (s.equals(input))
                return true;

        return false;
    }

    // Abfragen, ob eine String-Liste einen String enthält - unabhängig von Groß- und Kleinschreibung
    public static boolean containsIgnoreCase(List<String> list, String word) {
        // Gesamte Liste durchgehen und Strings vergleichen
        for (String line : list)
            if (line.equalsIgnoreCase(word))
                return true;

        // False zurückgeben, wenn kein String gleich ist
        return false;
    }

    // Führt eine List<String> zu einem String zusammen
    public static String mergeStringList(List<String> list) {
        // Null check
        if (list == null)
            throw new IllegalArgumentException();

        // Hauptmethode aufrufen
        return mergeStringList(list, null);
    }

    // Führt eine List<String> zusammen und trennt die Einträge mit einem Separator
    public static String mergeStringList(List<String> list, String separator) {
        // Null check
        if (list == null)
            throw new IllegalArgumentException();

        // Abfrage, ob ein separator vorhanden ist
        boolean addSeparator = separator != null;

        // Variable für Ergebnis
        StringBuilder builder = new StringBuilder();

        // Einträge einfügen
        for (int i = 0; i < list.size(); i++) {
            // Eintrag aus der Liste anhängen
            builder.append(list.get(i));

            // Separator hinzufügen, wenn einer vorhanden ist und es nicht der letzte String ist
            if (addSeparator && i < list.size() - 1)
                builder.append(separator);
        }

        // Ergebnis zurückgeben
        return builder.toString();
    }

}
