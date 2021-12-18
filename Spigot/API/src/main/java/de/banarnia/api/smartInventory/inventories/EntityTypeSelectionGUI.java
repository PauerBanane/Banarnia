package de.banarnia.api.smartInventory.inventories;

import com.google.common.collect.Lists;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.*;
import de.banarnia.api.util.ItemBuilder;
import de.banarnia.api.util.Language;
import de.banarnia.api.util.UtilClickableItem;
import de.banarnia.api.util.UtilItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EntityTypeSelectionGUI implements InventoryProvider {

    public static void open(Player player, String title, Consumer<EntityType> consumer, EntityType currentEntityType, List<EntityType> excludedEntityTypes) {
        open(player, title, true, consumer, currentEntityType, excludedEntityTypes);
    }

    public static void open(Player player, String title, boolean sound, Consumer<EntityType> consumer, EntityType currentEntityType, List<EntityType> excludedEntityTypes) {
        SmartInventory.builder().title(title).size(5).provider(new EntityTypeSelectionGUI(consumer, sound, currentEntityType, excludedEntityTypes)).build().open(player);
    }

    private EntityTypeSelectionGUI(Consumer<EntityType> consumer, boolean playSound, EntityType currentEntityType, List<EntityType> excludedMaterials) {
        this.consumer = consumer;
        this.sound = playSound;
        this.originalMaterial = currentEntityType;
        this.currentSelection = currentEntityType;
        this.excludedEntityTypes = excludedEntityTypes == null ? Lists.newArrayList() : excludedEntityTypes;
    }

    private final boolean sound;
    private final Consumer<EntityType> consumer;
    private final EntityType originalMaterial;
    private EntityType currentSelection;
    private List<EntityType> excludedEntityTypes;

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(UtilClickableItem.getFiller()));
        Pagination pagination = contents.pagination();
        ArrayList<ClickableItem> items = Lists.newArrayList();

        for (EntityType entityType : EntityType.values()) {
            if (excludedEntityTypes != null && excludedEntityTypes.contains(entityType)) continue;
            ItemBuilder builder = new ItemBuilder(UtilItem.getSpawnEgg(entityType)).name(Language.getName(entityType));
            if (currentSelection == entityType)
                builder.enchantment(Enchantment.ARROW_DAMAGE)
                        .setItemFlag(ItemFlag.HIDE_ENCHANTS);

            items.add(ClickableItem.of(builder.build(), click -> {
                if (currentSelection == entityType) return;
                currentSelection = entityType;
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
