package de.banarnia.api.skulls.gui;

import de.banarnia.api.messages.Message;
import de.banarnia.api.skulls.Skull;
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

public class SkullCategoryOverviewGUI implements FilledInventoryProvider, Closable {

    // Static öffnen
    public static void open(Player player, SkullCategory category) {
        // Null check
        if (category == null || !SkullManager.getInstance().isCategoryRegistered(category))
            return;

        // GUI öffnen
        SmartInventory.builder().provider(new SkullCategoryOverviewGUI(category))
                .size(5)
                .title(Message.SKULL_GUI_CATEGORY_OVERVIEW_HEADER.replace("%skull_category%", category.getDisplayName()))
                .build()
                .open(player);
    }

    // Instanz des SkullManagers
    private SkullManager manager;

    // Instanz der Kategorie
    private SkullCategory category;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private SkullCategoryOverviewGUI(SkullCategory category) {
        this.manager    = SkullManager.getInstance();
        this.category   = category;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ GUI ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void init(Player player, InventoryContents contents) {
        // Sichtbare Skulls bekommen
        List<Skull> visibleSkulls = manager.getSkulls(player, category);

        // Zurück Button setzen
        contents.setBackItem(4,3,player, click -> SkullOverviewGUI.open(player));

        // Pagination einstellen
        contents.pagination(21);


    }

    // Skull Button bekommen
    private ClickableItem getSkullItem(Player player, InventoryContents contents, Skull skull) {
        // Abfrage, ob der Spieler Skull-Admin Rechte hat
        boolean adminPermission = player.hasPermission("skulls.admin");

        // Icon
        ItemBuilder builder = ItemBuilder.of(manager.getSkullItem(skull));
        builder.name(skull.getDisplayName());
        builder.lore(adminPermission ? Message.SKULL_GUI_SKULL_ADMIN_LORE.replace("%price%", String.valueOf(skull.getPrice())) :
                                       Message.SKULL_GUI_SKULL_LORE.replace("%price%", String.valueOf(skull.getPrice())));

        // Item zurückgeben
        return ClickableItem.of(builder.build(), click -> {
            // Skull bearbeiten, wenn Rechtsklick und Skull-Admin
            if (click.isRightClick() && adminPermission) {
                skull.edit(player);
                return;
            }

            // Skull kaufen öffnen
            // TODO Kaufoption
            Message.NOT_IMPLEMENTED.sendAsError(player, "Skulls");
        });
    }

}
