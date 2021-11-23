package de.banarnia.api.addon;

import com.google.common.collect.Lists;
import org.bukkit.event.Listener;

import java.util.List;

/* IAddon
 * Enthält Standard-Methoden,
 * um den Code in den Addon-Klassen zu verringern.
 */
public interface IAddon {

    // Standard-Methoden beim Laden, Starten und Stoppen des Addons
    default void onLoad() {}
    default void onEnable() {}
    default void onDisable() {}

    // Plugins die benötigt werden, um dieses Addon zu starten
    default List<String> getPluginDependencies() {
        return Lists.newArrayList();
    }

    // Addons die geladen sein müssen, um dieses Addon zu starten
    default List<Class<? extends Addon>> getAddonDependencies() {
        return Lists.newArrayList();
    }

    // Zusätzliche Voraussetzungen die erfüllt sein müssen, um dieses Addon zu starten
    default boolean extraDependenciesFulfilled() {
        return true;
    }

}
