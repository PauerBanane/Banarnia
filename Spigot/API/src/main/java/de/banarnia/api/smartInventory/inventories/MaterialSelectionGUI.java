package de.banarnia.api.smartInventory.inventories;

import com.google.common.collect.Lists;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.*;
import de.banarnia.api.util.ItemBuilder;
import de.banarnia.api.util.UtilClickableItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MaterialSelectionGUI implements InventoryProvider {

    public static void open(Player player, String title, Consumer<Material> consumer, Material currentMaterial, List<Material> excludedMaterials) {
        open(player, title, true, consumer, currentMaterial, excludedMaterials);
    }

    public static void open(Player player, String title, Consumer<Material> consumer, Material currentMaterial) {
        open(player, title, true, consumer, currentMaterial, null);
    }

    public static void open(Player player, String title, boolean sound, Consumer<Material> consumer, Material currentMaterial, List<Material> excludedMaterials) {
        SmartInventory.builder().title(title).size(5).provider(new MaterialSelectionGUI(consumer, sound, currentMaterial, excludedMaterials)).build().open(player);
    }

    private MaterialSelectionGUI(Consumer<Material> consumer, boolean playSound, Material currentMaterial, List<Material> excludedMaterials) {
        this.consumer = consumer;
        this.sound = playSound;
        this.originalMaterial = currentMaterial;
        this.currentSelection = currentMaterial;
        this.excludedMaterials = excludedMaterials == null ? Lists.newArrayList() : excludedMaterials;
    }

    private final boolean sound;
    private final Consumer<Material> consumer;
    private final Material originalMaterial;
    private Material currentSelection;
    private List<Material> excludedMaterials;

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(UtilClickableItem.getFiller()));
        Pagination pagination = contents.pagination();
        ArrayList<ClickableItem> items = Lists.newArrayList();

        for (Material material : Material.values()) {
            if (excludedMaterials != null && excludedMaterials.contains(material)) continue;
            ItemBuilder builder = new ItemBuilder(material);
            if (currentSelection == material)
                builder.enchantment(Enchantment.ARROW_DAMAGE)
                       .setItemFlag(ItemFlag.HIDE_ENCHANTS);

            items.add(ClickableItem.of(builder.build(), click -> {
                if (currentSelection == material) return;
                currentSelection = material;
                reOpen(player, contents);
            }));
        }

        contents.set(4,3, ClickableItem.of(new ItemBuilder(Material.EMERALD).name("§2§lSpeichern").build(), click -> {
            player.closeInventory();
            consumer.accept(currentSelection);
        }));

        contents.set(4,5, ClickableItem.of(new ItemBuilder(Material.TNT).name("§4§lAbbrechen").build(), click -> {
            player.closeInventory();
            consumer.accept(originalMaterial);
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
