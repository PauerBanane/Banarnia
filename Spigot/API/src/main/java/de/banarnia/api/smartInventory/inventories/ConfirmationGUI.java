package de.banarnia.api.smartInventory.inventories;

import com.google.common.collect.Lists;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.smartInventory.content.InventoryProvider;
import de.banarnia.api.util.ItemBuilder;
import de.banarnia.api.util.UtilClickableItem;
import de.banarnia.api.util.UtilPlayer;
import de.banarnia.api.util.UtilScheduler;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;


public class ConfirmationGUI implements InventoryProvider {

    public static void open(Player player, String title, Consumer<Boolean> consumer) {
        open(player, title, true, consumer);
    }

    public static void open(Player player, String title, boolean sound, Consumer<Boolean> consumer) {
        SmartInventory.builder().title(title).size(4).provider(new ConfirmationGUI(consumer, sound)).build().open(player);
    }

    private ConfirmationGUI(Consumer<Boolean> consumer, boolean playSound) {
        this.consumer = consumer;
        this.sound = playSound;
    }

    private final boolean sound;
    private final Consumer<Boolean> consumer;
    private boolean confirm = false;

    private List<Integer> acceptSlots = Lists.newArrayList(1,2,9,10,11,12,18,19,20,21,28,29);
    private List<Integer> denySlots   = Lists.newArrayList(6,7,14,15,16,17,23,24,25,26,33,34);

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(UtilClickableItem.getFiller()));

        for (int i = 0; i < 36; i++) {
            if (acceptSlots.contains(i))
                contents.set(i, getConfirmItem(player, contents));
            if (denySlots.contains(i))
                contents.set(i, getDenyItem(player, contents));
        }
    }

    @Override
    public void onClose(Player player, InventoryContents contents) {
        UtilScheduler.runTaskLater(()->{
            this.consumer.accept(this.confirm);
        }, 1L);
    }

    private ClickableItem getConfirmItem(Player player, InventoryContents contents) {
        ItemBuilder builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).name(" ");

        return ClickableItem.of(builder.build(), click -> {
            this.confirm = true;
            if (sound) {
                UtilPlayer.playSound(player, Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.7F, 1.25F);
            }
            player.closeInventory();
        });
    }

    private ClickableItem getDenyItem(Player player, InventoryContents contents) {
        ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(" ");

        return ClickableItem.of(builder.build(), click -> {
            this.confirm = false;
            if (sound) {
                UtilPlayer.playSound(player, Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.7F, 1.25F);
            }
            player.closeInventory();
        });
    }

}
