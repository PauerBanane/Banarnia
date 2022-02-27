package de.banarnia.api.util;

import de.banarnia.api.BanarniaAPI;
import org.bukkit.Bukkit;

/* UtilScheduler Klasse
 * Beinhaltet Scheduler Methoden für den einfachen Zugriff.
 */
public class UtilScheduler {

    // Runnable mit Delay aufrufen
    public static void runTaskLater(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(BanarniaAPI.getInstance(), runnable, delay);
    }

}
