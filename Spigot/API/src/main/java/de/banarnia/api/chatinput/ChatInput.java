package de.banarnia.api.chatinput;

import de.banarnia.api.BanarniaAPI;
import de.banarnia.api.messages.Message;
import de.banarnia.api.util.Title;
import de.banarnia.api.util.UtilMath;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

/* ChatInput
 * Klasse für das einfache Handling von Chat-Eingaben.
 */
public class ChatInput<T> {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Variablen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Datentyp als Klasse speichern
    private Class<T> type;

    // Abfrage, ob die Eingabe abgebrochen werden kann
    protected boolean cancelable        = true;

    // Abfrage, ob die Eingabe im Chat angezeigt werden soll
    protected boolean withLocalEcho     = false;

    // Abfrage, ob das Inventar zu Beginn der Conversation geschlossen werden soll
    protected boolean closeInventory    = true;

    // Abfrage, ob ein Sound abgespielt werden soll
    protected boolean playSound         = true;

    // Visualisierung
    protected String message;
    protected Title  title;

    // BiConsumer, der akzeptiert wird, sobald der Spieler die Nachricht eingegeben hat
    private BiConsumer<T, Boolean> consumer;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ChatInput(Class<T> type) {
        // Null check
        if (type == null)
            throw new IllegalArgumentException();

        // Datentyp abspeichern
        this.type = type;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Conversation ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ChatInput<T> start(Player player) {
        // Inventar des Spielers schließen, wenn die Option aktiviert wurde
        if (closeInventory)
            player.closeInventory();

        // Conversation erstellen
        ConversationFactory factory = new ConversationFactory(BanarniaAPI.getInstance());
        factory.withFirstPrompt(new PlayerResponsePrompt(this));
        factory.withLocalEcho(withLocalEcho);

        // Conversation beginnen
        player.beginConversation(factory.buildConversation(player));

        // Title abspielen
        if (title != null)
            title.send(player);

        // Nachricht senden
        player.sendMessage(message);

        // Instanz zurückgeben
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Eingabe & Auswertung ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Eingabe auswerten
    protected ChatInput<T> onResponse(String message, boolean canceled) {
        // Eingegebene Nachricht in richtigen Datentyp umwandeln - Null wenn fehlgeschlagen
        T result = null;
        try {
            result = convertInput(message);
        } catch (ClassCastException ex) {}

        // Wenn der result nicht der richtige Datentyp ist, wird er auf null gesetzt
        if (!(result instanceof T))
            result = null;

        if (result == null) {
            BanarniaAPI.getInstance().getLogger().warning("Failed to resolve ChatInput for " + type.getName());
            canceled = true;
        }

        // Consumer akzeptieren
        consumer.accept(result, canceled);

        // Instanz zurückgeben
        return this;
    }

    private T convertInput(String input) {
        // Null check
        if (input == null)
            return null;
        if (type == null)
            return null;

        // If-Verzweigung für verschiedene Datentypen - CASE ist mit Klassen nur umständlich möglich
        if (type == String.class)
            return (T) input.replace("&", "§");

        if (type == Boolean.class) {
            if (input.equalsIgnoreCase(Boolean.TRUE.toString()))
                return (T) Boolean.TRUE;

            if (input.equalsIgnoreCase(Boolean.FALSE.toString()))
                return (T) Boolean.FALSE;
        }

        if (type == Integer.class || type == int.class) {
            // Abfrage, ob die Eingabe ein int ist
            if (UtilMath.isInt(input)) {
                // Integer parsen
                Integer value = Integer.parseInt(input);

                // Wert zurückgeben
                return (T) value;
            }
        }

        if (type == Float.class || type == float.class) {
            // Abfrage, ob die Eingabe ein Float ist
            if (UtilMath.isFloat(input)) {
                // Float parsen
                Float value = Float.parseFloat(input);

                // Wert zurückgeben
                return (T) value;
            }
        }

        if (type == Double.class || type == double.class){
            // Abfrage, ob die Eingabe ein Double ist
            if (UtilMath.isFloat(input)) {
                // Float parsen
                Double value = Double.parseDouble(input);

                // Wert zurückgeben
                return (T) value;
            }
        }

        // Null zurückgeben, wenn für den Datentyp noch keine Auswertung programmiert wurde
        return null;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Chain Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Festlegen, ob die Eingabe abgebrochen werden kann
    public ChatInput<T> setCancelable(boolean cancelable) {
        this.cancelable = cancelable;

        // Instanz zurückgeben
        return this;
    }

    // Festlegen, ob die Antwort im Chat des Spielers angezeigt wird
    public ChatInput<T> withLocalEcho(boolean withLocalEcho) {
        this.withLocalEcho = withLocalEcho;

        // Instanz zurückgeben
        return this;
    }

    // Festlegen, ob das Inventar zu Beginn der Conversation geschlossen werden soll
    public ChatInput<T> closeInventory(boolean closeInventory) {
        this.closeInventory = closeInventory;

        // Instanz zurückgeben
        return this;
    }

    // Festlegen, ob ein Sound bei Beginn abgespielt werden soll
    public ChatInput<T> playSound(boolean playSound) {
        this.playSound = playSound;

        // Instanz zurückgeben
        return this;
    }

    // Festlegen, welche Nachricht im Chat zu Beginn der Konversation angezeigt wird
    public ChatInput<T> message(String message) {
        // Null check
        if (message == null)
            return this;

        this.message = message;

        // Instanz zurückgeben
        return this;
    }

    // Title festlegen - mit String als Haupttitle
    public ChatInput<T> title(String title) {
        // Null check
        if (title == null)
            return this;

        return title(title, Message.CHAT_INPUT_DEFAULT_SUBTITLE.get());
    }

    // Title festlegen - mit String als Haupttitle und String als Subtitle
    public ChatInput<T> title(String title, String subTitle) {
        // Null check
        if (title == null || subTitle == null)
            return this;

        return title(Title.of("§6" + title, "§e" + subTitle));
    }

    // Festlegen, welcher Title zu Beginn der Konversation angezeigt wird
    public ChatInput<T> title(Title title) {
        // Null check
        if (title == null)
            return this;

        this.title = title;

        // Instanz zurückgeben
        return this;
    }

    // Consumer für die weitere Bearbeitung nach der Eingabe des Spielers
    public ChatInput<T> response(BiConsumer<T, Boolean> response) {
        // Null check
        if (response == null)
            return this;

        this.consumer = response;

        // Instanz zurückgeben
        return this;
    }

}
