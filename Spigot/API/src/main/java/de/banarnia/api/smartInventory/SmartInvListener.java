package de.banarnia.api.smartInventory;

import de.banarnia.api.BanarniaAPI;
import de.banarnia.api.util.F;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.HashSet;


@SuppressWarnings("unchecked")
public class SmartInvListener implements Listener {

    private final InventoryManager manager;

    public SmartInvListener(InventoryManager manager) {
        this.manager = manager;
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(!manager.getInventories().containsKey(p))
            return;

        if (e.getAction() == InventoryAction.NOTHING || e.getClickedInventory() == null) {
            e.setCancelled(true);
            return;
        }

        if (e.getClickedInventory().equals(p.getOpenInventory().getBottomInventory())) {
            if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                e.setCancelled(true);
                return;
            }
        }



        if(e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {
            try {
                e.setCancelled(true);

                int row = e.getSlot() / 9;
                int column = e.getSlot() % 9;

                if (row < 0 || column < 0)
                    return;

                SmartInventory inv = manager.getInventories().get(p);

                if (row >= inv.getRows() || column >= inv.getColumns())
                    return;

                inv.getListeners().stream()
                        .filter(listener -> listener.getType() == InventoryClickEvent.class)
                        .forEach(listener -> ((InventoryListener<InventoryClickEvent>) listener).accept(e));

                manager.getContents().get(p).get(row, column).ifPresent(item -> item.run(e));

                p.updateInventory();
            } catch (Exception ex) {
                p.sendMessage(F.error("Fehler", "Es ist ein Fehler aufgetreten. Bitte melde diesen an einen Admin."));
                ex.printStackTrace();
                SmartInventory inv = manager.getInventories().get(p);
                p.closeInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryDrag(InventoryDragEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(!manager.getInventories().containsKey(p))
            return;

        for(int slot : e.getRawSlots()) {
            if(slot >= p.getOpenInventory().getTopInventory().getSize())
                continue;
            e.setCancelled(true);
            break;
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();

        if(!manager.getInventories().containsKey(p))
            return;

        SmartInventory inv = manager.getInventories().get(p);

        inv.getListeners().stream()
                .filter(listener -> listener.getType() == InventoryOpenEvent.class)
                .forEach(listener -> ((InventoryListener<InventoryOpenEvent>) listener).accept(e));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if(!manager.getInventories().containsKey(p))
            return;

        SmartInventory inv = manager.getInventories().get(p);
        inv.getProvider().onClose(p, manager.getContents().get(p));

        inv.getListeners().stream()
                .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                .forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener).accept(e));

        if(inv.isCloseable()) {
            e.getInventory().clear();

            manager.getInventories().remove(p);
            manager.getContents().remove(p);
        }
        else
            Bukkit.getScheduler().runTask(BanarniaAPI.getInstance(), () -> p.openInventory(e.getInventory()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(!manager.getInventories().containsKey(p))
            return;

        SmartInventory inv = manager.getInventories().get(p);
        inv.getProvider().onClose(p, manager.getContents().get(p));

        manager.getInventories().remove(p);
        manager.getContents().remove(p);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPluginDisable(PluginDisableEvent e) {
        new HashSet<>(manager.getInventories().entrySet()).forEach(entry -> {
            SmartInventory inv = entry.getValue();
            inv.close(entry.getKey());
        });

        manager.getInventories().clear();
        manager.getContents().clear();
    }
}
