package de.banarnia.api.smartInventory.inventories;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.smartInventory.content.InventoryProvider;
import de.banarnia.api.smartInventory.content.SlotIterator;
import de.banarnia.api.smartInventory.content.SlotPos;
import de.banarnia.api.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class RegionSelectionGUI implements InventoryProvider {

    public static void open(Player player, String title, Consumer<ProtectedRegion> consumer, World world) {
        SmartInventory.builder().title(title).size(5).provider(new RegionSelectionGUI(consumer, true, world)).build().open(player);
    }

    private RegionSelectionGUI(Consumer<ProtectedRegion> consumer, boolean playSound, World world) {
        this.consumer = consumer;
        this.sound = playSound;
        this.world = world;
    }

    private final boolean sound;
    private final Consumer<ProtectedRegion> consumer;
    private final World world;

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill().fillBorders();
        contents.setCloseItem(4,4,player);
        contents.setBackItem(4,3,player, click -> consumer.accept(null));

        if (world != null) {
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
            for (ProtectedRegion region : regionManager.getRegions().values()) {
                ItemBuilder builder = ItemBuilder.of(Material.OAK_FENCE).name("§a" + region.getId());
                builder.lore("Welt: §e" + world.getName());
                builder.lore();
                builder.lore("§eKlicken zum Auswählen");

                contents.pagination().addItem(ClickableItem.of(builder.build(), click -> consumer.accept(region)));
            }
        }

        contents.pagination(21, contents.newIterator(SlotIterator.Type.CENTERED, SlotPos.of(1,1), true));
        contents.setPaginationButtons(player, 4);
    }

}