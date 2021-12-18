package de.banarnia.api.smartInventory.inventories;

import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.smartInventory.content.InventoryProvider;
import de.banarnia.api.smartInventory.content.SlotIterator;
import de.banarnia.api.smartInventory.content.SlotPos;
import de.banarnia.api.util.ItemBuilder;
import de.banarnia.api.util.UtilClickableItem;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class GroupSelectionGUI implements InventoryProvider {

    public static void open(Player player, Consumer<Group> consumer, Group origin) {
        SmartInventory.builder().provider(new GroupSelectionGUI(consumer, origin)).size(4).title("Rang auswählen").build().open(player);
    }

    private GroupSelectionGUI(Consumer<Group> consumer, Group origin) {
        this.consumer = consumer;
        this.origin = origin;
        this.currentSelection = origin;
    }

    private Consumer<Group> consumer;
    private Group origin;
    private Group currentSelection;

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill().fillBorders();
        contents.set(3,4, UtilClickableItem.getSaveItem(click -> consumer.accept(currentSelection)));
        contents.set(3,3,UtilClickableItem.getCancelItem(click -> consumer.accept(origin)));

        for (Group group : LuckPermsProvider.get().getGroupManager().getLoadedGroups())
            contents.pagination().addItem(getGroupItem(player, contents, group));

        contents.pagination(14, contents.newIterator(SlotIterator.Type.CENTERED, SlotPos.of(1,1), true));
        contents.setPaginationButtons(player, 3);
    }

    private ClickableItem getGroupItem(Player player, InventoryContents contents, Group group) {
        ItemBuilder builder = ItemBuilder.of(Material.CHEST).name(group.getDisplayName() != null ? group.getDisplayName() : "§a" + group.getName());

        if (currentSelection == group)
            builder.enchantment(Enchantment.ARROW_DAMAGE).hideEnchants();

        return ClickableItem.of(builder.build(), click -> {
            this.currentSelection = group;
            reOpen(player, contents);
        });
    }

}
