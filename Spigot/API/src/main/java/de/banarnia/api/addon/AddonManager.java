package de.banarnia.api.addon;

import com.google.common.collect.Maps;
import de.banarnia.api.messages.Message;
import de.banarnia.api.plugin.BanarniaPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/* AddonManager
 * Verwaltet alle Addons des Plugin.
 * Hier können Addons aktiviert, deaktiviert und neu geladen werden.
 */
public class AddonManager {

    // Static-Instanz
    private static AddonManager instance;

    // Addon map
    private HashMap<String, Addon> addons = Maps.newHashMap();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Konstruktor, um die Instanz zu setzen
    private AddonManager() {
        instance = this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Addon registrieren ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final void registerAddon(Addon addon, String name, BanarniaPlugin plugin) {
        // Null check
        Validate.notNull(addon);

        // Leerzeichen aus Namen entfernen
        String addonName = name.replace(" ", "_");

        // Abfragen, ob das Addon bereits registriert wurde.
        // Addon wird dann nicht mehr registriert.
        if (isRegistered(addon.getClass()) || isRegistered(addonName)) {
            plugin.getLogger().warning(Message.ERROR_ADDON_ALREADY_REGISTERED.replace("%addon%", addonName));
            return;
        }

        // Instanzen des Addons zuweisen
        addon.plugin        = plugin;
        addon.name          = addonName;
        addon.pluginManager = plugin.getPluginManager();

        // Addon in der Config eintragen
        if (!plugin.getConfig().isSet("Addons." + addonName + ".Enabled")) {
            plugin.getConfig().set("Addons." + addonName + ".Enabled", false);
            plugin.saveConfig();
        }

        // Addon in die Map hinzufügen
        addons.put(addonName, addon);

        // Boolean, ob das Addon direkt aktiviert werden soll.
        boolean enableOnStartup = plugin.getConfig().getBoolean("Addons." + addonName + ".Enabled", false);

        // Info senden, wenn das Addon nicht zum Serverstart aktiviert werden soll
        if (!enableOnStartup) {
            plugin.getLogger().info(Message.INFO_ADDON_NOT_LOADED.replace("%addon%", addonName));
            return;
        }

        // Addon als Serverstart-Addon markieren
        addon.loadOnStartup = true;
    }

    // Abfragen, ob ein Addon mit dem Namen registriert ist
    public final boolean isRegistered(String name) {
        // For-Schleife durch alle Addons, um die Namen zu vergleichen
        for (Addon addon : getAddons()) {
            if (addon.getName().equals(name))
                return true;
        }

        // False zurückgeben, wenn kein Addon den abgegebenen Namen hat
        return false;
    }

    // Abfragen, ob ein Addon mit dieser Klasse registriert ist
    public boolean isRegistered(Class<? extends Addon> clazz) {
        return getAddon(clazz) != null;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Addon Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Prüft, ob alle Voraussetzungen für das Laden eines Addons erfüllt sind
    public final boolean checkDependencies(Addon addon, boolean loadDependencies, boolean enableDependencies) {
        // Addon-Voraussetzungen
        for (Class<? extends Addon> clazz : addon.getAddonDependencies()) {
            // Instanz des Addons anhand der Klassen abrufen
            Addon dependency = getAddon(clazz);

            // Wenn das Addon nicht existiert false zurückgeben
            if (dependency == null) return false;

            // Vorausgesetztes Addon laden, wenn es noch nicht geladen wurde
            if (loadDependencies && !dependency.isLoaded() && dependency.shouldLoadOnStartup())
                loadAddon(dependency);

            // Vorausgesetztes Addon aktivieren, wenn es noch nicht aktiviert wurde
            if (enableDependencies && !dependency.isEnabled() && dependency.shouldLoadOnStartup())
                enableAddon(dependency, true);

            // Wenn das vorausgesetzte Addon immer noch nicht geladen wurde wird false zurückgegeben
            if (loadDependencies && !dependency.isLoaded() ||
                enableDependencies && ! dependency.isEnabled())
                return false;
        }

        // Plugin-Voraussetzungen
        for (String plugin : addon.getPluginDependencies()) {
            // False zurückgeben, wenn das Plugin nicht gefunden wurde
            if (Bukkit.getPluginManager().getPlugin(plugin) == null)
                return false;
        }

        // Ergebnis der Auswertung der zusätzlichen Voraussetzungen zurückgeben
        return addon.extraDependenciesFulfilled();
    }

    // Wird beim Laden des Addons während der #onLoad() Phase des Plugins ausgeführt
    public final void loadAddon(Addon addon) {
        // Null check
        Validate.notNull(addon);

        // Methode abbrechen, wenn das Addon schon geladen wurde
        if (addon.isLoaded()) return;

        // Abfrage, ob das Addon zum Serverstart geladen werden soll
        Validate.isTrue(addon.shouldLoadOnStartup());

        // Überprüfen der Dependencies
        if (!checkDependencies(addon, true, false)) {
            addon.getPlugin().getLogger()
                 .warning(Message.ERROR_ADDON_DEPENDENCY_NOT_FULFILLED.replace("%addon%", addon.getName()));
            return;
        }

        // Addon laden
        addon.load();
    }

    // Wird beim Laden des Addons ausgeführt
    public final void enableAddon(Addon addon, boolean updateConfig) {
        // Null check
        Validate.notNull(addon);

        // Methode abbrechen, wenn das Addon schon aktiviert ist
        if (addon.isEnabled()) return;

        // Überprüfen der Dependencies
        if (!checkDependencies(addon, false, true)) {
            addon.getPlugin().getLogger()
                    .warning(Message.ERROR_ADDON_DEPENDENCY_NOT_FULFILLED.replace("%addon%", addon.getName()));
            return;
        }

        // Abfrage, ob der Config-Eintrag gesetzt werden soll
        if (updateConfig) {
            addon.getPlugin().getConfig().set("Addons." + addon.getName() + " .Enabled", true);
            addon.getPlugin().saveConfig();
        }

        // Addon aktivieren
        addon.enable();

        // Log-Nachricht senden
        addon.getPlugin().getLogger().info(Message.INFO_ADDON_ENABLED.replace("%addon%", addon.getName()));
    }

    // Wird beim Deaktivieren des Addons ausgeführt
    public final void disableAddon(Addon addon, boolean updateConfig) {
        // Null check
        Validate.notNull(updateConfig);

        // Methode abbrechen, wenn das Addon bereits deaktiviert ist
        if (!addon.isEnabled()) return;

        // Addons deaktivieren, die dieses Addon als Abhängigkeit haben
        for (Addon mapAddon : getDependendAddons(addon)) {
            // Überspringen, wenn das Addon deaktiviert ist
            if (!mapAddon.isEnabled()) continue;

            // Addon Abhängigkeit überprüfen und gegebenenfalls deaktivieren
            if (mapAddon.getAddonDependencies().contains(addon.getClass()))
                disableAddon(addon, updateConfig);
        }

        // Abfrage, ob der Config-Eintrag gesetzt werden soll
        if (updateConfig) {
            addon.getPlugin().getConfig().set("Addons." + addon.getName() + ".Enabled", false);
            addon.getPlugin().saveConfig();
        }

        // Addon deaktivieren
        addon.disable();

        // Log-Nachricht senden
        addon.getPlugin().getLogger().info(Message.INFO_ADDON_DISABLED.replace("%addon%", addon.getName()));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Init Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Lädt die Addons eines bestimmten Plugins, oder alle falls plugin Null ist
    public final void loadAddons(BanarniaPlugin plugin) {
        // For-Schleife durch alle Addons
        for (Addon addon : getAddons()) {
            // Abfrage, ob das Plugin übereinstimmt
            if (plugin != null && !addon.getPlugin().equals(plugin)) continue;

            // Abfrage, ob das Addon bereits geladen wurde
            if (addon.isLoaded()) continue;

            // Abfrage, ob das Addon überhaupt laden darf
            if (!addon.shouldLoadOnStartup()) continue;

            // Addon laden
            loadAddon(addon);
        }
    }

    // Aktiviert die Addons eines bestimmten Plugins, oder alle falls plugin Null ist
    public final void enableAddons(BanarniaPlugin plugin) {
        // For-Schleife durch alle Addons
        for (Addon addon : getAddons()) {
            // Abfrage, ob das Plugin übereinstimmt
            if (plugin != null && !addon.getPlugin().equals(plugin)) continue;

            // Abfrage, ob das Addon bereits aktiviert wurde
            if (addon.isEnabled()) continue;

            // Addon aktivieren
            enableAddon(addon, false);
        }
    }

    // Deaktiviert die Addons eines bestimmten Plugins, oder alle falls plugin Null ist
    public final void disableAddons(BanarniaPlugin plugin) {
        // For-Schleife durch alle Addons
        for (Addon addon : getAddons()) {
            // Abfrage, ob das Plugin übereinstimmt
            if (plugin != null && !addon.getPlugin().equals(plugin)) continue;

            // Abfrage, ob das Addon bereits geladen wurde
            if (!addon.isEnabled()) continue;

            // Addon deaktivieren
            disableAddon(addon, false);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Addon Informationen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Addon eines Plugins bekommen
    public final List<Addon> getAddons(BanarniaPlugin plugin) {
        // Null check
        Validate.notNull(plugin);

        // Addons zurückgeben
        return getAddons().stream()
                .filter(addon -> addon.getPlugin().equals(plugin))
                .sorted(Comparator.comparing(Addon::getName))
                .collect(Collectors.toList());
    }

    // Addons bekommen, die von einem anderen abhängig sind
    public final List<Addon> getDependendAddons(Addon dependency) {
        // Null check
        Validate.notNull(dependency);

        // Addons zurückgeben
        return getAddons().stream()
                .filter(addon -> addon.getAddonDependencies().contains(dependency.getClass()))
                .sorted(Comparator.comparing(Addon::getName))
                .collect(Collectors.toList());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Static Instanz aufrufen
    public static AddonManager getInstance() {
        if (instance == null)
            new AddonManager();

        return instance;
    }

    // Addon anhand einer Klasse zurückgeben
    public <T extends Addon> T getAddon(Class<? extends Addon> clazz) {
        // For-Schleife durch alle Addons, um die Klassen zu vergleichen
        for (Addon addon : getAddons()) {
            if (addon.getClass().equals(clazz))
                return (T) addon;
        }

        // Null zurückgeben, wenn kein Addon mit dieser Klasse gefunden wurde
        return null;
    }

    // Alle Addons zurückgeben
    public Collection<Addon> getAddons() {
        return addons.values();
    }

    // Addon-Map zurückgeben
    public HashMap<String, Addon> getAddonMap() {
        return addons;
    }
}
