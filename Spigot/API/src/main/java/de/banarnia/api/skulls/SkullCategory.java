package de.banarnia.api.skulls;

import de.banarnia.api.messages.Message;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.smartInventory.content.provider.Closable;
import de.banarnia.api.smartInventory.content.provider.FilledInventoryProvider;
import org.bukkit.entity.Player;

/* SkullCategorie Klasse
 * Enthält die Daten einer Kategorie für Skulls.
 */
public class SkullCategory implements FilledInventoryProvider, Closable {

    // Name der Kategorie
    private final String name;

    // Anzeigename der Kategorie
    private String displayName;

    // Icon der Kategorie - Als String, damit auch Skull:<URL> verwendet werden kann
    private String icon;

    // Abfrage, ob man eine Permission braucht, um diese Kategorie anzuzeigen
    private boolean requiresPermission;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Standard Konstruktor
    public SkullCategory(String name, String displayName, String icon, boolean requiresPermission) {
        this.name               = name;
        this.displayName        = displayName;
        this.icon               = icon;
        this.requiresPermission = requiresPermission;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Benötigte Permission als String wiedergeben
    public String getPermission() {
        return "skulls.category." + name;
    }

    // Abfrage, ob ein Spieler die Kategorie sehen kann
    public boolean canView(Player player) {
        return !requiresPermission || player.hasPermission(getPermission()) || player.hasPermission("skulls.admin");
    }

    // Kategorie speichern
    public void save() {
        SkullManager.getInstance().saveCategory(this);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ GUI ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Kategorie bearbeiten
    public void edit(Player player) {
        // Null check
        if (player == null)
            return;

        // Permission Abfrage
        if (!player.hasPermission("skulls.admin"))
            return;

        // GUI öffnen
        SmartInventory.builder().provider(this).size(4).title(Message.SKULL_GUI_ADMIN_CATEGORY_EDIT_HEADER.replace("%skull_category%", displayName)).build().open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        // Anzeigenamen ändern
        contents.set(1,2, getDisplayNameItem(player, contents, displayName, input -> setDisplayName(input)));

        // Icon ändern
        contents.set(1,4, getIconItem(player, contents, icon, input -> setIcon(input)));

        // Abfrage, ob Permission benötigt wird
        contents.set(1,6, getToggleItem(player, contents,
                Message.SKULL_GUI_ADMIN_CATEGORY_REQUIRES_PERMISSION.get(), requiresPermission,
                click -> setRequiresPermission(!requiresPermission)));

        // Todo Kategorie Übersicht öffnen
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public boolean requiresPermission() {
        return requiresPermission;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;

        // Kategorie speichern
        save();
    }

    public void setIcon(String icon) {
        this.icon = icon;

        // Kategorie speichern
        save();
    }

    public void setRequiresPermission(boolean requiresPermission) {
        this.requiresPermission = requiresPermission;

        // Kategorie speichern
        save();
    }
}
