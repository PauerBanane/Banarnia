package de.banarnia.api.smartInventory.inventories;

import com.google.common.collect.Lists;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.*;
import de.banarnia.api.util.ItemBuilder;
import de.banarnia.api.util.Language;
import de.banarnia.api.util.UtilClickableItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PotionEffectTypeSelectionGUI implements InventoryProvider {

    public static void open(Player player, String title, Consumer<PotionEffectType> consumer, PotionEffectType currentSelection, List<PotionEffectType> excluded) {
        open(player, title, true, consumer, currentSelection, excluded);
    }

    public static void open(Player player, String title, boolean sound, Consumer<PotionEffectType> consumer, PotionEffectType currentSelection, List<PotionEffectType> excluded) {
        SmartInventory.builder().title(title).size(5).provider(new PotionEffectTypeSelectionGUI(consumer, sound, currentSelection, excluded)).build().open(player);
    }

    private PotionEffectTypeSelectionGUI(Consumer<PotionEffectType> consumer, boolean playSound, PotionEffectType currentSelection, List<PotionEffectType> excluded) {
        this.consumer = consumer;
        this.sound = playSound;
        this.original = currentSelection;
        this.currentSelection = currentSelection;
        this.excluded = excluded == null ? Lists.newArrayList() : excluded;
    }

    private final boolean sound;
    private final Consumer<PotionEffectType> consumer;
    private final PotionEffectType original;
    private PotionEffectType currentSelection;
    private List<PotionEffectType> excluded;

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(UtilClickableItem.getFiller()));
        Pagination pagination = contents.pagination();
        ArrayList<ClickableItem> items = Lists.newArrayList();

        for (PotionEffectType potionEffectType : PotionEffectType.values()) {
            if (excluded != null && excluded.contains(potionEffectType)) continue;
            ItemBuilder builder = new ItemBuilder(Material.POTION).name(Language.getName(potionEffectType));
            if (currentSelection == potionEffectType)
                builder.enchantment(Enchantment.ARROW_DAMAGE)
                        .setItemFlag(ItemFlag.HIDE_ENCHANTS);

            items.add(ClickableItem.of(builder.build(), click -> {
                if (currentSelection == potionEffectType) return;
                currentSelection = potionEffectType;
                reOpen(player, contents);
            }));
        }

        contents.set(4,3, ClickableItem.of(new ItemBuilder(Material.EMERALD).name("§2§lSpeichern").build(), click -> {
            player.closeInventory();
            consumer.accept(currentSelection);
        }));

        contents.set(4,5, ClickableItem.of(new ItemBuilder(Material.TNT).name("§4§lAbbrechen").build(), click -> {
            player.closeInventory();
            consumer.accept(original);
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
