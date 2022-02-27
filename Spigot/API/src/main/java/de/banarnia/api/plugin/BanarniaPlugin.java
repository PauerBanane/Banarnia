package de.banarnia.api.plugin;

import co.aikar.commands.*;
import co.aikar.commands.contexts.ContextResolver;
import com.google.common.collect.Maps;
import de.banarnia.api.addon.AddonManager;
import de.banarnia.api.commands.DefaultCommandContextResolver;
import de.banarnia.api.messages.Message;
import de.banarnia.api.messages.MessageHandler;
import de.banarnia.api.plugin.events.PluginPreDisableEvent;
import de.banarnia.api.plugin.events.PluginPreEnableEvent;
import de.banarnia.api.plugin.events.PluginPreLoadEvent;
import de.banarnia.api.skulls.SkullManager;
import de.banarnia.api.smartInventory.InventoryManager;
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

    // Message Handler
    protected MessageHandler messageHandler;

    // Manager Instanzen
    protected SkullManager skullManager;
    protected PluginManager pluginManager;
    protected BukkitCommandManager commandManager;
    protected AddonManager addonManager;
    protected InventoryManager inventoryManager;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Status Handling ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Wird beim Laden des Servers aufgerufen
    @Override
    public final void onLoad() {
        // Pre-Load Methode aufrufen
        preLoad();

        // Instanz in die Map eintragen
        loadedPlugins.put(this.getName(), this);

        // Plugin Informationen
        this.name       = getName();
        this.version    = getDescription().getVersion();
        this.icon       = Material.GRASS_BLOCK;

        // Config
        this.config = FileLoader.of(getDataFolder(), "config.yml");

        // MessageHandler laden
        this.messageHandler = MessageHandler.getInstance();

        // Manager Instanzen
        this.skullManager       = SkullManager.getInstance();
        this.pluginManager      = Bukkit.getPluginManager();
        this.addonManager       = AddonManager.getInstance();

        // Addons initialisieren
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

        // Manager Instanzen - wenn sie Listener registrieren
        this.inventoryManager   = InventoryManager.getInstance();

        // Addons aktivieren
        this.addonManager.enableAddons(this);

        // Event aufrufen
        new PluginPreEnableEvent(this).callEvent();

        // Methode vom Interface IBanarniaPlugin aufrufen
        enable();
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

    // Command im BukkitCommandManager registrieren
    public final void registerCommand(BaseCommand command) {
        this.commandManager.registerCommand(command);
    }

    // Command-Completion im BukkitCommandManager registrieren
    public void registerCommandCompletion(String id, CommandCompletions.CommandCompletionHandler<BukkitCommandCompletionContext> handler) {
        commandManager.getCommandCompletions().registerCompletion(id,handler);
    }

    // Command-Context im BukkitCommandManager registrieren
    public void registerCommandContext(Class context, ContextResolver<Object, BukkitCommandExecutionContext> supplier) {
        commandManager.getCommandContexts().registerContext(context, supplier);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public FileLoader getConfigFile() {
        return config;
    }

    public BukkitCommandManager getCommandManager() {
        return commandManager;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public static LinkedHashMap<String, BanarniaPlugin> getLoadedPlugins() {
        return loadedPlugins;
    }
}
