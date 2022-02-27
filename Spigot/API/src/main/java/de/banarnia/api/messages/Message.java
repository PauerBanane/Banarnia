package de.banarnia.api.messages;

import java.util.List;

/* Message Enumeration
 * Beinhaltet alle Standard-Nachrichten.
 */
public enum Message implements IMessage {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Standard-Nachrichten ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Für die Klasse 'F'
    CHECK("§2✔"),
    CROSS("§4✖"),

    CHAT_PREFIX("§6  %topic% §8» §7"),
    CHAT_PREFIX_ERROR("§c  %topic% §8» §7"),

    NO_PERMISSION("Dazu hast du keine Rechte!"),
    NOT_IMPLEMENTED("Diese Funktion ist noch nicht freigeschaltet!"),

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Commands ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Standard Context
    ERROR_COMMAND_INVALID_DAY("Dieser Wochentag existiert nicht."),
    ERROR_COMMAND_INVALID_MONTH("Dieser Monat existiert nicht."),
    ERROR_COMMAND_INVALID_YEAR("Dieses Jahr existiert nicht."),
    ERROR_COMMAND_INVALID_GAMEMODE("Dieser Spielmodus existiert nicht."),
    ERROR_COMMAND_INVALID_ENTITYTYPE("Diese Art von Entities existiert nicht."),
    ERROR_COMMAND_INVALID_PROTECTED_REGION("Diese Region existiert in dieser Welt nicht."),
    ERROR_COMMAND_INVALID_SOUND("Dieser Sound existiert nicht."),
    ERROR_COMMAND_INVALID_MATERIAL("Dieses Material existiert nicht."),
    ERROR_COMMAND_INVALID_BANARNIA_PLUGIN("Dieses Plugin ist nicht geladen."),

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Smart Inventory ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Standard
    GUI_PAGE_NEXT("§8§lSeite vor"),
    GUI_PAGE_PREVIOUS("§8§lSeite zurück"),
    GUI_CLOSE("§cSchließen"),
    GUI_BACK("§eZurück"),

    // Anzeigenamen ändern
    BUTTON_DISPLAYNAME_CHANGE_INPUT("§eGib den neuen Anzeigenamen ein, oder §7'§cAbbrechen§7' §eum zurückzukehren:"),
    BUTTON_DISPLAYNAME_CHANGE_INPUT_TITLE("Anzeigenamen eingeben"),
    BUTTON_DISPLAYNAME_CHANGE_ICON_NAME("§aAnzeigenamen ändern"),
    BUTTON_DISPLAYNAME_CHANGE_LORE("§7Zurzeit: §e" + "%name%\n" +
            "\n" +
            "§7Dies ändert den sichtbaren Namen." +
            "\n" +
            "§eKlicken, zum Bearbeiten"),

    // Namen ändern
    BUTTON_NAME_CHANGE_INPUT("§eGib den neuen Namen ein, oder §7'§cAbbrechen§7' §eum zurückzukehren:"),
    BUTTON_NAME_CHANGE_INPUT_TITLE("Namen eingeben"),
    BUTTON_NAME_CHANGE_ICON_NAME("§aNamen ändern"),
    BUTTON_NAME_CHANGE_LORE("§7Zurzeit: §e" + "%name%\n" +
            "\n" +
            "§7Dies ändert nur den Namen im Plugin,\n" +
            "§cnicht §7den Anzeigenamen.\n" +
            "\n" +
            "§eKlicken, zum Bearbeiten"),

    // Icon ändern
    BUTTON_ICON_CHANGE_INPUT("§eGib das neue Icon ein, oder §7'§cAbbrechen§7' §eum zurückzukehren:"),
    BUTTON_ICON_CHANGE_INPUT_TITLE("Icon eingeben"),
    BUTTON_ICON_CHANGE_ICON_NAME("§aIcon ändern"),
    BUTTON_ICON_CHANGE_LORE("§7Zurzeit: §e" + "%icon%\n" +
            "\n" +
            "§eKlicken, zum Bearbeiten"),

    // Boolean togglen
    BUTTON_BOOL_TOGGLE_LORE("§7Zurzeit: §e" + "%bool%\n" +
            "\n" +
            "§eKlicken, zum Ändern"),

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Chat Input ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    CHAT_INPUT_DEFAULT_SUBTITLE("&eSchau in den Chat"),

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Addon ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    INFO_ADDON_NOT_LOADED("Das Addon %addon% wurde nicht geladen."),
    INFO_ADDON_ENABLED("Das Addon %addon% wurde aktiviert."),
    INFO_ADDON_DISABLED("Das Addon %addon% wurde deaktiviert."),
    INFO_ADDON_RELOADED("Das Addon %addon% wurde neu geladen."),

    // Error
    ERROR_ADDON_ALREADY_REGISTERED("Das Addon %addon% wurde bereits registriert."),
    ERROR_ADDON_DEPENDENCY_NOT_FULFILLED("Das Addon %addon% konnte nicht geladen werden, da nicht alle" +
            " Dependencies erfüllt wurden."),

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Skulls ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Header in der Übersicht
    SKULL_GUI_OVERVIEW_HEADER("Köpfe"),
    SKULL_GUI_CATEGORY_OVERVIEW_HEADER("%skull_category% §rKöpfe"),

    // Skull Ansicht
    SKULL_GUI_SKULL_LORE("§7Preis: §e%price% %money_currency_plural%\n" +
            "\n" +
            "§eKlicken, zum Kaufen"),
    SKULL_GUI_SKULL_ADMIN_LORE("§7Preis: §e%price% %money_currency_plural%\n" +
            "\n" +
            "§eKlicken, zum Kaufen\n" +
            "§cRechtsklick, zum Bearbeiten"),

    // Skull bearbeiten
    SKULL_GUI_ADMIN_EDIT_HEADER("%skull% §rbearbeiten"),

    // Skull-Kategorie bearbeiten
    SKULL_GUI_ADMIN_CATEGORY_EDIT_HEADER("%skull_category% §rbearbeiten"),

    // Übersicht
    SKULL_GUI_CATEGORY_LORE("§7Anzahl: §e%skull_amount%\n" +
            "\n" +
            "§eKlicken, zum Anzeigen"),
    SKULL_GUI_CATEGORY_ADMIN_LORE("§7Anzahl: §e%skull_amount%\n" +
            "\n" +
            "§eKlicken, zum Anzeigen\n" +
            "§eRechtsklick, zum Bearbeiten"),

    // URL
    SKULL_GUI_ADMIN_CHANGE_URL("§aURL ändern"),
    SKULL_GUI_ADMIN_CHANGE_URL_LORE("§7Zurzeit: §e%url%\n" +
            "\n" +
            "§eKlicken, zum Bearbeiten"),

    SKULL_GUI_ADMIN_CHANGE_URL_INPUT_TITLE("URL eingeben"),
    SKULL_GUI_ADMIN_CHANGE_URL_INPUT_MESSAGE("§eGib die neue URL ein, oder §7'§cAbbrechen§7' §eum zurückzukehren:"),

    // Toggle Item Namen
    SKULL_GUI_ADMIN_CATEGORY_REQUIRES_PERMISSION("§aPermission benötigt"),
    SKULL_GUI_ADMIN_FOR_SALE("§aKaufbar"),

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Übersetzung ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    LANGUAGE_CONSOLE_SENDER_NAME("Konsole"),

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Fehlermeldungen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    ERROR_GENERAL("Es ist ein Fehler aufgetreten. Bitte melden diesen einem Admin."),
    ERROR_CONFIG_FILE_IS_NULL("Fehler beim Laden einer Config: File ist null"),
    ERROR_MESSAGEHANDLER_NOT_REGISTERED("Die Message-Enumeration %enum% wurde noch nicht registriert."),
    ERROR_MESSAGEHANDLER_ALREADY_REGISTERED("Die Message-Enumeration %enum% wurde bereits registriert.");

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Registrieren
    static {
        MessageHandler.getInstance().register(Message.class);
    }

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
        return String.valueOf(this);
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
