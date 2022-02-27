package de.banarnia.api.util.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

/* PapiWrapper Klasse.
 * Implementiert die PAPI-Methoden zur sicheren Verwendung als SoftDependency.
 */
public class PapiWrapper {

    // Placeholder einfügen
    public static final String parse(Player player, String s) {
        return PlaceholderAPI.setPlaceholders(player, s);
    }

}
