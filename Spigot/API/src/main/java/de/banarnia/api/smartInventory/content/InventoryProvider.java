package de.banarnia.api.smartInventory.content;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface InventoryProvider {

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
}
