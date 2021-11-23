package de.banarnia.api.util;

/* UtilMath
 * Utility-Klasse für diverse mathematische Methoden.
 */
public class UtilMath {

    // Wertet aus, ob der String ein Integer ist.
    public static boolean isInt(String amount) {
        try {
            // Integer von String parsen.
            Integer.parseInt(amount);
            return true;
        } catch (NumberFormatException e) {
            // Error abfangen, wenn der String kein Integer ist.
            return false;
        }
    }

}
