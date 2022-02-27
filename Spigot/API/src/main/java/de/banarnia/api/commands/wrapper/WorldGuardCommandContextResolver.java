package de.banarnia.api.commands.wrapper;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.locales.MessageKey;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.banarnia.api.commands.DefaultCommandCompletion;
import de.banarnia.api.messages.Message;
import org.bukkit.World;

/* DefaultCommandContextResolver
 * Registriert alle Standard Command-Completions und Command-Context.
 */
public class WorldGuardCommandContextResolver {

    public static void registerWorldGuardCommandContext(BukkitCommandManager commandManager) {

        // ProtectedRegion Completion - Listet nur die Regions in der Welt des Spielers
        commandManager.getCommandCompletions().registerCompletion(DefaultCommandCompletion.PROTECTED_REGION.get(), c -> {
            // Instanz der Welt abrufen
            World world = c.getPlayer().getWorld();

            // Region Container aufrufen
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

            // Namen der Regionen zurückgeben
            return container.get(BukkitAdapter.adapt(world)).getRegions().keySet();
        });
        // ProtectedRegion Context
        commandManager.getCommandContexts().registerContext(ProtectedRegion.class, c -> {
            // Erstes Argument abrufen
            final String tag = c.popFirstArg();

            // Instanz der Welt abrufen
            World world = c.getPlayer().getWorld();

            // Region Container aufrufen
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

            // Variable für die ProtectedRegion
            ProtectedRegion region = container.get(BukkitAdapter.adapt(world)).getRegions().get(tag);

            // Error throwen, wenn Region nicht gefunden wurde
            if (region == null)
                throw new InvalidCommandArgument(Message.ERROR_COMMAND_INVALID_PROTECTED_REGION.get());

            // ProtectedRegion zurückgeben, wenn keiner Fehler aufgetreten ist
            else
                return region;
        });

    }

}
