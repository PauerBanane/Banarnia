package de.banarnia.api.smartInventory.content.provider;

import de.banarnia.api.smartInventory.content.InventoryContents;
import org.bukkit.entity.Player;

public interface Closable {

    default void insertCloseItem(Player player, InventoryContents contents) {
        contents.setCloseItem(contents.getLastRow(), contents.getCenterColumn(), player);
    }

}
