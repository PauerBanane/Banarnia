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

    public static String getName(GameMode gameMode) {
        // Null check
        Validate.notNull(gameMode);

        return instance.config.getOrElseSet("GameMode." + gameMode.toString(), gameMode.name());
    }

    public static String getName(Material material) {
        // Null check
        Validate.notNull(material);

        return instance.config.getOrElseSet("Material." + material.toString(), material.name());
    }

    public static String getName(ItemStack item) {
        // Null check
        Validate.notNull(item);

        // Abfrage, ob der ItemStack ein DisplayName hat
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
            return item.getItemMeta().getDisplayName();

        // Name des Material zurückgeben, wenn das Item keinen DisplayName hat
        return getName(item.getType());
    }

    public static final String getName(Enchantment enchantment) {
        // Null check
        Validate.notNull(enchantment);

        return instance.config.getOrElseSet("Enchantment." + enchantment.getKey().getKey(),
                                                                 enchantment.getKey().getKey());
    }

    public static String getName(EntityType entityType) {
        // Null check
        Validate.notNull(entityType);

        return instance.config.getOrElseSet("EntityType." + entityType.toString(), entityType.name());
    }

    public static String getName(PotionEffectType potionEffectType) {
        // Null check
        Validate.notNull(potionEffectType);

        return instance.config.getOrElseSet("PotionEffectType." + potionEffectType.getName(),
                                                                      potionEffectType.getName());
    }

    public static String getName(Villager.Profession profession) {
        // Null check
        Validate.notNull(profession);

        return instance.config.getOrElseSet("VillagerProfession." + profession.toString(), profession.name());
    }

    public static String getName(DayOfWeek day) {
        // Null check
        Validate.notNull(day);

        return instance.config.getOrElseSet("Day." + day.name(), day.name());
    }

    public static String getName(Month month) {
        // Null check
        Validate.notNull(month);

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
