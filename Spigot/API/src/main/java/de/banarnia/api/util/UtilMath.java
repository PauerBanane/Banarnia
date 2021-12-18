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

    // Wertet aus, ob der String ein Float ist.
    public static boolean isFloat(String amount) {
        try {
            // Float von String parsen.
            Float.parseFloat(amount);
            return true;
        } catch (NumberFormatException e) {
            // Error abfangen, wenn der String kein Float ist.
            return false;
        }
    }

    // Wertet aus, ob der String ein Double ist.
    public static boolean isDouble(String amount) {
        try {
            // Double von String parsen.
            Double.parseDouble(amount);
            return true;
        } catch (NumberFormatException e) {
            // Error abfangen, wenn der String kein Double ist.
            return false;
        }
    }

}
