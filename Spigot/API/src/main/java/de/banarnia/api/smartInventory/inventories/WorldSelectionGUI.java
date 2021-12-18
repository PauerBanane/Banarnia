package de.banarnia.api.smartInventory.inventories;

import com.google.common.collect.Lists;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.*;
import de.banarnia.api.util.ItemBuilder;
import de.banarnia.api.util.UtilClickableItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.function.Consumer;

public class WorldSelectionGUI implements InventoryProvider {

    public static void open(Player player, String title, Consumer<World> consumer, World currentWorld) {
        open(player, title, true, consumer, currentWorld);
    }

    public static void open(Player player, String title, boolean sound, Consumer<World> consumer, World currentWorld) {
        SmartInventory.builder().title(title).size(5).provider(new WorldSelectionGUI(consumer, sound, currentWorld)).build().open(player);
    }

    private WorldSelectionGUI(Consumer<World> consumer, boolean playSound, World currentWorld) {
        this.consumer = consumer;
        this.sound = playSound;
        this.originalWorld = currentWorld;
        this.currentSelection = currentWorld;
    }

    private final boolean sound;
    private final Consumer<World> consumer;
    private final World originalWorld;
    private World currentSelection;

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(UtilClickableItem.getFiller()));
        Pagination pagination = contents.pagination();
        ArrayList<ClickableItem> items = Lists.newArrayList();

        for (World world : Bukkit.getWorlds()) {
            ItemBuilder builder = new ItemBuilder(Material.GRASS_BLOCK);
            builder.name("§c" + world.getName());
            builder.lore("Spieler: §a" + world.getPlayerCount());
            if (currentSelection == world)
                builder.enchantment(Enchantment.ARROW_DAMAGE)
                        .setItemFlag(ItemFlag.HIDE_ENCHANTS);

            items.add(ClickableItem.of(builder.build(), click -> {
                if (currentSelection == world) return;
                currentSelection = world;
                reOpen(player, contents);
            }));
        }

        contents.set(4,3, ClickableItem.of(new ItemBuilder(Material.EMERALD).name("§2§lSpeichern").build(), click -> {
            player.closeInventory();
            consumer.accept(currentSelection);
        }));

        contents.set(4,5, ClickableItem.of(new ItemBuilder(Material.TNT).name("§4§lAbbrechen").build(), click -> {
            player.closeInventory();
            consumer.accept(originalWorld);
        }));

        ClickableItem[] c = new ClickableItem[items.size()];
        c = items.<ClickableItem>toArray(c);
        pagination.setItems(c);
        pagination.setItemsPerPage(27);
        if (items.size() > 0 && !pagination.isLast())
            contents.set(4, 7, ClickableItem.of((new ItemBuilder(Material.ARROW)).name("§f§lSeite vor").build(), e -> {
                contents.inventory().open(player, pagination.next().getPage());
            }));
        if (!pagination.isFirst())
            contents.set(4, 1, ClickableItem.of((new ItemBuilder(Material.ARROW)).name("§f§lSeite zurück").build(), e -> {
                contents.inventory().open(player, pagination.previous().getPage());
            }));

        SlotIterator slotIterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1,0));
        slotIterator = slotIterator.allowOverride(true);
        pagination.addToIterator(slotIterator);

    }

}
