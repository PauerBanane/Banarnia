package de.banarnia.api.smartInventory.inventories;

import com.google.common.collect.Lists;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.*;
import de.banarnia.api.util.ItemBuilder;
import de.banarnia.api.util.UtilClickableItem;
import de.banarnia.api.util.UtilPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SoundSelectionGUI implements InventoryProvider {

    public static void open(Player player, String title, Consumer<Sound> consumer, Sound currentSound) {
        open(player, title, true, consumer, currentSound);
    }

    public static void open(Player player, String title, boolean sound, Consumer<Sound> consumer, Sound currentSound) {
        SmartInventory.builder().title(title).size(5).provider(new SoundSelectionGUI(consumer, sound, currentSound)).build().open(player);
    }

    private SoundSelectionGUI(Consumer<Sound> consumer, boolean playSound, Sound currentSound) {
        this.consumer = consumer;
        this.sound = playSound;
        this.originalSound = currentSound;
        this.currentSelection = currentSound;
    }

    private final boolean sound;
    private final Consumer<Sound> consumer;
    private final Sound originalSound;
    private Sound currentSelection;

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(UtilClickableItem.getFiller()));
        Pagination pagination = contents.pagination();
        ArrayList<ClickableItem> items = Lists.newArrayList();

        for (Sound sound : Sound.values()) {
            ItemBuilder builder = new ItemBuilder(Material.NOTE_BLOCK);
            builder.name("§c" + sound.name());
            builder.lore("§eRechtsklick zum Abspielen");
            if (currentSelection == sound)
                builder.enchantment(Enchantment.ARROW_DAMAGE)
                        .setItemFlag(ItemFlag.HIDE_ENCHANTS);

            items.add(ClickableItem.of(builder.build(), click -> {
                if (click.isRightClick()) {
                    UtilPlayer.playSound(player, sound);
                    return;
                }

                if (currentSelection == sound) return;
                currentSelection = sound;
                reOpen(player, contents);
            }));
        }

        contents.set(4,3, ClickableItem.of(new ItemBuilder(Material.EMERALD).name("§2§lSpeichern").build(), click -> {
            player.closeInventory();
            consumer.accept(currentSelection);
        }));

        contents.set(4,5, ClickableItem.of(new ItemBuilder(Material.TNT).name("§4§lAbbrechen").build(), click -> {
            player.closeInventory();
            consumer.accept(originalSound);
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