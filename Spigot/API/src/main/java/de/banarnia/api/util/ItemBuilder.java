package de.banarnia.api.util;

import com.google.common.collect.Lists;
import de.banarnia.api.messages.Language;
import de.banarnia.api.skulls.SkullManager;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;

import javax.annotation.Nullable;
import java.util.List;

/* ItemBuilder Util
 * Mit dieser Klasse können Custom-Items einfach erstellt werden.
 */
public final class ItemBuilder {

    // Instanz des ItemStack
    private ItemStack item;

    // Meta-Daten des ItemStacks
    private ItemMeta meta;
    private PersistentDataContainer dataContainer;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Static Aufruf der Konstruktoren ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ItemBuilder mit einem String erstellen - kann auch ein Custom Head sein
    public static ItemBuilder of(String s) {
        return new ItemBuilder(s);
    }

    // ItemBuilder mit einem String und Default-Material erstellen - kann auch ein Custom Head sein
    public static ItemBuilder of(String s, Material def) {
        return new ItemBuilder(s, def);
    }

    // ItemBuilder mit einem Material erstellen
    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    // ItemBuilder mit einem Material und ItemFlags erstellen
    public static ItemBuilder of(Material material, ItemFlag... flags) {
        return new ItemBuilder(material, flags);
    }

    // ItemBuilder mit einem ItemStack erstellen
    public static ItemBuilder of(ItemStack item) {
        return new ItemBuilder(item);
    }

    // ItemBuilder mit einem ItemStack und ItemFlags erstellen
    public static ItemBuilder of(ItemStack item, ItemFlag... flags) {
        return new ItemBuilder(item, flags);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktoren ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /* Konstruktor für den ItemBuilder
     * Konstruktor mit einem String als Argument.
     */
    private ItemBuilder(String s) {
        this(s, null);
    }

    /* Konstruktor für den ItemBuilder
     * Konstruktor mit einem String als Argument und einem Material als Ausweich-Item.
     */
    private ItemBuilder(String s, Material def) {
        // Null check
        if (s == null || s.length() == 0)
            throw new IllegalArgumentException();

        // Variable für den ItemStack anlegen
        ItemStack item = null;

        // Abfrage, ob ein Material angegeben ist
        Material material = Material.getMaterial(s);
        if (material != null) {
            // ItemStack anlegen
            item = new ItemStack(item);

            // Initialisieren
            init(item, null);

            // Methode beenden
            return;
        }

        // Abfrage, ob der String eine Head-URL ist
        if (s.startsWith("URL:")) {
            // String in ein Array splitten, um das "URL:" von der eigentlichen URL zu trennen
            String[] splitted = s.split("URL:");

            // Abfrage, ob das Array aus mindestens 2 Strings besteht
            if (splitted.length <= 1) return;

            // Url in separate Variable sichern
            String url = splitted[1];

            // Skull bekommen
            item = SkullManager.getInstance().getSkullItem(url);

            // Initialisieren
            init(item, null);

            // Methode beenden
            return;
        }

        // Abfrage, ob der String ein Skull ist
        if (s.startsWith("Skull:")) {
            // String in ein Array splitten, um das "URL:" von der eigentlichen URL zu trennen
            String[] splitted = s.split("Skull:");

            // Abfrage, ob das Array aus mindestens 2 Strings besteht
            if (splitted.length <= 1) return;

            // Url in separate Variable sichern
            String skullName = splitted[1];

            // Skull bekommen
            item = SkullManager.getInstance().getSkullItem(skullName);

            // Initialisieren
            init(item, null);

            // Methode beenden
            return;
        }

        // Abfrage, ob ein Ausweich Material genommen werden soll
        if (def != null) {
            init(new ItemStack(def));

            // Methode beenden
            return;
        }

        // Error throwen, wenn nichts klappt
        throw new IllegalArgumentException("Could not create ItemStack with input: " + s);
    }

    /* Konstruktor für den ItemBuilder
     * Konstruktor mit einem Material als Argument.
     */
    private ItemBuilder(Material material) {
        this(material, null);
    }

    /* Konstruktor für den ItemBuilder
     * Konstruktor mit einem Material und Flags als Argument.
     */
    private ItemBuilder(Material material, ItemFlag... flags) {
        this(new ItemStack(material), flags);
    }

    /* Konstruktor für den ItemBuilder
     * Konstruktor mit einem ItemStack als Argument.
     */
    private ItemBuilder(ItemStack item) {
        this(item, null);
    }

    /* Konstruktor für den ItemBuilder
     * Hauptkonstruktor, der die Meta-Daten Instanzen anlegt und Flags setzt.
     */
    private ItemBuilder(ItemStack item, ItemFlag... flags) {
        // Initialisierung
        init(item, flags);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Init ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /* Init Methode
     * Initialisiert die Werte des Items nach Aufruf des Konstruktors.
     */
    private void init(ItemStack item, ItemFlag... flags) {
        // Null check
        if (item == null)
            throw new IllegalArgumentException();

        // Instanz setzen
        this.item = item;

        // Abfrage, ob Item bereits Meta-Daten hat
        if (item.hasItemMeta()) {
            this.meta           = item.getItemMeta();
            this.dataContainer  = meta.getPersistentDataContainer();
        }

        // Flags setzen
        addItemFlag(flags);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Build ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Erstellt das Item
    public ItemStack build() {
        // Meta zuweisen
        this.item.setItemMeta(meta);

        // Item zurückgeben
        return item;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Name ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // DisplayName des Items ändern
    public ItemBuilder name(String name) {
        // Null check
        if (name == null)
            throw new IllegalArgumentException();

        // Abfrage, ob ItemMeta existiert
        if (meta == null) {
            // Warnung in der Konsole ausgeben
            warnMetaNotExisting();

            // Builder zurückgeben
            return this;
        }

        // DisplayName setzen
        meta.setDisplayName(name);

        // Builder zurückgeben
        return this;
    }

    /* Nur mit Paper-API
    // DisplayName des Items ändern - mit BaseComponents
    public ItemBuilder name(BaseComponent... components) {
        // Null check
        if (components == null)
            throw new IllegalArgumentException();

        // Abfrage, ob ItemMeta existiert
        if (meta == null) {
            warnMetaNotExisting();
            return this;
        }

        // DisplayName setzen
        meta.setDisplayNameComponent(components);

        // Builder zurückgeben
        return this;
    }
    */

    // Farbe des ItemNamens ändern
    public ItemBuilder colorName(ChatColor color) {
        // Null check
        if (color == null)
            throw new IllegalArgumentException();

        // Aktuellen Namen bekommen - kann auch Übersetzung des Materials sein
        String displayName      = getDisplayName();
        String newDisplayName   = color + displayName;

        // Name neu setzen und zurückgeben
        return this.name(newDisplayName);
    }

    // Namen des Items bekommen - entweder Übersetzung des Material oder Custom DisplayName
    public String getDisplayName() {
        // Abfrage, ob ItemMeta existiert und ob DisplayName gesetzt ist
        if (meta != null && meta.hasDisplayName())
            return meta.getDisplayName();

        // Übersetzung zurückgeben, wenn kein DisplayName gesetzt
        return Language.getName(item.getType());
    }

    // Namen zurücksetzen
    public ItemBuilder resetName() {
        // Abfrage, ob ItemMeta existiert
        if (meta == null) {
            // Warnung in der Konsole ausgeben
            warnMetaNotExisting();

            // Builder zurückgeben
            return this;
        }

        // Namen zurücksetzen
        meta.setDisplayName(null);

        // Builder zurückgeben
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Lore ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Lore bekommen
    public List<String> getLore() {
        // Lore zurückgeben, wenn sie existiert - sonst leere Liste
        return meta != null && meta.hasLore() ? meta.getLore() : Lists.newArrayList();
    }

    // Zeilen zur Lore hinzufügen
    public ItemBuilder lore(List<String> lore) {
        // Null check, wenn null wird leere Zeile hinzugefügt
        if (lore == null || lore.isEmpty())
            this.lore(" ");

        // Liste zu Array umwandeln
        String[] loreArray = lore.toArray(new String[lore.size()]);

        // Lore hinzufügen und Builder zurückgeben
        return this.lore(loreArray);
    }

    // Zeilen zur Lore hinzufügen - Null wird zu leerer Zeile
    public ItemBuilder lore(String... lore) {
        // Aktuelle lore bekommen
        List<String> newLore = getLore();

        // Zeilen zur Lore hinzufügen
        for (String line : lore)
            newLore.add(line == null ? " " : ChatColor.GRAY + line);

        // Neue Lore setzen
        meta.setLore(newLore);

        // Builder zurückgeben
        return this;
    }

    // Lore leeren
    public ItemBuilder clearLore() {
        // Abfrage, ob ItemMeta existiert
        if (meta == null) {
            // Warnung in der Konsole ausgeben
            warnMetaNotExisting();

            // Builder zurückgeben
            return this;
        }

        // Lore setzen mit leerer Liste
        meta.setLore(Lists.newArrayList());

        // Builder zurücksetzen
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Enchantments ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Enchantments entfernen
    public ItemBuilder clearEnchantments() {
        // Abfrage, ob ItemMeta existiert
        if (meta == null) {
            // Warnung in der Konsole ausgeben
            warnMetaNotExisting();

            // Builder zurückgeben
            return this;
        }

        // Enchantments entfernen
        for (Enchantment enchantment : meta.getEnchants().keySet())
            meta.removeEnchant(enchantment);

        // Builder zurückgeben
        return this;
    }

    // Enchantment hinzufügen - mit weniger Parametern
    public ItemBuilder addEnchantment(Enchantment enchantment) {
        return addEnchantment(enchantment, 1, false);
    }

    // Enchantment hinzufügen
    public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        // Abfrage, ob ItemMeta existiert
        if (meta == null) {
            // Warnung in der Konsole ausgeben
            warnMetaNotExisting();

            // Builder zurückgeben
            return this;
        }

        // Enchantment hinzufügen
        meta.addEnchant(enchantment, level, ignoreLevelRestriction);

        // Builder zurückgeben
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Attribute & Flags ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ItemBuilder addItemFlag(ItemFlag... flags) {
        // Null check
        if (flags == null)
            return this;

        // Länge prüfen
        if (flags.length == 0)
            return this;

        // Alle Flags hinzufügen
        for (ItemFlag flag : flags) {
            // Null check, damit bei einer Null-Flag kein Fehler geworfen wird
            if (flag == null) {
                Bukkit.getLogger().warning("Failed to apply an ItemFlag: Null.");
                continue;
            }

            // Flag hinzufügen
            meta.addItemFlags(flag);
        }

        // Builder zurückgeben
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag... flags) {
        // Null check
        if (flags == null)
            throw new IllegalArgumentException();

        // Abfrage, ob das Item eine ItemMeta hat
        if (meta == null) {
            Bukkit.getLogger().warning("Failed to ");
            return this;
        }

        // Länge prüfen
        if (flags.length == 0)
            return this;

        // Alle Flags hinzufügen
        for (ItemFlag flag : flags) {
            // Null check, damit bei einer Null-Flag kein Fehler geworfen wird
            if (flag == null) {
                Bukkit.getLogger().warning("Failed to apply an ItemFlag: Null.");
                continue;
            }

            // Flag entfernen
            meta.removeItemFlags(flag);
        }

        // Builder zurückgeben
        return this;
    }

    public ItemBuilder hideEnchants() {
        // Flag setzen
        return addItemFlag(ItemFlag.HIDE_ENCHANTS);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Kopf-Skins ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Skull zu Spielerskin ändern
    public ItemBuilder setSkullOwner(@Nullable OfflinePlayer player) {
        // Abfrage, ob das Item ein Skull ist
        Validate.isTrue(item.getType() == Material.PLAYER_HEAD,
                "You can only change the Skull-Owner of skulls.");

        // SkullMeta abrufen
        SkullMeta skullMeta = (SkullMeta) this.meta;

        // Owner ändern
        skullMeta.setOwningPlayer(player);

        // Builder zurückgeben
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Fehlermeldungen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ItemMeta existiert nicht
    private void warnMetaNotExisting() {
        Bukkit.getLogger().warning("Failed to modify ItemMeta - ItemMeta is null");
    }

}
