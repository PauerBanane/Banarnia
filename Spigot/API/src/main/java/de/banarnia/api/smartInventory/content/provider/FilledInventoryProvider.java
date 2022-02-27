package de.banarnia.api.smartInventory.content.provider;

import de.banarnia.api.smartInventory.content.InventoryContents;
import org.bukkit.entity.Player;

/* FilledInventoryProvider Klasse
 * Erstellt zusätzlich ein Standard-Layout für ein einheitliches Design.
 */
public interface FilledInventoryProvider extends InventoryProvider {

    @Override
    default void preInit(Player player, InventoryContents contents) {
        // Standard-Layout einfügen - Außen dunkle Glasscheiben, innen helle
        contents.fill();
        contents.fillBorders();
    }

}
