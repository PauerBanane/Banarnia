package de.banarnia.api.messages;

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

}
