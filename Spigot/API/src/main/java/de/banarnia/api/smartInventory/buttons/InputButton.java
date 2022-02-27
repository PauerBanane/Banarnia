package de.banarnia.api.smartInventory.buttons;

import de.banarnia.api.chatinput.ChatInput;
import de.banarnia.api.messages.Message;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.util.Title;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/* InputButton Klasse.
 * Damit können direkt ClickableItems erstellt werden,
 * welche die Eingabe eines bestimmten Datentyps in den Chat erfordern.
 */
public class InputButton<T> extends ClickableItem {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Konstruktor mit Haupttitel als String
    public InputButton(Player player, ItemStack icon, Class<T> type, BiConsumer<T, Boolean> input, String message, String title) {
        this(player, icon, type, input, message, Title.of(title, Message.CHAT_INPUT_DEFAULT_SUBTITLE.getKey()));
    }

    // Standard Konstruktor
    public InputButton(Player player, ItemStack icon, Class<T> type, BiConsumer<T, Boolean> input, String message, Title title) {
        super(icon, click -> {
            new ChatInput<T>(type)
                    .message(message)
                    .title(title)
                    .response(input)
                    .start(player);
        });
    }

}
