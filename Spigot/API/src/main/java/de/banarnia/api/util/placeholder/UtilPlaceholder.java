package de.banarnia.api.util.placeholder;

import de.banarnia.api.messages.Message;
import de.banarnia.api.util.placeholder.PapiWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/* Klasse UtilPlaceholder.
 * Beinhaltet Möglichkeiten zur direkten Nutzung von PAPI oder Standard-Platzhaltern.
 */
public class UtilPlaceholder {

    // Abfrage, ob PAPI installiert ist
    public static boolean hasPapiSupport() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Placeholder parsen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Placeholder einfügen, ohne Empfängernamen und Prefix
    public static String parse(String message) {
        return parse(null, null, message);
    }

    // Placeholder einfügen, ohne Empfängernamen
    public static String parse(String topic, String message) {
        return parse(null, topic, message);
    }

    // Placeholder einfügen, ohne Prefix
    public static String parse(CommandSender receiver, String message) {
        return parse(receiver, null, message);
    }

    // Placeholder einfügen, mit Prefix
    public static String parse(CommandSender receiver, String topic, String message) {
        // Standard-Platzhalter anpassen
        String parsed = parseDefaultPlaceholders(receiver, message);

        // Topic einfügen
        parsed = parsed.replace("%topic%", topic);

        // PAPI Placeholder einfügen, wenn vorhanden - und wenn es um einen Spieler geht
        if (receiver != null && receiver instanceof Player  &&  hasPapiSupport())
            parsed = PapiWrapper.parse((Player) receiver, message);

        // Richtigen String zurückgeben
        return parsed;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Standard-Placeholder parsen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Standard-Placeholder einfügen - ohne Empfänger
    public static String parseDefaultPlaceholders(String message) {
        return parseDefaultPlaceholders(null, message);
    }

    // Standard-Placeholder einfügen
    public static String parseDefaultPlaceholders(CommandSender receiver, String message) {
        // Namen des Empfängers erhalten
        String receiverName = null;
        if (receiver != null && receiver instanceof Player)
            receiverName = receiver.getName();
        else
            receiverName = Message.LANGUAGE_CONSOLE_SENDER_NAME.get();

        // Neue Variable anlegen
        String parsed = String.valueOf(message);

        // Platzhalter einsetzen
        parsed = parsed.replace("%player%", receiverName)
                       .replace("%prefix%", Message.CHAT_PREFIX.get())
                       .replace("%prefix_error%", Message.CHAT_PREFIX_ERROR.get());

        // Neue Variable zurückgeben
        return parsed;
    }

}
