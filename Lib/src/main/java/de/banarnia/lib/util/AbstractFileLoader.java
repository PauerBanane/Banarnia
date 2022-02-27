package de.banarnia.lib.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* AbstractFileLoader
 * Abstrakte Klasse zum einfachen Bearbeiten von YAML-Dateien.
 */
public abstract class AbstractFileLoader {

    protected String  path;               // Pfad zu der Datei
    protected File    directory;          // Instanz des Ordners
    protected File    file;               // Instanz der Datei
    protected boolean loaded;             // Config erfolgreich geladen

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktoren ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /* Konstruktor für den FileLoader.
     * Zusätzlich, um eine Datei über den Ordner als String zu erstellen.
     */
    protected AbstractFileLoader(String directoryPath, String fileName) {
        this(new File(directoryPath), fileName);
    }

    /* Konstruktor für den FileLoader.
     * Zusätzlich. um eine Datei über einen File als Ordner und String als Dateinamen zu erstellen.
     */
    protected AbstractFileLoader(File directory, String fileName) {
        this(directory, new File(directory, fileName));
    }

    /* Konstruktor für den FileLoader.
     * Hauptkonstruktor, welcher die Dateien erstellt und die YamlConfiguration lädt.
     */
    protected AbstractFileLoader(File directory, File file) {
        // Null checks
       if (directory == null || file == null)
           throw new NullPointerException();

        // Instanzen erstellen
        this.path = file.getPath();
        this.directory = directory;
        this.file = file;

        // Datei erstellen, falls diese noch nicht existiert
        if (!file.exists()) {
            // Ordner erstellen
            directory.mkdirs();

            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // YamlConfiguration laden
        initConfig();

        // Instanz als erfolgreich geladen markieren
        this.loaded = file != null;
    }

    protected abstract boolean initConfig();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Datei Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Speichert eine Datei
    public abstract AbstractFileLoader save();

    // Lädt eine Datei neu
    public abstract AbstractFileLoader reload();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Werte schreiben  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Abfrage, ob ein Key in der Config existiert.
    public abstract boolean isSet(String key);

    // Abfrage, ob eine ConfigurationSection in der Config existiert.
    public abstract boolean hasConfigurationSection(String path);

    /* Liest den Wert von key aus der Config.
     * Sollte der key nicht existieren, wird ein Default-Wert def gesetzt und zurückgegeben.
     */
    public abstract <T> T getOrElseSet(String key, T def);

    /* Schreibt einen Wert in die Config.
     * Die Config muss anschließend separat gespeichert werden,
     * wenn der Wert persistent gesetzt sein soll.
     */
    public abstract AbstractFileLoader set(String key, Object value);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Werte lesen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /* Liest die Keys aus einer Config.
     * Wenn deep == true ist, werden auch die unterlagerten Keys zurückgegeben.
     */
    public abstract Set<String> getKeys(boolean deep);

    /* Liest die Keys aus einer Config.
     * Wenn deep == true ist, werden auch die unterlagerten Keys zurückgegeben.
     */
    public abstract Set<String> getKeys(String path, boolean deep);

    /* Liest einen Boolean aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public abstract Boolean getBoolean(String key, boolean def);
    /* Liest einen Boolean aus der Config.
     * Gibt den Wert false zurück, falls der Key nicht vorhanden ist.
     */
    public Boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /* Liest einen String aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public abstract String getString(String key, String def);
    /* Liest einen String aus der Config.
     * Gibt den Wert null zurück, falls der Key nicht vorhanden ist.
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /* Liest eine List<String> aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public abstract List<String> getStringList(String key, List<String> def);
    /* Liest eine List<String> aus der Config.
     * Gibt eine leere ArrayList<String> zurück, falls der Key nicht vorhanden ist.
     */
    public List<String> getStringList(String key) {
        return getStringList(key, new ArrayList<>());
    }

    /* Liest einen int aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public abstract int getInt(String key, int def);
    /* Liest einen int aus der Config.
     * Gibt den Wert 0 zurück, falls der Key nicht vorhanden ist.
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /* Liest einen long aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public abstract long getLong(String key, long def);
    /* Liest einen long aus der Config.
     * Gibt den Wert 0 zurück, falls der Key nicht vorhanden ist.
     */
    public long getLong(String key) {
        return getLong(key, 0);
    }

    /* Liest einen double aus der Config.
     * Gibt den Wert von def zurück, falls der Key nicht vorhanden ist.
     */
    public abstract double getDouble(String key, double def);
    /* Liest einen double aus der Config.
     * Gibt den Wert 0 zurück, falls der Key nicht vorhanden ist.
     */
    public double getDouble(String key) {
        return getDouble(key, 0);
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

}
