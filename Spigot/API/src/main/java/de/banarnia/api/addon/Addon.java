package de.banarnia.api.addon;

import co.aikar.commands.BaseCommand;
import com.google.common.collect.Lists;
import de.banarnia.api.plugin.BanarniaPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.ArrayList;

/* Addon
 * Dient als abgekapselter Programmteil, welcher aktiviert und deaktiviert werden kann.
 * Wird direkt bei der Aktivierung als Listener registriert.
 */
public class Addon implements Listener, IAddon {

    // Instanzen
    protected BanarniaPlugin plugin;
    protected PluginManager  pluginManager;

    // Addon Informationen
    protected String    name;           // Name des Addons
    protected boolean   loaded;         // Addon wurde während des Serverstarts geladen
    protected boolean   enabled;        // Addon ist aktiviert
    protected boolean   loadOnStartup;  // Addon war beim Start aktiviert und soll geladen werden

    // Commands & Listener
    private ArrayList<BaseCommand>  addonCommands  = Lists.newArrayList();
    private ArrayList<Listener>     addonListeners = Lists.newArrayList();

    // Files
    private File addonFolder;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Status-Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Wird ausgeführt, wenn das Addon beim Serverstart geladen wird
    protected void load() {
        // Methode im Addon aufrufen
        onLoad();

        // Status auf geladen setzen
        loaded = true;
    }

    // Wird ausgeführt, wenn ein Addon aktiviert wird
    protected void enable() {
        // Methode im Addon aufrufen
        onEnable();

        // Status auf Aktiviert setzen
        enabled = true;
    }

    // Wird ausgeführt, wenn ein Addon deaktiviert wird
    protected void disable() {
        // Methode im Addon aufrufen
        onDisable();

        // Status auf Deaktiviert und Nicht geladen setzen
        loaded = false;
        enabled = false;
    }

    // Wird ausgeführt, wenn ein Addon neu geladen wird
    protected void reload() {
        // Addon deaktivieren
        disable();

        // Addon aktivieren
        enable();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Command & Listener Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Command registrieren
    public void registerCommand(BaseCommand command) {
        // Null check
        Validate.notNull(command);

        // Command registrieren
        plugin.registerCommand(command);

        // Command zur Liste der Addon-Commands hinzufügen
        addonCommands.add(command);
    }

    // Command deregistrieren
    public void unregisterCommand(BaseCommand command) {
        // Null check
        Validate.notNull(command);

        // Command deregistrieren
        plugin.getCommandManager().unregisterCommand(command);

        // Command von der Liste der Addon-Commands entfernen
        addonCommands.remove(command);
    }

    // Listener registrieren
    public void registerListener(Listener listener) {
        // Null check
        Validate.notNull(listener);

        // Listener registrieren
        pluginManager.registerEvents(listener, plugin);

        // Listener zur Liste der Addon-Listener hinzufügen
        addonListeners.add(listener);
    }

    // Listener deregistrieren
    public void unregisterListener(Listener listener) {
        // Null check
        Validate.notNull(listener);

        // Listener deregistrieren
        HandlerList.unregisterAll(listener);

        // Listener von der Liste der Addon-Listener entfernen
        addonListeners.remove(listener);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /* AddonFolder
     * Gibt den Ordner '/plugins/<Plugin>/Addons' zurück
     */
    public final File getAddonFolder() {
        // Abfrage, ob die Instanz bereits initialisiert wurde und ob der Ordner noch existiert
        if (addonFolder != null && addonFolder.exists())
            return addonFolder;

        // Instanz erstellen
        addonFolder = new File(plugin.getDataFolder(), "Addons");

        // Ordner erstellen, falls er noch nicht existiert
        if (!addonFolder.exists())
             addonFolder.mkdirs();

        // Instanz zurückgeben
        return addonFolder;
    }

    // Gibt an, ob das Addon aktiviert ist
    public boolean isEnabled() {
        return enabled;
    }

    // Gibt an, ob das Addon geladen wurde
    public boolean isLoaded() {
        return loaded;
    }

    // Gibt an, ob das Addon beim Start geladen werden soll
    public boolean shouldLoadOnStartup() {
        return loadOnStartup;
    }

    // Namen des Addons zurückgeben
    public String getName() {
        return name;
    }

    // Plugin des Addons zurückgeben
    public BanarniaPlugin getPlugin() {
        return plugin;
    }
}
