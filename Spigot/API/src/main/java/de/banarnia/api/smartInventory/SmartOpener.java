package de.banarnia.api.smartInventory;

import com.google.common.collect.Maps;
import de.banarnia.api.smartInventory.content.InventoryContentImpl;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.smartInventory.opener.InventoryOpener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.Optional;

public class SmartOpener {

    private final SmartInventory inv;
    private final Player player;

    private final Map<String, Object> properties;

    private int page = 0;

    public SmartOpener(Player player, SmartInventory inventory) {
        this.inv = inventory;
        this.player = player;
        this.properties = Maps.newHashMap();
    }

    public SmartOpener page(int page) {
        this.page = page;
        return this;
    }

    public SmartOpener data(String key, Object object) {
        this.properties.put(key, object);
        return this;
    }


    @SuppressWarnings("unchecked")
    public Inventory open() {
        InventoryManager manager = InventoryManager.getInstance();
        Optional<SmartInventory> oldInv = manager.getInventory(player);

        oldInv.ifPresent(inv -> {
            inv.getListeners().stream().filter(listener -> listener.getType() == InventoryCloseEvent.class).forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener).accept(new InventoryCloseEvent(player.getOpenInventory())));
            manager.setInventory(player, null);
        });

        InventoryContents contents = new InventoryContentImpl(this.inv, player);
        contents.pagination().page(page);

        manager.setContents(player, contents);
        InventoryContents c = manager.getContents(player).get();
        this.properties.forEach( (key, val) -> c.setProperty(key, val));

        this.inv.getProvider().init(player, contents);


        InventoryOpener opener = manager.findOpener(this.inv.getType()).orElseThrow(() -> new IllegalStateException("No opener found for the inventory type " + this.inv.getType().name()));
        Inventory handle = opener.open(this.inv, player);
        manager.setInventory(player, this.inv);
        return handle;
    }
}
