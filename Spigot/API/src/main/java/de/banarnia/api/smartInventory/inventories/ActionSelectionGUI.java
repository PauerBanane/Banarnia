package de.banarnia.api.smartInventory.inventories;

import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.smartInventory.content.InventoryProvider;
import de.banarnia.api.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;

public class ActionSelectionGUI implements InventoryProvider {

    public static void open(Player player, String title, BiConsumer<String,Boolean> biConsumer, List<Material> excludedMaterials,List<EntityType> excludedEntityTypes) {
        SmartInventory.builder().provider(new ActionSelectionGUI(biConsumer, excludedMaterials, excludedEntityTypes)).title(title).size(3).build().open(player);
    }

    private ActionSelectionGUI(BiConsumer<String,Boolean> biConsumer, List<Material> excludedMaterials, List<EntityType> excludedEntityTypes) {
        this.biConsumer = biConsumer;
        this.excludedMaterials = excludedMaterials;
        this.excludedEntityTypes = excludedEntityTypes;
    }

    private List<Material> excludedMaterials;
    private List<EntityType> excludedEntityTypes;
    private BiConsumer<String,Boolean> biConsumer;
    private String currentSelection;

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill().fillBorders();

        contents.set(1,3,getMaterialItem(player, contents));
        contents.set(1,5,getEntityTypeItem(player, contents));

        contents.set(2,3,getAcceptItem(player,contents));
        contents.set(2,5,getCancelItem(player, contents));
    }

    private ClickableItem getMaterialItem(Player player, InventoryContents contents) {
        ItemBuilder builder = ItemBuilder.of(Material.COBBLESTONE).name("§cMaterial auswählen");

        return ClickableItem.of(builder.build(), click -> {
            MaterialSelectionGUI.open(player, "§cMaterial auswählen", material -> {
                if (material != null)
                    currentSelection = material.toString();

                reOpen(player, contents);
            }, null, excludedMaterials);
        });
    }

    private ClickableItem getEntityTypeItem(Player player, InventoryContents contents) {
        ItemBuilder builder = ItemBuilder.of(Material.ZOMBIE_HEAD).name("§cEntity auswählen");

        return ClickableItem.of(builder.build(), click -> {
            EntityTypeSelectionGUI.open(player, "§cEntity auswählen", entityType -> {
                if (entityType != null)
                    currentSelection = entityType.toString();

                reOpen(player, contents);
            }, null, excludedEntityTypes);
        });
    }

    private ClickableItem getAcceptItem(Player player, InventoryContents contents) {
        ItemBuilder builder = ItemBuilder.of(Material.EMERALD).name("§2§lSpeichern");
        builder.lore("§8Ausgewählt: §e" + currentSelection);

        return ClickableItem.of(builder.build(), click -> {
            Material material = null;
            EntityType entityType = null;

            if (currentSelection != null && currentSelection.length() > 0) {
                try {
                    material = Material.getMaterial(currentSelection);
                } catch (Exception ex) {}
                try {
                    entityType = EntityType.valueOf(currentSelection);
                } catch (Exception ex) {}
            }

            boolean cancelled = material == null && entityType == null;

            player.closeInventory();
            biConsumer.accept(currentSelection, cancelled);
        });
    }

    private ClickableItem getCancelItem(Player player, InventoryContents contents) {
        ItemBuilder builder = ItemBuilder.of(Material.TNT).name("§4§lAbbrechen");

        return ClickableItem.of(builder.build(), click -> {
            player.closeInventory();
            biConsumer.accept(null, true);
        });
    }

}
