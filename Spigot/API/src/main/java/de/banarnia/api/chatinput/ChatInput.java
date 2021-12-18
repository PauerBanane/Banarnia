package de.banarnia.api.chatinput;

import com.google.common.collect.Maps;
import de.banarnia.api.BanarniaAPI;
import de.banarnia.api.util.Title;
import de.banarnia.api.util.UtilMath;
import org.apache.commons.lang.Validate;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ChatInput<T> {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Eingabe Auswertung ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Map mit allen möglichen Objects
    private static Map<Class, Supplier<String>> registeredObjects = Maps.newHashMap();

    // Neues Object registrieren
    public static void registerInputObject(Class clazz, Supplier<String> message) {
        registeredObjects.put(clazz, message);
    }

    // Object von der Map entfernen
    public static void unregisterObject(Class clazz) {
        registeredObjects.remove(clazz);
    }

    // Abfrage, ob das Objekt registriert ist
    public static boolean isRegistered(Class clazz) {
        return registeredObjects.containsKey(clazz);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Variablen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Datentyp als Klasse speichern
    private Class<T> type;

    // Abfrage, ob die Eingabe abgebrochen werden kann
    protected boolean cancelable        = true;

    // Abfrage, ob die Eingabe im Chat angezeigt werden soll
    protected boolean withLocalEcho     = false;

    // Abfrage, ob das Inventar zu Beginn der Conversation geschlossen werden soll
    protected boolean closeInventory    = true;

    // Visualisierung
    protected String message;
    protected Title  title;

    // BiConsumer, der akzeptiert wird, sobald der Spieler die Nachricht eingegeben hat
    private BiConsumer<T, Boolean> consumer;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ChatInput(Class<T> type) {
        // Null check
        Validate.notNull(type);

        // Datentyp abspeichern
        this.type = type;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Conversation ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ChatInput start(Player player) {
        // Inventar des Spielers schließen, wenn die Option aktiviert wurde
        if (closeInventory)
            player.closeInventory();

        // Conversation erstellen
        ConversationFactory factory = new ConversationFactory(BanarniaAPI.getInstance());
        factory.withFirstPrompt(new PlayerResponsePrompt(this));
        factory.withLocalEcho(withLocalEcho);

        // Conversation beginnen
        player.beginConversation(factory.buildConversation(player));

        // Instanz zurückgeben
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Eingabe & Auswertung ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Eingabe auswerten
    protected ChatInput onResponse(String message, boolean canceled) {
        // Eingegebene Nachricht in richtigen Datentyp umwandeln - Null wenn fehlgeschlagen
        T result = convertInput(message);

        // Wenn der result nicht der richtige Datentyp ist, wird er auf null gesetzt
        if (!(result instanceof T))
            result = null;

        // Consumer akzeptieren
        if (consumer != null)
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

        // Supplier für die Klasse aufrufen
        Supplier<String> supplier

        // If-Verzweigung für verschiedene Datentypen - CASE ist mit Klassen nur umständlich möglich
        if (T instanceof String)
            return (T) input;

        if (type == Boolean.class) {
            if (input.equalsIgnoreCase(Boolean.TRUE.toString()))
                return (T) Boolean.TRUE;

            if (input.equalsIgnoreCase(Boolean.FALSE.toString()))
                return (T) Boolean.FALSE;
        }

        if (type == Integer.class) {
            // Abfrage, ob die Eingabe ein int ist
            if (UtilMath.isInt(input)) {
                // Integer parsen
                Integer value = Integer.parseInt(input);

                // Wert zurückgeben
                return (T) value;
            }
        }

        if (type == Float.class) {
            // Abfrage, ob die Eingabe ein Float ist
            if (UtilMath.isFloat(input)) {
                // Float parsen
                Float value = Float.parseFloat(input);

                // Wert zurückgeben
                return (T) value;
            }
        }

        if (type == Double.class){
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
    public ChatInput setCancelable(boolean cancelable) {
        this.cancelable = cancelable;

        // Instanz zurückgeben
        return this;
    }

    // Festlegen, ob die Antwort im Chat des Spielers angezeigt wird
    public ChatInput withLocalEcho(boolean withLocalEcho) {
        this.withLocalEcho = withLocalEcho;

        // Instanz zurückgeben
        return this;
    }

    // Festlegen, ob das Inventar zu Beginn der Conversation geschlossen werden soll
    public void closeInventory(boolean closeInventory) {
        this.closeInventory = closeInventory;
    }

    // Festlegen, welche Nachricht im Chat zu Beginn der Konversation angezeigt wird
    public ChatInput message(String message) {
        this.message = message;

        // Instanz zurückgeben
        return this;
    }

    // Festlegen, welcher Title zu Beginn der Konversation angezeigt wird
    public ChatInput title(Title title) {
        this.title = title;

        // Instanz zurückgeben
        return this;
    }

    // Consumer für die weitere Bearbeitung nach der Eingabe des Spielers
    public ChatInput response(BiConsumer<T, Boolean> response) {
        this.consumer = response;

        // Instanz zurückgeben
        return this;
    }

}
