package de.banarnia.api.plugin.events;

import de.banarnia.api.events.BanarniaEvent;
import de.banarnia.api.plugin.BanarniaPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/* PluginPreDisableEvent
 * Wird gefeuert bevor die Methode IBanarniaPlugin#disable in der Klasse
 * BanarniaPlugin aufgerufen wird.
 */
public class PluginPreDisableEvent extends BanarniaEvent {

    // Instanz des Plugins
    private BanarniaPlugin plugin;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Konstruktor
    public PluginPreDisableEvent(BanarniaPlugin plugin) {
        this.plugin = plugin;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public BanarniaPlugin getPlugin() {
        return plugin;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Event ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static final HandlerList HANDLERS = new HandlerList();
    public static HandlerList getHandlerList() { return HANDLERS; }
    @Override
    public HandlerList getHandlers() { return HANDLERS; }

}