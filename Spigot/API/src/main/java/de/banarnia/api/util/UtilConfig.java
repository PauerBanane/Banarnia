package de.banarnia.api.util;

/* UtilConfig Klasse.
 * Enthält Methoden in Bezug auf YAML-Config Dateien.
 */
public class UtilConfig {

    // Nicht erlaubte Zeichen aus String löschen (z.B. '.', da dies eine Abgrenzung für ConfigurationSection ist)
    public String replaceRestrictedChars(String s) {
        return s.replace(".", "_");
    }

}
