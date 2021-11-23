package de.banarnia.api.util;

import de.banarnia.api.messages.Language;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;

/* UtilTime
 * Utility-Klasse für diverse Zeit-Methoden.
 */
public class UtilTime {

    /* DayOfWeek durch einen String erhalten.
     * Gibt einen DayOfWeek zurück, wenn ein gültiger String übergeben wird.
     * Zulässig sind Zahlen 1-7, sowie die deutschen und englischen Monatsnamen.
     */
    public static DayOfWeek getDayOfWeek(String input) {
        // Variable für den DayOfWeek
        DayOfWeek day = null;

        // Versuchen den DayOfWeek direkt zu parsen
        try {
            day = DayOfWeek.valueOf(input);

            if (day != null)
                return day;
        } catch (Exception ex) {}

        // Abfrage, ob der String eine Zahl ist
        if (UtilMath.isInt(input)) {

            // String zu Integer parsen.
            int parsed = Integer.parseInt(input);

            // Integer zu DayOfWeek zuordnen
            switch (parsed) {

                case 1:
                    day = DayOfWeek.MONDAY;
                    break;
                case 2:
                    day = DayOfWeek.TUESDAY;
                    break;
                case 3:
                    day = DayOfWeek.WEDNESDAY;
                    break;
                case 4:
                    day = DayOfWeek.THURSDAY;
                    break;
                case 5:
                    day = DayOfWeek.FRIDAY;
                    break;
                case 6:
                    day = DayOfWeek.SATURDAY;
                    break;
                case 7:
                    day = DayOfWeek.SUNDAY;
                    break;
            }

            return day;
        }

        // Versuchen den DayOfWeek über die Übersetzung zu bekommen
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            String javaName         = dayOfWeek.name();
            String translatedName   = Language.getName(dayOfWeek);

            if (javaName.equalsIgnoreCase(input) || translatedName.equalsIgnoreCase(input))
                return dayOfWeek;
        }

        // Null zurückgeben, wenn DayOfWeek nicht zugeordnet werden konnte
        return null;
    }

    /* Month durch einen String erhalten.
     * Gibt einen Month zurück, wenn ein gültiger String übergeben wird.
     * Zulässig sind Zahlen 1-12, sowie die Java und übersetzten Monatsnamen.
     */
    public static Month getMonth(String input) {
        // Variable für den Monat.
        Month month = null;

        // Abfrage, ob der String eine Zahl ist.
        if (UtilMath.isInt(input)) {

            // String zu Integer parsen.
            int parsed = Integer.parseInt(input);

            // Versuchen den Monat durch die Zahl zu erhalten.
            try {
                month = Month.of(parsed);

                // Error abfangen, wenn die Zahl ungültig ist.
            } catch (Exception ex) {}

        } else {

            // For-Schleife durch alle Monate
            for (Month forMonth : Month.values()) {
                // Java-Namen und übersetzten Namen in Variable speichern
                String javaName         = forMonth.name();
                String translatedName   = Language.getName(forMonth);

                // Abfrage, ob der input einem der beiden Namen entspricht
                if (input.equalsIgnoreCase(javaName) || input.equalsIgnoreCase(translatedName))
                    month = forMonth;
            }

        }

        // Wert des Monats zuürckgeben.
        // Entspricht null, wenn der String kein gültiger Monat ist.
        return month;
    }

    /* Year durch einen String erhalten.
     * Versucht einen String zu einem Integer zu parsen.
     * Gibt den Wert der Methode #getYear(int input) zurück, wenn der String ein int ist.
     */
    public static Year getYear(String input) {
        // Abfrage, ob der String ein Integer ist.
        if (!UtilMath.isInt(input))
            return null;

        // Stammfunktion zurückgeben.
        return getYear(Integer.parseInt(input));
    }

    /* Year durch einen Integer erhalten.
     * Versucht einen int zu einem Year zu parsen.
     * Gibt null zurück, wenn der Wert ungültig ist.
     */
    public static Year getYear(int input) {
        // Variable für das Year
        Year year = null;

        // Versuchen das Year zu parsen
        try {
            year = Year.of(input);
        } catch (Exception ex) {}

        // Year zurückgeben
        return year;
    }

}
