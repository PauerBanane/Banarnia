package de.banarnia.api.plugin;

import co.aikar.commands.*;
import co.aikar.commands.contexts.ContextResolver;
import com.google.common.collect.Maps;
import de.banarnia.api.addon.AddonManager;
import de.banarnia.api.commands.DefaultCommandContextResolver;
import de.banarnia.api.plugin.events.PluginPreDisableEvent;
import de.banarnia.api.plugin.events.PluginPreEnableEvent;
import de.banarnia.api.plugin.events.PluginPreLoadEvent;
import de.banarnia.api.util.FileLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

/* BanarniaPlugin
 * Enthält Standard-Methoden,
 * um den Code in den Main-Klassen zu verringern.
 */
public abstract class BanarniaPlugin extends JavaPlugin implements IBanarniaPlugin {

    // Static LinkedHashMap mit allen erbenden Klassen
    private static LinkedHashMap<String, BanarniaPlugin> loadedPlugins = Maps.newLinkedHashMap();

    // Plugin Informationen - werden in #onLoad() initialisiert
    private String name;
    private String version;
    private Material icon;

    // Config
    private FileLoader config;

    // Manager Instanzen
    protected PaperCommandManager commandManager;
    protected PluginManager pluginManager;
    protected AddonManager addonManager;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Status Handling ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Wird beim Laden des Servers aufgerufen
    @Override
    public final void onLoad() {
        // Instanz die die Map eintragen
        loadedPlugins.put(this.getName(), this);

        // Plugin Informationen
        this.name = getName();
        this.version = getDescription().getVersion();
        this.icon = Material.GRASS_BLOCK;

        // Config
        this.config = FileLoader.of(getDataFolder(), "config.yml");

        // Manager Instanzen
        this.pluginManager = Bukkit.getPluginManager();
        this.addonManager = AddonManager.getInstance();

        // Initialisierungs-Methoden des Interface IBanarniaPlugin aufrufen
        registerAddons();

        // Addons laden
        this.addonManager.loadAddons(this);

        // Event aufrufen
        new PluginPreLoadEvent(this).callEvent();

        // Methode vom Interface IBanarniaPlugin aufrufen
        load();
    }

    // Wird beim Starten des Plugins aufgerufen
    @Override
    public final void onEnable() {
        // CommandManager einrichten
        this.commandManager = new PaperCommandManager(this);
        this.commandManager.getLocales().setDefaultLocale(Locale.GERMAN);

        // Standard Completion und Context registrieren
        DefaultCommandContextResolver.registerDefaultContextAndCompletions(commandManager);

        // Addons laden
        TODO: this.addonManager.enableAddons(this);

        // Event aufrufen
        new PluginPreEnableEvent(this).callEvent();

        // Methode vom Interface IBanarniaPlugin aufrufen
        load();
    }

    // Wird beim Beenden des Plugins aufgerufen
    @Override
    public final void onDisable() {
        // Addons deaktivieren
        this.addonManager.disableAddons(this);

        // Event aufrufen
        new PluginPreDisableEvent(this).callEvent();

        // Methode vom Interface IBanarniaPlugin
        disable();

        // Instanz aus der Map entfernen
        loadedPlugins.remove(this.getName());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Listener im PluginManager registrieren
    public final void registerListener(Listener listener) {
        this.pluginManager.registerEvents(listener, this);
    }

    // Command im PaperCommandManager registrieren
    public final void registerCommand(BaseCommand command) {
        this.commandManager.registerCommand(command);
    }

    // Command-Completion im PaperCommandManager registrieren
    public void registerCommandCompletion(String id, CommandCompletions.CommandCompletionHandler<BukkitCommandCompletionContext> handler) {
        commandManager.getCommandCompletions().registerCompletion(id,handler);
    }

    // Command-Context im PaperCommandManager registrieren
    public void registerCommandContext(Class context, ContextResolver<Object, BukkitCommandExecutionContext> supplier) {
        commandManager.getCommandContexts().registerContext(context, supplier);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public FileLoader getConfigFile() {
        return config;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public static LinkedHashMap<String, BanarniaPlugin> getLoadedPlugins() {
        return loadedPlugins;
    }
}
