package de.banarnia.api.smartInventory.content.provider;

import de.banarnia.api.config.ApiConfig;
import de.banarnia.api.messages.Message;
import de.banarnia.api.skulls.Skulls;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.buttons.InputButton;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.util.F;
import de.banarnia.api.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public interface InventoryProvider {

    default ClickableItem getNameItem(Player player, InventoryContents contents, String currentName, Consumer<String> consumer) {
        return getNameItem(player, contents, currentName, consumer, true);
    }

    default ClickableItem getNameItem(Player player, InventoryContents contents, String currentName, Consumer<String> consumer, boolean reopenOnCancle) {
        // Icon
        ItemBuilder builder = ItemBuilder.of(Skulls.ID.get());
        builder.name(Message.BUTTON_NAME_CHANGE_ICON_NAME.get());
        builder.lore(Message.BUTTON_NAME_CHANGE_LORE.replace("%name%", currentName));

        // Item zurückgeben
        return new InputButton<String>(player, builder.build(), String.class, (name, canceled) -> {
            // Abfrage, ob die Eingabe abgebrochen wurde
            if (canceled || name == null) {
                // Abfrage, ob nach Abrruch neu geöffnet werden soll
                if (reopenOnCancle)
                    reOpen(player, contents);

                return;
            }

            // Consumer akzeptieren, wenn alles geklappt hat
            consumer.accept(name);

            // Neu öffnen
            reOpen(player, contents);
        }, Message.BUTTON_NAME_CHANGE_INPUT.get(), Message.BUTTON_NAME_CHANGE_INPUT_TITLE.get());
    }

    default ClickableItem getDisplayNameItem(Player player, InventoryContents contents, String currentName, Consumer<String> consumer) {
        return getDisplayNameItem(player, contents, currentName, consumer, true);
    }

    default ClickableItem getDisplayNameItem(Player player, InventoryContents contents, String currentName, Consumer<String> consumer, boolean reopenOnCancle) {
        // Icon
        ItemBuilder builder = ItemBuilder.of(Material.NAME_TAG);
        builder.name(Message.BUTTON_DISPLAYNAME_CHANGE_ICON_NAME.get());
        builder.lore(Message.BUTTON_DISPLAYNAME_CHANGE_LORE.replace("%name%", currentName));

        // Item zurückgeben
        return new InputButton<String>(player, builder.build(), String.class, (name, canceled) -> {
            // Abfrage, ob die Eingabe abgebrochen wurde
            if (canceled || name == null) {
                // Abfrage, ob nach Abrruch neu geöffnet werden soll
                if (reopenOnCancle)
                    reOpen(player, contents);

                return;
            }

            // Consumer akzeptieren, wenn alles geklappt hat
            consumer.accept(name);

            // Neu öffnen
            reOpen(player, contents);
        }, Message.BUTTON_DISPLAYNAME_CHANGE_INPUT.get(), Message.BUTTON_DISPLAYNAME_CHANGE_INPUT_TITLE.get());
    }

    default ClickableItem getIconItem(Player player, InventoryContents contents, String currentIcon, Consumer<String> consumer) {
        return getIconItem(player, contents, currentIcon, consumer, true);
    }

    default ClickableItem getIconItem(Player player, InventoryContents contents, String currentIcon, Consumer<String> consumer, boolean reopenOnCancle) {
        // Icon
        ItemBuilder builder = ItemBuilder.of(currentIcon, Material.AIR);
        builder.name(Message.BUTTON_ICON_CHANGE_ICON_NAME.get());
        builder.lore(Message.BUTTON_ICON_CHANGE_LORE.replace("%icon%", currentIcon));

        // Item zurückgeben
        return new InputButton<String>(player, builder.build(), String.class, (input,canceled) -> {
            // Abfrage, ob es ein gültiges Item sein kann
            try {
                ItemBuilder.of(input);
            }
            catch (Exception ex) {
                canceled = true;
            }

            // Abfrage, ob die Eingabe abgebrochen wurde
            if (canceled || input == null) {
                // Abfrage, ob nach Abrruch neu geöffnet werden soll
                if (reopenOnCancle)
                    reOpen(player, contents);

                return;
            }

            // Consumer akzeptieren, wenn alles geklappt hat
            consumer.accept(input);

            // Neu öffnen
            reOpen(player, contents);
        }, Message.BUTTON_ICON_CHANGE_INPUT.get(), Message.BUTTON_ICON_CHANGE_INPUT_TITLE.get());
    }

    default ClickableItem getToggleItem(Player player, InventoryContents contents, String name, boolean tf, Consumer<InventoryClickEvent> consumer) {
        // Icon
        ItemBuilder builder = ItemBuilder.of(tf ? ApiConfig.GUI_TOGGLE_ICON_TRUE() : ApiConfig.GUI_TOGGLE_ICON_FALSE());
        builder.name(name);
        builder.lore(Message.BUTTON_BOOL_TOGGLE_LORE.replace("%bool%", F.checkOrCross(tf)));

        // Item zurückgeben
        return ClickableItem.of(builder.build(), click -> {
            consumer.accept(click);

            // Neu öffnen
            reOpen(player, contents);
        });
    }

    default void preInit(Player player, InventoryContents contents) {
    }

    void init(Player player, InventoryContents contents);

    default void update(Player player, InventoryContents contents) {
        return;
    }

    default void reOpen(Player player, InventoryContents contents) {
        InventoryView playerInventory = player.getOpenInventory();
        ItemStack cursor = playerInventory != null ? playerInventory.getCursor().clone() : null;
        playerInventory.setCursor(null );
        if (contents.properties().isEmpty()) {
            contents.inventory().open(player, contents.pagination().getPage());
        } else {
            Map<String, Object> map = contents.properties();
            contents.inventory().open(player, contents.pagination().getPage(), map.keySet().toArray(new String[map.keySet().size()]),
                    map.values().toArray(new Object[map.values().size()]));
        }
        playerInventory = player.getOpenInventory();
        if (playerInventory != null)
            playerInventory.setCursor(cursor);
    }

    default void onClose(Player player, InventoryContents contents) {
        return;
    }

    default ClickableItem getGrayStainedGlass() {
        return ClickableItem.empty(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
    }
    default ClickableItem getLightGrayStainedGlass() {
        return ClickableItem.empty(ItemBuilder.of(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ").build());
    }
    default ItemStack getFiller() {
        return ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).name(" ").build();
    }
    default ClickableItem getCloseItem(Player player) {
        ItemBuilder builder = ItemBuilder.of(Material.BARRIER).name("§cMenü schließen");
        return ClickableItem.of(builder.build(), click -> player.closeInventory());
    }
}
