package de.banarnia.api.smartInventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.banarnia.api.BanarniaAPI;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.smartInventory.opener.ChestInventoryOpener;
import de.banarnia.api.smartInventory.opener.InventoryOpener;
import de.banarnia.api.smartInventory.opener.SpecialInventoryOpener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class InventoryManager {

    private static InventoryManager manager;

    private Map<Player, SmartInventory> inventories;

    private Map<Player, InventoryContents> contents;

    private List<InventoryOpener> defaultOpeners;

    private List<InventoryOpener> openers;

    public InventoryManager() {
        if (InventoryManager.manager != null) return;
        InventoryManager.manager = this;
        this.inventories = Maps.newHashMap();
        this.contents = Maps.newHashMap();
        this.defaultOpeners = Arrays.asList(new ChestInventoryOpener(), new SpecialInventoryOpener());
        this.openers = Lists.newArrayList();
        Bukkit.getPluginManager().registerEvents(new SmartInvListener(this), BanarniaAPI.getInstance());
        new InvTask().runTaskTimer(BanarniaAPI.getInstance(), 1, 1);
    }

    public static InventoryManager get() {
        return InventoryManager.manager;
    }


    public Optional<InventoryOpener> findOpener(InventoryType type) {
        Optional<InventoryOpener> opInv = this.openers.stream()
                .filter(opener -> opener.supports(type))
                .findAny();

        if(!opInv.isPresent()) {
            opInv = this.defaultOpeners.stream()
                    .filter(opener -> opener.supports(type))
                    .findAny();
        }

        return opInv;
    }

    public void registerOpeners(InventoryOpener... openers) {
        this.openers.addAll(Arrays.asList(openers));
    }

    public List<Player> getOpenedPlayers(SmartInventory inv) {
        List<Player> list = new ArrayList<>();

        this.inventories.forEach((player, playerInv) -> {
            if(inv.equals(playerInv))
                list.add(player);
        });

        return list;
    }

    public Optional<SmartInventory> getInventory(Player p) {
        return Optional.ofNullable(this.inventories.get(p));
    }

    protected void setInventory(Player p, SmartInventory inv) {
        if(inv == null)
            this.inventories.remove(p);
        else
            this.inventories.put(p, inv);
    }

    public Optional<InventoryContents> getContents(Player p) {
        return Optional.ofNullable(this.contents.get(p));
    }

    protected void setContents(Player p, InventoryContents contents) {
        if(contents == null)
            this.contents.remove(p);
        else
            this.contents.put(p, contents);
    }

    public Map<Player, SmartInventory> getInventories() {
        return inventories;
    }

    public Map<Player, InventoryContents> getContents() {
        return contents;
    }

    class InvTask extends BukkitRunnable {

        @Override
        public void run() {
            inventories.forEach((player, inv) -> inv.getProvider().update(player, contents.get(player)));
        }

    }

}
