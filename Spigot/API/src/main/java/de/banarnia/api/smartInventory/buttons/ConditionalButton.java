package de.banarnia.api.smartInventory.buttons;

import de.banarnia.api.smartInventory.ClickableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConditionalButton extends ClickableItem {

    public ConditionalButton(ItemStack item, Predicate<Player> condition,  Consumer<InventoryClickEvent> consumer) {
        super(item, consumer);
        this.condition = condition;
    }

    private final Predicate<Player> condition;
    private String error = "";

    public ConditionalButton error(String error) {
        this.error = error;
        return this;
    }

    @Override
    public void run(InventoryClickEvent e) {
        final Player player = (Player) e.getWhoClicked();
        if (!condition.test(player)) {
            if (!error.isEmpty()) player.sendMessage(this.error);
            return;
        }
        super.run(e);
    }

    @Override
    public ItemStack getItem() {
        return super.getItem();
    }
}
