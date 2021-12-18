package de.banarnia.api.smartInventory.buttons;

import de.banarnia.api.chatinput.ChatInput;
import de.banarnia.api.smartInventory.ClickableItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;


public class InputButton extends ClickableItem {

    private Consumer<InventoryClickEvent> onRightClick;

    public InputButton(Consumer<String> result) {
        this(new ItemStack(Material.BOOK), "Input", result);
    }

    public InputButton(ItemStack icon, String text, Consumer<String> result) {
        this(icon, e -> {
            new ChatInput((Player) e.getWhoClicked(), text, t -> {
                result.accept(t);
            });
        });
    }

    public InputButton onRightClick(Consumer<InventoryClickEvent> consumer) {
        this.onRightClick = consumer;
        return this;
    }

    private InputButton(ItemStack item, Consumer<InventoryClickEvent> consumer) {
        super(item, consumer);
    }

    @Override
    public void run(InventoryClickEvent e) {
        if (this.onRightClick != null && e.getClick() == ClickType.RIGHT) {
            this.onRightClick.accept(e);
            return;
        }
        super.run(e);
    }

}
