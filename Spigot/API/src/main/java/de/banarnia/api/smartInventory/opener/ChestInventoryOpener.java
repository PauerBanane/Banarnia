package de.banarnia.api.smartInventory.opener;

import com.google.common.base.Preconditions;
import de.banarnia.api.smartInventory.InventoryManager;
import de.banarnia.api.smartInventory.SmartInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ChestInventoryOpener implements InventoryOpener {

    @Override
    public Inventory open(SmartInventory inv, Player player) {
        Preconditions.checkArgument(inv.getColumns() == 9, "The column count for the chest inventory must be 9, found: %s.", inv.getColumns());
        Preconditions.checkArgument(inv.getRows() >= 1 && inv.getRows() <= 6, "The row count for the chest inventory must be between 1 and 6, found: %s", inv.getRows());

        InventoryManager manager = InventoryManager.get();
        Inventory handle = manager.getContents(player).get().getInventory();

        fill(handle, manager.getContents(player).get());

        player.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return type == InventoryType.CHEST || type == InventoryType.ENDER_CHEST;
    }

}