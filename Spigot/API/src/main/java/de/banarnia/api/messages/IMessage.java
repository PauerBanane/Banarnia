package de.banarnia.api.messages;

import co.aikar.commands.annotation.CommandAlias;
import de.banarnia.api.util.F;
import de.banarnia.api.util.placeholder.UtilPlaceholder;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Enumeration;

/* Interface IMessage
 * Liefert die Standard-Methoden um eine Language-Datei zu erstellen.
 */
public interface IMessage {

    // Gibt den Key in der Config wieder
    String getKey();

    // Gibt die Standard-Nachricht wieder
    String getDefaultMessage();

    // Gibt die aktuelle Nachricht wieder, insofern sie nicht null ist
    String get();

    // Gibt die aktuelle Nachricht wieder und ersetzt einen Inhalt.
    default String replace(String prev, String replacement) {
        return get().replace(prev, replacement);
    }

    // Setzt die aktuelle Nachricht
    void set(String message);

    // Gibt die Nachricht mit Placeholdern wieder
    default String getParsed(CommandSender receiver) {
        return UtilPlaceholder.parse(receiver, get());
    }

    // Sendet die Nachricht an einen Spieler oder die Konsole
    default void send(CommandSender receiver) {
        // Nachricht senden
        receiver.sendMessage(getParsed(receiver));
    }

    // Sendet die Nachricht mit Prefix von F#main()
    default void sendWithPrefix(CommandSender receiver, String prefix) {
        // Nachricht senden
        F.send(prefix, getParsed(receiver), receiver);
    }

    // Sendet die Nachricht mit Error Prefix
    default void sendAsError(CommandSender receiver, String prefix) {
        // Nachricht senden
        F.send(prefix, getParsed(receiver), receiver);
    }

}
