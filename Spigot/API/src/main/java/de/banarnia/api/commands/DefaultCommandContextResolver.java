package de.banarnia.api.commands;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.Lists;
import de.banarnia.api.commands.wrapper.WorldGuardCommandContextResolver;
import de.banarnia.api.messages.Language;
import de.banarnia.api.messages.Message;
import de.banarnia.api.plugin.BanarniaPlugin;
import de.banarnia.api.util.UtilEntity;
import de.banarnia.api.util.UtilMath;
import de.banarnia.api.util.UtilString;
import de.banarnia.api.util.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/* DefaultCommandContextResolver
 * Registriert alle Standard Command-Completions und Command-Context.
 */
public class DefaultCommandContextResolver {

    public static void registerDefaultContextAndCompletions(PaperCommandManager commandManager) {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Java Context ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // DayOfWeek Completion
        commandManager.getCommandCompletions().registerCompletion(DefaultCommandCompletion.DAY.get(),
                // Liste der Wochentage zurückgeben
                c -> Arrays.stream(DayOfWeek.values())
                        .map(day -> Language.getName(day))
                        .collect(Collectors.toUnmodifiableList()));
        // DayOfWeek Context
        commandManager.getCommandContexts().registerContext(DayOfWeek.class, c -> {
            // Erstes Argument abrufen
            final String tag = c.popFirstArg();

            // Versuchen den Month zu parsen
            DayOfWeek day = UtilTime.getDayOfWeek(tag);

            // Error throwen, wenn der Monat nicht geparsed werden konnte
            if (day == null)
                throw new InvalidCommandArgument(Message.ERROR_COMMAND_INVALID_DAY.get());

            // DayOfWeek zurückgeben
            else
                return day;
        });

        // Monat Completion
        commandManager.getCommandCompletions().registerCompletion(DefaultCommandCompletion.MONTH.get(),
                // Liste der Monate zurückgeben
                c -> Arrays.stream(Month.values())
                        .map(month -> Language.getName(month))
                        .collect(Collectors.toUnmodifiableList()));
        // Monat Context
        commandManager.getCommandContexts().registerContext(Month.class, c -> {
            // Erstes Argument abrufen
            final String tag = c.popFirstArg();

            // Versuchen den Month zu parsen
            Month month = UtilTime.getMonth(tag);

            // Error throwen, wenn der Monat nicht geparsed werden konnte
            if (month == null)
                throw new InvalidCommandArgument(Message.ERROR_COMMAND_INVALID_MONTH.get());

            // Monat zurückgeben
            else
                return month;
        });

        // Jahr Completion
        commandManager.getCommandCompletions().registerCompletion(DefaultCommandCompletion.YEAR.get(), c -> {
            // Liste für die zurückgegebenen Completions
            List<String> nextYears = Lists.newLinkedList();

            // Aktuelles Jahr auslesen
            int currentYear = LocalDateTime.now().getYear();

            // Die letzten zwei, das aktuelle und die nächsten zwei Jahre zur Liste hinzufügen
            for (int i = -2; i < 3; i++)
                nextYears.add(String.valueOf(currentYear + i));

            // Liste zurückgeben
            return nextYears;
        });
        // Jahr Context
        commandManager.getCommandContexts().registerContext(Year.class, c -> {
            // Erstes Argument abrufen
            final String tag = c.popFirstArg();

            // Versuchen das Jahr zu parsen
            Year year = UtilTime.getYear(tag);

            // Error throwen, wenn das Jahr nicht geparsed werden konnte
            if (year == null)
                throw new InvalidCommandArgument(Message.ERROR_COMMAND_INVALID_YEAR.get());

            // Jahr zurückgeben
            else
                return year;
        });

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Spigot Context ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // GameMode Completion
        commandManager.getCommandCompletions().registerCompletion(DefaultCommandCompletion.GAMEMODE.get(),
                c -> Arrays.stream(GameMode.values())
                .map(gameMode -> Language.getName(gameMode))
                .collect(Collectors.toUnmodifiableList()));
        // GameMode Context
        commandManager.getCommandContexts().registerContext(GameMode.class, c -> {
            // Erstes Argument abrufen
            final String tag = c.popFirstArg();

            // Abfrage, ob der GameMode als Zahl eingegeben wurde
            if (UtilMath.isInt(tag)) {
                switch (Integer.parseInt(tag)) {
                    case 0:
                        return GameMode.SURVIVAL;
                    case 1:
                        return GameMode.CREATIVE;
                    case 2:
                        return GameMode.ADVENTURE;
                    case 3:
                        return GameMode.SPECTATOR;
                }
            }

            // Versuchen den GameMode durch die Übersetzung zuzuordnen
            for (GameMode gameMode : GameMode.values()) {
                // Übersetzten Namen erhalten
                String translatedName = Language.getName(gameMode);

                // Abfrage, ob die Eingabe übereinstimmt
                if (tag.equalsIgnoreCase(gameMode.toString()) || tag.equalsIgnoreCase(translatedName))
                    return gameMode;
            }

            // Error throwen, wenn der GameMode nicht gefunden wurde
            throw new InvalidCommandArgument(Message.ERROR_COMMAND_INVALID_GAMEMODE.get());
        });

        // Material Completion
        commandManager.getCommandCompletions().registerCompletion(DefaultCommandCompletion.MATERIAL.get(),
                c -> Arrays.stream(Material.values())
                .map(material -> Language.getName(material))
                .collect(Collectors.toUnmodifiableList()));
        // Material Context
        commandManager.getCommandContexts().registerContext(Material.class, c -> {
            // Argumente abrufen
            final String tag = UtilString.mergeStringList(c.getArgs(), " ");

            // Variable für Material erstellen
            Material material = null;

            // Versuchen das Material über den Namen zuzuordnen
            for (Material forMaterial : Material.values()) {
                // Übersetzung abrufen
                String translatedMaterial = Language.getName(forMaterial);

                // Abfrage, ob der Input mit dem Material übereinstimmt
                if (forMaterial.toString().equalsIgnoreCase(tag) || translatedMaterial.equalsIgnoreCase(tag))
                    material = forMaterial;
            }

            // Error throwen, wenn das Material nicht gefunden wurde
            if (material == null)
                throw new InvalidCommandArgument(Message.ERROR_COMMAND_INVALID_MATERIAL.get());

            // Material zurückgeben, wenn kein Fehler aufgetreten ist
            else
                return material;
        });

        // EntityType Completion
        commandManager.getCommandCompletions().registerCompletion(DefaultCommandCompletion.ENTITYTYPE.get(),
                c -> Arrays.stream(EntityType.values())
                .map(entityType -> Language.getName(entityType))
                .collect(Collectors.toUnmodifiableList()));
        // EntityType Context
        commandManager.getCommandContexts().registerContext(EntityType.class, c -> {
            // Argument abrufen
            final String tag = UtilString.mergeStringList(c.getArgs(), " ");

            // Überprüfen, ob die Eingabe einem EntityType entspricht
            EntityType entityType = UtilEntity.getEntityType(tag);

            // Error throwen, wenn EntityType nicht gefunden wurde
            if (entityType == null)
                throw new InvalidCommandArgument(Message.ERROR_COMMAND_INVALID_ENTITYTYPE.get());

            // EntityType zurückgeben
            else
                return entityType;
        });

        // Sound Completion
        commandManager.getCommandCompletions().registerCompletion(DefaultCommandCompletion.SOUND.get(),
                c -> Arrays.stream(Sound.values())
                .map(sound -> sound.toString().toLowerCase())
                .collect(Collectors.toUnmodifiableList()));
        // Sound Context
        commandManager.getCommandContexts().registerContext(Sound.class, c -> {
            // Erstes Argument abrufen
            final String tag = c.popFirstArg();

            // Variable für Sound erstellen
            Sound sound = null;

            // For-Schleife für Vergleich von Input mit Sound-Namen
            for (Sound forSound : Sound.values()) {
                if (forSound.toString().equalsIgnoreCase(tag))
                    sound = forSound;
            }

            // Error throwen, wenn Sound nicht gefunden wurde
            if (sound == null)
                throw new InvalidCommandArgument(Message.ERROR_COMMAND_INVALID_SOUND.get());

            // Sound zurückgeben, wenn kein Fehler aufgetreten ist
            else
                return sound;
        });

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Custom Context ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // BanarniaPlugin Completion
        commandManager.getCommandCompletions().registerCompletion(DefaultCommandCompletion.BANARNIA_PLUGIN.get(),
                c -> BanarniaPlugin.getLoadedPlugins().keySet());
        // BanarniaPlugin Context
        commandManager.getCommandContexts().registerContext(BanarniaPlugin.class, c -> {
            // Erstes Argument abrufen
            final String tag = c.popFirstArg();

            // Versuchen das BanarniaPlugin durch den Namen zu erhalten
            BanarniaPlugin plugin = BanarniaPlugin.getLoadedPlugins().get(tag);

            // Error throwen, wenn das Plugin nicht gefunden wurde
            if (plugin == null)
                throw new InvalidCommandArgument(Message.ERROR_COMMAND_INVALID_BANARNIA_PLUGIN.get());

            // BanarniaPlugin zurückgeben, wenn kein Fehler aufgetreten ist
            else
                return plugin;
        });

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Lib Context ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // WorldGuard Context aufrufen
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null)
            WorldGuardCommandContextResolver.registerWorldGuardCommandContext(commandManager);

    }

}
