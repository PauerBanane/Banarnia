package de.banarnia.api.util;

/* UtilEnum Klasse.
 * Enthält Standard Methoden fürs Enums.
 */
public class UtilEnum {

    // Abfrage, ob ein Wert in einem Enum heißt wie ein bestimmter String
    public static <T> boolean contains(T[] values, String s) {
        return get(values, s) != null;
    }

    // Enum-Wert anhand eines Strings bekommen
    public static <T> T get(T[] values, String name) {
        // Null check
        if (values == null || name == null)
            return null;

        // For-Schleife durch das Array um die Namen zu vergleichen
        for (T key : values)
            if (key != null && key.toString().equalsIgnoreCase(name))
                return key;

        // Null zurückgeben, wenn der Wert nicht gefunden wurde
        return null;
    }

}
