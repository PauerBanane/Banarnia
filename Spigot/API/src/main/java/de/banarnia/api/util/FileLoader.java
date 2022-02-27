package de.banarnia.api.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.banarnia.api.messages.Message;
import de.banarnia.lib.util.AbstractFileLoader;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/* FileLoader
 * Klasse zum einfachen Bearbeiten von YAML-Dateien.
 */
public class FileLoader extends AbstractFileLoader {

    private YamlConfiguration config;   // Instanz der YamlConfiguration
    private boolean loaded;             // Config erfolgreich geladen

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Static Aufruf des FileLoader ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // FileLoader mit Order-Pfad und Dateinamen erstellen
    public static FileLoader of(String path, String fileName) {
        return new FileLoader(path, fileName);
    }

    // FileLoader mit Ordner und Dateinamen erstellen
    public static FileLoader of(File directory, String fileName) {
        return new FileLoader(directory, fileName);
    }

    // FileLoader mit Ordner und Datei erstellen
    public static FileLoader of(File directory, File file) {
        return new FileLoader(directory, file);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktoren ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected FileLoader(String directoryPath, String fileName) {
        super(directoryPath, fileName);
    }

    protected FileLoader(File directory, String fileName) {
        super(directory, fileName);
    }

    protected FileLoader(File directory, File file) {
        super(directory, file);
    }

    // Konstruktor wird in der Super-Klasse AbstractFileLoader erstellt und ruft nur die #initConfig auf
    @Override
    protected boolean initConfig() {
        // Null check
        if (file == null) {
            Bukkit.getLogger().severe(Message.ERROR_CONFIG_FILE_IS_NULL.get());
            return false;
        }

        // YamlConfiguration laden
        this.config = YamlConfiguration.loadConfiguration(file);

        // True zurückgeben, wenn alles geklappt hat
        return true;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Datei Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Speichert eine Datei
    public FileLoader save() {
        // Null checks
        if (file == null || config == null)
            throw new IllegalArgumentException();

        // Datei speichern
        try {
            config.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            Bukkit.getLogger().severe("Fehler beim Speichern der Datei: "  + this.path);
        }

        // Datei neu laden
        return reload();
    }

    // Lädt eine Datei neu
    public FileLoader reload() {
        // Null check
        if (path == null)
            throw new IllegalArgumentException();

        // File erstellen
        this.file = new File(this.path);

        // Null check
        if (file == null)
            throw new NullPointerException();

        // YamlConfiguration erstellen
        this.config = YamlConfiguration.loadConfiguration(this.file);

        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Werte schreiben  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Abfrage, ob ein Key in der Config existiert.
    public boolean isSet(String key) {
        return config.isSet(key);
    }

    // Abfrage, ob eine ConfigurationSection in der Config existiert.
    public boolean hasConfigurationSection(String path) {
        return getConfigurationSection(path) != null;
    }

    /* Liest den Wert von key aus der Config.
     * Sollte der key nicht existieren, wird ein Default-Wert def gesetzt und zurückgegeben.
     */
    public <T> T getOrElseSet(String key, T def) {
        // Abfrage, ob der Key in der Config existiert
        if (isSet(key))
            return (T) config.get(key);     // Wert aus der Config zurückgeben

        // Default-Wert setzen, falls kein Key vorhanden, und Config anschließend speichern
        config.set(key, def);
        save();

        return def;
    }

    /* Schreibt einen Wert in die Config.
     * Die Config muss anschließend separat gespeichert werden,
     * wenn der Wert persistent gesetzt sein soll.
     */
    public FileLoader set(String key, Object value) {
        config.set(key, value);
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Werte lesen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Liest eine ConfigurationSection aus der Config.
    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    /* Liest die Keys aus einer Config.
     * Wenn deep == true ist, werden auch die unterlagerten Keys zurückgegeben.
     */
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    /* Liest die Keys aus einer Config.
     * Wenn deep == true ist, werden auch die unterlagerten Keys zurückgegeben.
     */
    public Set<String> getKeys(String path, boolean deep) {
        if (!hasConfigurationSection(path))
            return Sets.newHashSet();

        return getConfigurationSection(path).getKeys(deep);
    }

    /* Liest einen Boolean aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public Boolean getBoolean(String key, boolean def) {
        return config.getBoolean(key, def);
    }
    /* Liest einen Boolean aus der Config.
     * Gibt den Wert false zurück, falls der Key nicht vorhanden ist.
     */
    public Boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /* Liest einen String aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public String getString(String key, String def) {
        return config.getString(key, def);
    }
    /* Liest einen String aus der Config.
     * Gibt den Wert null zurück, falls der Key nicht vorhanden ist.
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /* Liest eine List<String> aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public List<String> getStringList(String key, List<String> def) {
        // Abfrage, ob der Key existiert
        if (isSet(key))
            return config.getStringList(key);

        // Default-Wert zurückgeben, falls der Wert nicht existiert.
        return def;
    }
    /* Liest eine List<String> aus der Config.
     * Gibt eine leere ArrayList<String> zurück, falls der Key nicht vorhanden ist.
     */
    public List<String> getStringList(String key) {
        return getStringList(key, Lists.newArrayList());
    }

    /* Liest einen int aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public int getInt(String key, int def) {
        return config.getInt(key, def);
    }
    /* Liest einen int aus der Config.
     * Gibt den Wert 0 zurück, falls der Key nicht vorhanden ist.
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /* Liest einen long aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public long getLong(String key, long def) {
        return config.getLong(key, def);
    }
    /* Liest einen long aus der Config.
     * Gibt den Wert 0 zurück, falls der Key nicht vorhanden ist.
     */
    public long getLong(String key) {
        return getLong(key, 0);
    }

    /* Liest einen double aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public double getDouble(String key, double def) {
        return config.getDouble(key, def);
    }
    /* Liest einen double aus der Config.
     * Gibt den Wert 0 zurück, falls der Key nicht vorhanden ist.
     */
    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    /* Liest einen ItemStack aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public ItemStack getItemStack(String key, ItemStack def) {
        return config.getItemStack(key, def);
    }
    /* Liest einen ItemStack aus der Config.
     * Gibt den Wert null zurück, falls der Key nicht vorhanden ist.
     */
    public ItemStack getItemStack(String key) {
        return getItemStack(key, null);
    }

    /* Liest eine Location aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public Location getLocation(String key, Location def) {
        return config.getLocation(key, def);
    }
    /* Liest eine Location aus der Config.
     * Gibt den Wert null zurück, falls der Key nicht vorhanden ist.
     */
    public Location getLocation(String key) {
        return getLocation(key, null);
    }

    /* Liest einen Serializable aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     *
     * Serializable Werte müssen im Voraus registriert werden:
     * ConfigurationSerialization.registerClass();
     */
    public <T extends ConfigurationSerializable> T getSerializable(String key, Class<T> clazz, T def) {
        return config.getSerializable(key, clazz, def);
    }
    /* Liest einen Serializable aus der Config.
     * Gibt den Wert null zurück, falls der Key nicht vorhanden ist.
     *
     * Serializable Werte müssen im Voraus registriert werden:
     * ConfigurationSerialization.registerClass();
     */
    public <T extends ConfigurationSerializable> T getSerializable(String key, Class<T> clazz) {
        return getSerializable(key, clazz, null);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public File getDirectory() {
        return directory;
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
