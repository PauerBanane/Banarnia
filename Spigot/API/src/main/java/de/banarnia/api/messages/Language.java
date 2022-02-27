package de.banarnia.api.messages;

import de.banarnia.api.BanarniaAPI;
import de.banarnia.api.util.FileLoader;
import org.apache.commons.lang.Validate;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.Month;

public class Language {

    // Static Instanz
    private static Language instance;

    // Instanzen
    private BanarniaAPI plugin;
    private FileLoader config;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private Language(BanarniaAPI plugin) {
        // Instanzen deklarieren
        instance = this;
        config = FileLoader.of(plugin.getDataFolder(), "Language.yml");
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // GameMode
    public static String getName(GameMode gameMode) {
        // Null check
        if (gameMode == null)
            throw new IllegalArgumentException();

        return instance.config.getOrElseSet("GameMode." + gameMode.toString(), gameMode.name());
    }

    // Material
    public static String getName(Material material) {
        // Null check
        if (material == null)
            throw new IllegalArgumentException();

        return instance.config.getOrElseSet("Material." + material.toString(), material.name());
    }

    // ItemStack
    public static String getName(ItemStack item) {
        // Null check
        if (item == null)
            throw new IllegalArgumentException();

        // Abfrage, ob der ItemStack ein DisplayName hat
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
            return item.getItemMeta().getDisplayName();

        // Name des Material zurückgeben, wenn das Item keinen DisplayName hat
        return getName(item.getType());
    }

    // Enchantment
    public static final String getName(Enchantment enchantment) {
        // Null check
        if (enchantment == null)
            throw new IllegalArgumentException();

        return instance.config.getOrElseSet("Enchantment." + enchantment.getKey().getKey(),
                                                                 enchantment.getKey().getKey());
    }

    // EntityType
    public static String getName(EntityType entityType) {
        // Null check
        if (entityType == null)
            throw new IllegalArgumentException();

        return instance.config.getOrElseSet("EntityType." + entityType.toString(), entityType.name());
    }

    // PotionEffectType
    public static String getName(PotionEffectType potionEffectType) {
        // Null check
        if (potionEffectType == null)
            throw new IllegalArgumentException();

        return instance.config.getOrElseSet("PotionEffectType." + potionEffectType.getName(),
                                                                      potionEffectType.getName());
    }

    // Villager Profession
    public static String getName(Villager.Profession profession) {
        // Null check
        if (profession == null)
            throw new IllegalArgumentException();

        return instance.config.getOrElseSet("VillagerProfession." + profession.toString(), profession.name());
    }

    // DayOfWeek
    public static String getName(DayOfWeek day) {
        // Null check
        if (day == null)
            throw new IllegalArgumentException();

        return instance.config.getOrElseSet("Day." + day.name(), day.name());
    }

    // Month
    public static String getName(Month month) {
        // Null check
        if (month == null)
            throw new IllegalArgumentException();

        return instance.config.getOrElseSet("Month." + month.name(), month.name());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Gibt die Instanz der Klasse zurück
    public static Language getInstance() {
        // Abfrage, ob die Klasse bereits instanziiert wurde
        if (instance == null)
            new Language(BanarniaAPI.getInstance());

        // Instanz zurückgeben
        return instance;
    }
}
