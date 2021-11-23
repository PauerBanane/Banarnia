package de.banarnia.api.messages;

/* Message Enumeration
 * Beinhaltet alle Standard-Nachrichten.
 */
public enum Message implements IMessage {

    INFO_ADDON_NOT_LOADED("Das Addon %addon% wurde nicht geladen."),
    INFO_ADDON_ENABLED("Das Addon %addon% wurde aktiviert."),
    INFO_ADDON_DISABLED("Das Addon %addon% wurde deaktiviert."),
    INFO_ADDON_RELOADED("Das Addon %addon% wurde neu geladen."),
    ERROR_COMMAND_INVALID_DAY("Dieser Wochentag existiert nicht."),
    ERROR_COMMAND_INVALID_MONTH("Dieser Monat existiert nicht."),
    ERROR_COMMAND_INVALID_YEAR("Dieses Jahr existiert nicht."),
    ERROR_COMMAND_INVALID_GAMEMODE("Dieser Spielmodus existiert nicht."),
    ERROR_COMMAND_INVALID_ENTITYTYPE("Diese Art von Entities existiert nicht."),
    ERROR_COMMAND_INVALID_PROTECTED_REGION("Diese Region existiert in dieser Welt nicht."),
    ERROR_COMMAND_INVALID_SOUND("Dieser Sound existiert nicht."),
    ERROR_COMMAND_INVALID_MATERIAL("Dieses Material existiert nicht."),
    ERROR_COMMAND_INVALID_BANARNIA_PLUGIN("Dieses Plugin ist nicht geladen."),
    ERROR_ADDON_ALREADY_REGISTERED("Das Addon %addon% wurde bereits registriert."),
    ERROR_ADDON_DEPENDENCY_NOT_FULFILLED("Das Addon %addon% konnte nicht geladen werden, da nicht alle" +
                                        " Dependencies erfüllt wurden."),
    ERROR_MESSAGEHANDLER_NOT_REGISTERED("Die Message-Enumeration %enum% wurde noch nicht registriert."),
    ERROR_MESSAGEHANDLER_ALREADY_REGISTERED("Die Message-Enumeration %enum% wurde bereits registriert.");

    private String defaultMessage;
    private String message;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    Message(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        this.message        = defaultMessage;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Gibt den Key in der Config wieder
    @Override
    public String getKey() {
        return String.valueOf(this).replace("_", ".");
    }

    // Gibt die Standard-Nachricht wieder
    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }

    // Gibt die aktuelle Nachricht wieder, insofern sie nicht null ist
    @Override
    public String get() {
        // Nachricht abrufen
        String message = this.message != null ? this.message : defaultMessage;

        // Placeholder einsetzen
        return message.replace("%enum%", this.getClass().toString());
    }

    // Setzt die aktuelle Nachricht
    @Override
    public void set(String message) {
        this.message = message;
    }
}
