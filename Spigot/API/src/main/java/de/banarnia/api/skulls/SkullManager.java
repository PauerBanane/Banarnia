package de.banarnia.api.skulls;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.banarnia.api.BanarniaAPI;
import de.banarnia.api.skulls.commands.SkullCommand;
import de.banarnia.api.util.FileLoader;
import de.banarnia.api.util.UtilItem;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/* SkullManager Klasse
 * Steuert alle Skulls die verwendet werden können.
 */
public class SkullManager {

    // Singleton Instanz
    private static SkullManager instance;

    // Übergeordnetes Plugin
    private BanarniaAPI plugin;

    // Datei, in der die Skull-URLs gespeichert werden
    private FileLoader config;

    // Section in der die Skull-URLs sind
    private final String SECTION = "Skulls";

    // Map für alle Skulls
    private HashMap<String, Skull> skullMap;

    // Map für alle Kategorien
    private HashMap<String, SkullCategory> categoryMap;

    // Section in der die Skull-Kategorien sind
    private final String CATEGORY_SECTION = "Categories";

    // Name der Default-Kategorie
    private final String DEFAULT_CATEGORY_NAME = "default";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private SkullManager(BanarniaAPI plugin) {
        // Singleton Instanz setzen
        instance = this;

        // Plugin Instanz setzen
        this.plugin = plugin;

        // Map initialisieren
        skullMap    = Maps.newHashMap();
        categoryMap = Maps.newHashMap();

        // Datei laden
        config = FileLoader.of(plugin.getDataFolder(), "Skulls.yml");

        // Config initialisieren
        init();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Init Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Init zum Start des Plugins - Lädt Kategorien aus der Config
    private void init() {
        // Null check
        if (config == null)
            throw new NullPointerException();

        // Kategorien laden
        loadCategories();

        // Skulls laden
        loadSkulls();
    }

    // Command registrieren
    public void registerCommand() {
        plugin.registerCommand(new SkullCommand());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Skulls ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Abfrage, ob ein Skull registriert ist - Anhand des Objekts
    public boolean isRegistered(Skull skull) {
        // Null check
        if (skull == null)
            throw new IllegalArgumentException();

        // Nächste Methode aufrufen
        return isRegistered(skull.getName());
    }

    // Abfrage, ob ein Skull registriert ist - Anhand des Namens
    public boolean isRegistered(String skullName) {
        // Null check
        if (skullName == null || skullName.isEmpty())
            throw new IllegalArgumentException();

        // Zurückgeben, ob der Name in der Map ist
        return skullMap.containsKey(skullName);
    }

    // Skull registrieren
    public boolean register(Skull skull) {
        // Null check
        if (skull == null || skull.getName() == null)
            return false;

        // Abfrage, ob der Skull schon registriert wurde
        if (isRegistered(skull))
            return false;

        // Skull registrieren
        skullMap.put(skull.getName(), skull);

        // Skull speichern
        saveSkull(skull);

        // True zurückgeben, wenn alles geklappt hat
        return true;
    }

    // Skull bekommen
    public Skull getSkull(String name) {
        // Null check
        if (name == null) return null;

        // Skull zurückgeben
        return skullMap.get(name);
    }

    // Skull löschen - mit Namen
    public boolean delete(String skullName) {
        return delete(getSkull(skullName));
    }

    // Skull löschen - mit Objekt
    public boolean delete(Skull skull) {
        // Null check
        if (skull == null || skull.getName() == null)
            return false;

        // Abfrage, ob der Skull ein Standard-Skull aus der 'Skulls.class' enum ist - kann dann nicht gelöscht werden
        if (skull.isDefaultSkull())
            return false;

        // Namen aufrufen
        String name = skull.getName();

        // Skull aus der Map löschen
        skullMap.remove(name);

        // Skull aus der Config löschen
        config.set(SECTION + "." + name, null).save();

        // True zurückgeben, wenn alles geklappt hat
        return true;
    }

    // Skulls aus der Config lesen
    public void loadSkulls() {
        // Null check
        if (config == null)
            throw new NullPointerException();

        // Abfrage, ob Einträge in der Config stehen
        if (config.isSet(SECTION)) {
            // ConfigurationSection anlegen
            ConfigurationSection section = config.getConfigurationSection(SECTION);

            // Schleife durch alle Einträge
            for (String name : section.getKeys(false)) {
                // Daten auslesen
                String displayName  = section.getString(name + ".DisplayName", name.replace("_", " "));
                String url          = section.getString(name + ".URL");
                String category     = section.getString(name + ".Category", DEFAULT_CATEGORY_NAME);
                boolean forSale     = section.getBoolean(name + ".ForSale", false);
                double price        = section.getDouble(name + ".Price", Double.MAX_VALUE);

                // Skull anlegen
                Skull skull = new Skull(name, displayName, url, category, forSale, price);

                // Skull registrieren
                register(skull);
            }
        }

        // Standard-Skulls auslesen
        for (Skulls skullEnum : Skulls.values()) {
            // Namen des Skulls anlegen
            String name         = skullEnum.toString();

            // Abfrage, ob der Name bereits registriert wurde
            if (isRegistered(name))
                continue;

            // Daten auslesen
            String displayName  = config.getOrElseSet(SECTION + "." + name + ".DisplayName", name.replace("_", " "));
            String url          = config.getOrElseSet(SECTION + "." + name + ".URL", skullEnum.getDefaultUrl());
            String category     = config.getOrElseSet(SECTION + "." + name + ".Category", DEFAULT_CATEGORY_NAME);
            boolean forSale     = config.getOrElseSet(SECTION + "." + name + ".ForSale", false);
            double price        = config.getOrElseSet(SECTION + "." + name + ".Price", Double.MAX_VALUE);

            // Skull anlegen
            Skull skull = new Skull(name, displayName, url, category, forSale, price);

            // Skull registrieren
            register(skull);
        }
    }

    // Alle Skulls speichern
    public void saveSkulls() {
        skullMap.values().forEach(skull -> saveSkull(skull));
    }

    // Skull speichern
    public void saveSkull(Skull skull) {
        // Null check
        if (skull == null || skull.getName() == null)
            throw new IllegalArgumentException();

        // Abfrage, ob der Skull registriert ist
        if (!isRegistered(skull))
            return;

        // Skull Daten abrufen
        String name         = skull.getName();
        String displayName  = skull.getDisplayName();
        String category     = skull.getCategoryName();
        boolean forSale     = skull.isForSale();
        double price        = skull.getPrice();

        // Section des Skulls
        String section = SECTION + "." + name;

        // Skull speichern
        config.set(section, null);
        config.set(section + ".DisplayName", displayName);
        config.set(section + ".Category", category);
        config.set(section + ".forSale", forSale);
        config.set(section + ".Price", price);
        config.save();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Kategorien ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



    // Kategorien laden
    private void loadCategories() {
        // Null check
        if (config == null)
            throw new NullPointerException();

        // Abfrage, ob Kategorien in der Config stehen
        if (!config.isSet(CATEGORY_SECTION))
            return;

        // ConfigurationSection anlegen
        ConfigurationSection section = config.getConfigurationSection(CATEGORY_SECTION);

        // Kategorien aus der Config lesen
        for (String categoryName : section.getKeys(false)) {
            // Daten auslesen
            String displayName          = section.getString(categoryName + ".DisplayName");
            String icon                 = section.getString(categoryName + ".Icon");
            boolean requiresPermission  = section.getBoolean(categoryName + ".RequiresPermission");

            // Kategorie erstellen
            SkullCategory category = new SkullCategory(categoryName, displayName, icon, requiresPermission);

            // Kategorie registrieren
            registerCategory(category);
        }

        // Abfrage, ob die Default Kategorie schon geladen wurde
        if (isCategoryRegistered(DEFAULT_CATEGORY_NAME))
            return;

        // Standard-Kategorie laden
        String name                 = DEFAULT_CATEGORY_NAME;
        String displayName          = config.getOrElseSet(SECTION + "." + name + ".DisplayName","§8Sonstige");
        String icon                 = config.getOrElseSet(SECTION + "." + name + ".Icon",       "URL:" + Skulls.Erde_Kiste.getDefaultUrl());
        boolean requiresPermission  = config.getOrElseSet(SECTION + "." + name + ".RequiresPermission", false);

        // Kategorie erstellen
        SkullCategory defaultCategory = new SkullCategory(name, displayName, icon, requiresPermission);

        // Kategorie registrieren
        registerCategory(defaultCategory);
    }

    // Abfrage, ob die Kategorie schon existiert - mit Objekt
    public boolean isCategoryRegistered(SkullCategory category) {
        // Null check
        if (category == null || category.getName() == null)
            throw new IllegalArgumentException();

        // Nächste Methode aufrufen
        return isCategoryRegistered(category.getName());
    }

    // Abfrage, ob die Kategorie schon existiert - mit Namen
    public boolean isCategoryRegistered(String categoryName) {
        // Null check
        if (categoryName == null || categoryName.isEmpty())
            throw new IllegalArgumentException();

        // Zurückgeben, ob der Wert in der Map ist
        return categoryMap.containsKey(categoryName);
    }

    // Kategorie anhand des Namens bekommen
    public SkullCategory getCategory(String categoryName, boolean defaultIfNotExists) {
        // Null check
        if (categoryName == null || categoryName.isEmpty())
            return null;

        // Wert aus der Map bekommen
        SkullCategory category = categoryMap.get(categoryName);

        // Wenn die Kategorie != null ist wird sie zurückgegeben
        if (category != null)
            return category;

        // Null zurückgeben, wenn alternativ nicht die Standardkategorie gewünscht ist
        if (!defaultIfNotExists)
            return null;

        // Standard-Kategorie bekommen
        SkullCategory defaultCategory = getCategory(DEFAULT_CATEGORY_NAME, false);

        // Standard-Kategorie wiedergeben, wenn andere nicht existiert
        if (defaultCategory != null)
            return defaultCategory;
        else
            throw new IllegalStateException("Default Skull-Category does not exist.");
    }

    // Kategorie registrieren
    public boolean registerCategory(SkullCategory category) {
        // Null check
        if (category == null || category.getName() == null)
            throw new IllegalArgumentException();

        // Abfrage, ob Kategorie bereits registriert ist
        if (isCategoryRegistered(category))
            return false;

        // Kategorie in Map eintragen
        categoryMap.put(category.getName(), category);

        // Kategorie speichern
        saveCategory(category);

        // True zurückgeben, wenn alles geklappt hat
        return true;
    }

    // Kategorie löschen
    public boolean deleteCategory(String categoryName) {
        // Null check
        if (categoryName == null || categoryName.isEmpty())
            return false;

        // Kategorie erhalten
        SkullCategory category = getCategory(categoryName, false);

        // Abfrage, ob Category existiert
        if (category == null)
            return false;

        // Nächste Methode aufrufen
        return deleteCategory(category);
    }

    // Kategorie löschen
    public boolean deleteCategory(SkullCategory category) {
        // Null check
        if (category == null || category.getName() == null)
            throw new IllegalArgumentException();

        // Abfrage, ob es die Default-Kategorie ist
        if (category.getName().equalsIgnoreCase(DEFAULT_CATEGORY_NAME))
            return false;

        // Kategorie aus Map entfernen
        categoryMap.remove(category.getName());

        // Kategorie aus der Config löschen
        config.set(CATEGORY_SECTION + "." + category.getName(), null).save();

        // True zurückgeben, wenn alles geklappt hat
        return true;
    }

    // Kategorie speichern
    public void saveCategory(SkullCategory category) {
        // Null check
        if (category == null)
            throw new IllegalArgumentException();

        // Abfrage, ob Kategorie registriert ist
        if (!isCategoryRegistered(category))
            return;

        // Section der Kategorie
        String SAVE_SECTION = CATEGORY_SECTION + "." + category.getName();

        // Alte Daten löschen
        config.set(SAVE_SECTION, null);
        config.set(SAVE_SECTION + ".DisplayName", category.getDisplayName());
        config.set(SAVE_SECTION + ".Icon", category.getIcon());
        config.set(SAVE_SECTION + ".RequiresPermission", category.requiresPermission());

        // Config speichern
        config.save();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Alle Kategorien bekommen
    public HashMap<String, SkullCategory> getCategoryMap() {
        return categoryMap;
    }

    // Alle Skulls bekommen
    public HashMap<String, Skull> getSkullMap() {
        return skullMap;
    }

    // Alle sichtbaren Kategorien bekommen
    public List<SkullCategory> getCategories(Player player) {
        return getCategoryMap().values().stream()
                .filter(category -> category.canView(player))
                .sorted(Comparator.comparing(SkullCategory::getDisplayName))
                .collect(Collectors.toList());
    }

    // Alle Skulls einer Kategorie bekommen
    public List<Skull> getSkulls(SkullCategory category) {
        // Null check
        if (category == null)
            return Lists.newArrayList();

        // Liste zurückgeben
        return getSkullMap().values().stream()
                .filter(skull -> category.equals(skull.getCategory()))
                .sorted(Comparator.comparing(Skull::getDisplayName))
                .collect(Collectors.toList());
    }

    // Alle sichtbaren Skulls bekommen
    public List<Skull> getSkulls(Player player, SkullCategory category) {
        // Null check
        if (player == null || category == null)
            return Lists.newArrayList();

        // Permission Abfrage
        if (!category.canView(player))
            return Lists.newArrayList();

        // Skulls zurückbekommen
        return getSkulls(category).stream()
                .filter(skull -> skull.canBuy(player))
                .sorted(Comparator.comparing(Skull::getDisplayName))
                .collect(Collectors.toList());
    }

    // Skull Item bekommen - mit Namen
    public ItemStack getSkullItem(String skullName) {
        // Skull bekommen
        Skull skull = getSkull(skullName);

        // Null check
        if (skull == null) {
            Bukkit.getLogger().warning("Failed to create Skull by Skull-Name: " + skullName);
            return null;
        }

        // Nächste Methode aufrufen
        return getSkullItem(skull);
    }

    // Skull Item bekommen - mit Objekt
    public ItemStack getSkullItem(Skull skull) {
        // Null check
        if (skull == null)
            return null;

        // URL aufrufen
        String url = skull.getUrl();

        // Abfrage, ob URL richtig ist
        if (url == null || url.isEmpty()) {
            Bukkit.getLogger().warning("Failed to create Skull with invalid URL: " + skull.getName());
            return null;
        }

        // ItemStack zurückgeben
        return UtilItem.getSkull(url);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Getter für die Singleton-Instanz
    public static SkullManager getInstance() {
        // Abfrage, ob bereits Instanziiert wurde
        if (instance == null)
            new SkullManager(BanarniaAPI.getInstance());

        // Instanz zurückgeben
        return instance;
    }

}
