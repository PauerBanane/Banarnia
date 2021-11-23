package de.banarnia.api.messages;

import com.google.common.collect.Maps;
import de.banarnia.api.BanarniaAPI;
import de.banarnia.api.util.FileLoader;
import org.apache.commons.lang.Validate;

import java.util.Arrays;
import java.util.HashMap;

/* Message-Handler Klasse
 * Regelt alle Nachrichten, welche in Dateien geändert werden können.
 */
public class MessageHandler {

    // Static Instanzen
    private static MessageHandler instance;
    private static FileLoader defaultFile;

    // Zuordnung von Enum zu FileLoader
    private HashMap<Enum<? extends IMessage>, FileLoader> enumMap;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private MessageHandler(BanarniaAPI plugin) {
        // Null check
        Validate.notNull(plugin);

        // Map Instanzieren
        enumMap = Maps.newHashMap();

        // Instanz setzen
        instance = this;

        // Default-File setzen
        defaultFile = FileLoader.of(plugin.getDataFolder(), "Messages.yml");
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden zur Registrierung ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Abfrage, ob eine Enumeration schon registriert wurde
    public boolean isRegistered(Enum<? extends IMessage> enumeration) {
        return enumMap != null ? enumMap.containsKey(enumeration) : false;
    }

    // Registriert eine Message-Enumeration in dem Standard-FileLoader
    public void register(Enum<? extends IMessage> enumeration) {
        register(enumeration, defaultFile);
    }

    // Registriert eine Message-Enumeration mit einem dazugehörigen FileLoader
    public void register(Enum<? extends IMessage> enumeration, FileLoader messageFile) {
        // Null checks
        Validate.notNull(enumeration);
        Validate.notNull(messageFile);
        Validate.notNull(enumMap);

        // Abfrage, ob die Enum schon registriert wurde
        if (isRegistered(enumeration))
            throw new IllegalArgumentException(Message.ERROR_MESSAGEHANDLER_ALREADY_REGISTERED.get());

        // Enum in die Map eintragen
        enumMap.put(enumeration, messageFile);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden zum Laden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Nachrichten aus der Config laden
    public void load(Enum<? extends IMessage> enumeration) {
        // Null check
        Validate.notNull(enumeration);

        // Abfrage, ob die Enumeration registriert wurde
        if (!isRegistered(enumeration))
            throw new IllegalStateException(Message.ERROR_MESSAGEHANDLER_NOT_REGISTERED.get());

        // FileLoader erhalten
        FileLoader fileLoader = enumMap.get(enumeration);

        // Datei neu laden
        fileLoader.reload();

        // Werte auslesen
        Arrays.stream(enumeration.getDeclaringClass().getEnumConstants()).forEach(enumValue -> {
            // Key zu der Nachricht
            String key = enumValue.getKey();

            // Standard-Nachricht des Enums
            String defaultMessage = enumValue.getDefaultMessage();

            // Nachricht aus der Config, ist defaultMessage, wenn sie nicht in der Config existiert
            String message = fileLoader.getOrElseSet(key, defaultMessage);

            // Nachricht im Enum setzen
            enumValue.set(message);
        });
    }

    // Alle Nachtichten neu laden
    public void reload() {
        enumMap.keySet().forEach(enumeration -> load(enumeration));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static MessageHandler getInstance() {
        if (instance != null)
            new MessageHandler(BanarniaAPI.getInstance());

        return instance;
    }

    public static FileLoader getDefaultFile() {
        return defaultFile;
    }
}
