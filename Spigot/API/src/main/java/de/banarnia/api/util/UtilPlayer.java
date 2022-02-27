package de.banarnia.api.util;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/* UtilPlayer
 * Utility-Klasse für diverse Spieler Methoden.
 */
public class UtilPlayer {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Sound ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Sound für den Spieler abspielen - mit String als Sound
    public static void playSound(Player player, String soundName) {
        // Sound aufrufen
        Sound sound = UtilEnum.get(Sound.values(), soundName);

        // Nachricht in die Konsole, wenn Name ungültig
        if (sound == null) {
            Bukkit.getLogger().warning("Cannot play sound with invalid name: " + soundName);
            return;
        }

        // Sound abspielen
        playSound(player, sound);
    }

    // Sound für den Spieler abspielen
    public static void playSound(Player player, Sound sound) {
        // Null check
        if (player == null || sound == null)
            return;

        // Sound abspielen
        playSound(player, sound, 1.0f, 1.0f);
    }

    // Sound für den Spieler abspielen
    public static void playSound(Player player, Sound sound, float v, float v1) {
        // Null check
        if (player == null || sound == null)
            return;

        // Sound abspielen
        player.playSound(player.getLocation(), sound, v, v1);
    }

}
