package de.banarnia.api.skulls;

import de.banarnia.api.messages.Message;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.buttons.InputButton;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.smartInventory.content.provider.FilledInventoryProvider;
import de.banarnia.api.util.F;
import de.banarnia.api.util.ItemBuilder;
import de.banarnia.api.util.UtilEnum;
import org.bukkit.entity.Player;

/* Skull Klasse.
 * Enthält alle Daten für einen Custom-Skull.
 */
public class Skull implements FilledInventoryProvider {

    // Name des Skulls - nicht der Anzeigename
    private final String name;

    // Anzeigename des Skulls
    private String displayName;

    // URL des Skulls
    private String url;

    // Kategorie des Skulls
    private String category;

    // Abfrage, ob der Skull kaufbar ist
    private boolean forSale;

    // Preis für den Skull
    private double price = Double.MAX_VALUE;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Standard Konstruktor
    public Skull(String name, String displayName, String url, String category, boolean forSale, double price) {
        this.name           = name;
        this.displayName    = displayName;
        this.url            = url;
        this.category       = category;
        this.forSale        = forSale;
        this.price          = price;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Abfrage, ob der Skull ein Standard-Skull aus dem 'Skulls.class' enum ist - kann dann nicht gelöscht werden
    public boolean isDefaultSkull() {
        return UtilEnum.contains(Skulls.values(), name);
    }

    // Abfrage, ob der Spieler den Skull im GUI sehen kann - Skull muss kaufbar sein
    public boolean canBuy(Player player) {
        // Null check
        if (player == null)
            return false;

        return isForSale() || player.hasPermission("skulls.admin");
    }

    // Löschen
    public boolean delete() {
        // Abfrage, ob es ein Default-Skull ist
        if (isDefaultSkull())
            return false;

        // Löschen
        return SkullManager.getInstance().delete(this);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ GUI - Bearbeitung ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // GUI öffnen
    public void edit(Player player) {
        // Permission Abfrage
        if (!player.hasPermission("skulls.admin"))
            return;

        // GUI öffnen
        SmartInventory.builder().provider(this).title(Message.SKULL_GUI_ADMIN_EDIT_HEADER.replace("%skull%", displayName)).size(3).build().open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        // Anzeigenamen ändern
        contents.set(1,1, getDisplayNameItem(player, contents, displayName, input -> setDisplayName(input)));

        // URL ändern
        contents.set(1,2, getUrlItem(player, contents));
    }

    // Item zum Ändern der URL des Skulls
    private ClickableItem getUrlItem(Player player, InventoryContents contents) {
        // Icon erstellen
        ItemBuilder builder = ItemBuilder.of(SkullManager.getInstance().getSkullItem(url));
        builder.name(Message.SKULL_GUI_ADMIN_CHANGE_URL.get());
        builder.lore(Message.SKULL_GUI_ADMIN_CHANGE_URL_LORE.replace("%url%", F.crossOrElse(url != null, url)));

        // Button zurückgeben
        return new InputButton<String>(player, builder.build(), String.class, (input, canceled) -> {
            // Abfrage, ob abgebrochen wurde
            if (canceled) {
                reOpen(player, contents);
                return;
            }

            // URL ändern
            setUrl(input);

            // Neu öffnen
            reOpen(player, contents);
        }, Message.SKULL_GUI_ADMIN_CHANGE_URL_INPUT_MESSAGE.get(), Message.SKULL_GUI_ADMIN_CHANGE_URL_INPUT_TITLE.get());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        // Abfrage, ob URL richtig sein kann
        if (url != null && !url.isEmpty())
            return url;

        // Wenn Skull ein Default-Skull aus der 'Skulls.class' enum ist, wird die Standard-URL wiedergegeben
        if (isDefaultSkull()) {
            // Enum aufrufen
            Skulls skullEnum = UtilEnum.get(Skulls.values(), name);

            // Standard-URL wiedergeben, wenn nicht null
            if (skullEnum != null)
                return skullEnum.getDefaultUrl();
        }

        // Null zurückgeben, wenn alles fehlschlägt
        return null;
    }

    public double getPrice() {
        return price;
    }

    public boolean isForSale() {
        return forSale;
    }

    public String getCategoryName() {
        return category;
    }

    public SkullCategory getCategory() {
        return SkullManager.getInstance().getCategory(getCategoryName(), true);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        save();
    }

    public void setCategory(String category) {
        this.category = category;
        save();
    }

    public void setForSale(boolean forSale) {
        this.forSale = forSale;
        save();
    }

    public void setPrice(double price) {
        this.price = price;
        save();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void save() {
        SkullManager.getInstance().saveSkull(this);
    }
}
