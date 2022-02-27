package de.banarnia.api.config;

import com.google.common.collect.Lists;
import de.banarnia.api.BanarniaAPI;
import de.banarnia.api.util.FileLoader;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.List;

public class ApiConfig {

    // Config initialisieren
    static {
        config = FileLoader.of(BanarniaAPI.getInstance().getDataFolder(), "ApiConfig.yml");
    }

    // Config-Datei
    private static FileLoader config;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konversation ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static List<String> CONVERSATION_CANCEL_WORDS() {
        return config.getOrElseSet("Conversation.Cancel-words", Lists.newArrayList("Abbrechen"));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Allgemein ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static String ERROR_SOUND() {
        return config.getOrElseSet("Error-Sound", Sound.ENTITY_PIG_AMBIENT.toString());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ GUI ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static String GUI_PREV_PAGE_ICON() {
        return config.getOrElseSet("GUI.Buttons.Previous-Page", "Skull:Holz_Pfeil_Links");
    }

    public static String GUI_NEXT_PAGE_ICON() {
        return config.getOrElseSet("GUI.Buttons.Next-Page", "Skull:Holz_Pfeil_Rechts");
    }

    public static String GUI_BACK_ICON() {
        return config.getOrElseSet("GUI.Buttons.Back", "Skull:Holz_Pfeil_Unten");
    }

    public static String GUI_CLOSE_ICON() {
        return config.getOrElseSet("GUI.Buttons.Close", Material.BARRIER.toString());
    }

    public static String GUI_TOGGLE_ICON_TRUE() {
        return config.getOrElseSet("GUI.Buttons.True", Material.GREEN_DYE.toString());
    }

    public static String GUI_TOGGLE_ICON_FALSE() {
        return config.getOrElseSet("GUI.Buttons.False", Material.LIGHT_GRAY_DYE.toString());
    }

}
