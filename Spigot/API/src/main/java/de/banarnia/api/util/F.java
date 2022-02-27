package de.banarnia.api.util;

import com.google.protobuf.Api;
import de.banarnia.api.config.ApiConfig;
import de.banarnia.api.messages.Message;
import de.banarnia.api.util.placeholder.UtilPlaceholder;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/* F Klasse.
 * Util-Klasse zum Einfachen verschicken von Nachrichten an Spieler.
 */
public class F {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Boolean Nachrichten ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Wenn TRUE Haken zurückgeben, wenn FALSE Kreuz zurückgeben
    public static String checkOrCross(boolean b) {
        return b ? Message.CHECK.get() : Message.CROSS.get();
    }

    // Wenn TRUE Haken zurückgeben, sonst andere Nachricht
    public static String checkOrElse(boolean b, String ifFalse) {
        return b ? Message.CHECK.get() : ifFalse;
    }

    // Wenn FALSE Kreuz zurückgeben, sonst andere Nachricht
    public static String crossOrElse(boolean b, String ifTrue) {
        return b ? ifTrue : Message.CROSS.get();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Nachrichten erstellen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Standard-Nachricht erhalten - verwendet definierten Prefix aus Message-Klasse.
    public static String main(String prefix, String message) {
        return main(null, prefix, message);
    }

    // Standard-Nachricht erhalten - verwendet Prefix und Spielernamen
    public static String main(CommandSender receiver, String prefix, String message) {
        // Nachricht formatieren
        message = formatMessage(message, false);

        // Placeholder einfügen
        return UtilPlaceholder.parse(receiver, prefix, message);
    }

    // Error-Nachricht erhalten - verwendet definierten Prefix aus Message-Klasse.
    public static String error(String prefix, String message) {
        return main(null, prefix, message);
    }

    // Error-Nachricht erhalten - verwendet Prefix und Spielernamen
    public static String error(CommandSender receiver, String prefix, String message) {
        // Nachricht formatieren
        message = formatMessage(message, true);

        // Placeholder einfügen
        return UtilPlaceholder.parse(receiver, prefix, message);
    }

    // Prefix in die Nachricht einfügen
    private static String formatMessage(String message, boolean error) {
        // Prefix erhalten
        String prefix = error ? Message.CHAT_PREFIX_ERROR.get() : Message.CHAT_PREFIX.get();

        // Nachricht mit Prefix zurückgeben
        return prefix + message;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Nachrichten verschicken ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Standard-Nachricht verschicken - Nachricht entspricht #main()
    public static void send(String prefix, String message, List<CommandSender> receivers) {
        // Liste in Array wandeln
        CommandSender[] receiverArray = receivers != null ? receivers.toArray(new CommandSender[receivers.size()]) : null;

        // Nachricht senden
        sendMessage(false, false, prefix, message, receiverArray);
    }

    // Standard-Nachricht verschicken - Nachricht entspricht #main()
    public static void send(String prefix, String message, CommandSender... receivers) {
        sendMessage(false, false, prefix, message, receivers);
    }

    // Error-Nachricht verschicken - Nachricht entspricht #error()
    public static void sendError(String prefix, String message, List<CommandSender> receivers) {
        // Liste in Array wandeln
        CommandSender[] receiverArray = receivers != null ? receivers.toArray(new CommandSender[receivers.size()]) : null;

        // Nachricht senden
        sendMessage(true, true, prefix, message, receiverArray);
    }

    // Error-Nachricht verschicken - Nachricht entspricht #error() mit Sound
    public void sendError(String prefix, String message, CommandSender... receivers) {
        sendMessage(true, true, prefix, message, receivers);
    }

    // Error-Nachricht verschicken - Nachricht entspricht #error()
    public void sendError(boolean withSound, String prefix, String message, CommandSender... receivers) {
        sendMessage(true, withSound, prefix, message, receivers);
    }

    // Methode zum Verschicken der Nachricht
    private static void sendMessage(boolean error, boolean withSound, String prefix, String message, CommandSender... receivers) {
        // Beenden, wenn receivers == null
        if (receivers == null || receivers.length == 0) return;

        // For-Schleife durch alle Spieler
        for (CommandSender receiver : receivers) {
            // Nachricht erhalten
            String parsedMessage = error ? main(receiver, prefix, message) : error(receiver, prefix, message);

            // Nachricht senden
            receiver.sendMessage(parsedMessage);

            // Sound abspielen, falls gewünscht
            if (receiver instanceof Player && withSound)
                UtilPlayer.playSound((Player) receiver, ApiConfig.ERROR_SOUND());
        }
    }

}
