package de.banarnia.api.skulls.gui;

import de.banarnia.api.messages.Message;
import de.banarnia.api.skulls.SkullCategory;
import de.banarnia.api.skulls.SkullManager;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.smartInventory.content.provider.Closable;
import de.banarnia.api.smartInventory.content.provider.FilledInventoryProvider;
import de.banarnia.api.util.ItemBuilder;
import org.bukkit.entity.Player;

import java.util.List;

public class SkullOverviewGUI implements FilledInventoryProvider, Closable {

    // Static öffnen
    public static void open(Player player) {
        SmartInventory.builder().provider(new SkullOverviewGUI()).size(5).title(Message.SKULL_GUI_OVERVIEW_HEADER.get()).build().open(player);
    }

    // Instanz des SkullManagers
    private SkullManager manager;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private SkullOverviewGUI() {
        this.manager = SkullManager.getInstance();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ GUI ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void init(Player player, InventoryContents contents) {
        // Kategorien bekommen
        List<SkullCategory> visibleCategories = manager.getCategories(player);

        // Pagination einrichten
        contents.pagination(21);

        // Schleife durch alle sichtbaren Kategorien
        for (SkullCategory category : visibleCategories)
            contents.pagination().addItem(getCategoryItem(player, contents, category));
    }

    // Item für Kategorie bekommen
    private ClickableItem getCategoryItem(Player player, InventoryContents contents, SkullCategory category) {
        // Anzahl der sichtbaren Skulls der Kategorie
        int amount = manager.getSkulls(player, category).size();

        // Abfrage, ob der Spieler Skull-Admin Rechte hat
        boolean adminPermission = player.hasPermission("skulls.admin");

        // Icon
        ItemBuilder builder = ItemBuilder.of(category.getIcon());
        builder.name(category.getDisplayName());
        builder.lore(adminPermission ? Message.SKULL_GUI_CATEGORY_ADMIN_LORE.replace("%skull_amount%", String.valueOf(amount)) :
                                       Message.SKULL_GUI_CATEGORY_LORE.replace("%skull_amount%", String.valueOf(amount)));

        // Item zurückgeben
        return ClickableItem.of(builder.build(), click -> {
            // Kategorie bearbeiten, wenn Rechtsklick und Skull-Admin
            if (click.isRightClick() && adminPermission) {
                category.edit(player);
                return;
            }

            // Kategorie öffnen
            SkullCategoryOverviewGUI.open(player, category);
        });
    }

}
