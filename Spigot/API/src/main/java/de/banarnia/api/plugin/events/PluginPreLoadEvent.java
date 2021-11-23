package de.banarnia.api.plugin.events;

import de.banarnia.api.events.BanarniaEvent;
import de.banarnia.api.plugin.BanarniaPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/* PluginPreLoadEvent
 * Wird gefeuert bevor die Methode IBanarniaPlugin#load in der Klasse
 * BanarniaPlugin aufgerufen wird.
 */
public class PluginPreLoadEvent extends BanarniaEvent {

    // Instanz des Plugins
    private BanarniaPlugin plugin;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Konstruktor
    public PluginPreLoadEvent(BanarniaPlugin plugin) {
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
