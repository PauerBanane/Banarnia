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
    private HashMap<Class<? extends IMessage>, FileLoader> enumMap;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private MessageHandler(BanarniaAPI plugin) {
        // Null check
        if (plugin == null)
            throw new IllegalArgumentException();

        // Map Instanzieren
        enumMap = Maps.newHashMap();

        // Instanz setzen
        instance = this;

        // Default-File setzen
        defaultFile = FileLoader.of(plugin.getDataFolder(), "Messages.yml");
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden zur Registrierung ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Abfrage, ob eine Enumeration schon registriert wurde
    public boolean isRegistered(Class<? extends IMessage> enumClass) {
        return enumMap != null ? enumMap.containsKey(enumClass) : false;
    }

    // Registriert eine Message-Enumeration
    public void register(Class<? extends IMessage> enumeration) {
        register(enumeration, defaultFile);
    }

    // Registriert eine Message-Enumeration mit einem dazugehörigen FileLoader
    public void register(Class<? extends IMessage> enumClass, FileLoader messageFile) {
        // Null checks
        if (enumClass == null || messageFile == null || enumMap == null || !enumClass.isEnum())
            throw new IllegalArgumentException();

        // Abfrage, ob die Enum schon registriert wurde
        if (isRegistered(enumClass))
            throw new IllegalArgumentException(Message.ERROR_MESSAGEHANDLER_ALREADY_REGISTERED.get());

        // Enum in die Map eintragen
        enumMap.put(enumClass, messageFile);

        // Enum laden
        load(enumClass);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden zum Laden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Nachrichten aus der Config laden
    public void load(Class<? extends IMessage> enumClass) {
        // Null check
        if (enumClass == null)
            throw new IllegalArgumentException();

        // Abfrage, ob die Enumeration registriert wurde
        if (!isRegistered(enumClass))
            throw new IllegalStateException(Message.ERROR_MESSAGEHANDLER_NOT_REGISTERED.get());

        // FileLoader erhalten
        FileLoader fileLoader = enumMap.get(enumClass);

        // Datei neu laden
        fileLoader.reload();

        // Werte auslesen
        Arrays.stream(enumClass.getEnumConstants()).forEach(enumValue -> {
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

    // Alle Nachrichten neu laden
    public void reload() {
        enumMap.keySet().forEach(enumeration -> load(enumeration));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static MessageHandler getInstance() {
        if (instance == null)
            new MessageHandler(BanarniaAPI.getInstance());

        return instance;
    }

    public static FileLoader getDefaultFile() {
        return defaultFile;
    }
}
