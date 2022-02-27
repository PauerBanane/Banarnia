package de.banarnia.api;

import de.banarnia.api.messages.Message;
import de.banarnia.api.messages.MessageHandler;
import de.banarnia.api.plugin.BanarniaPlugin;
import de.banarnia.api.smartInventory.InventoryManager;
import org.bukkit.Bukkit;

public class BanarniaAPI extends BanarniaPlugin {

    // Static Instanz des Singletons
    private static BanarniaAPI instance;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Aufruf ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void preLoad() {
        // Static Instanz laden
        instance = this;
    }

    @Override
    public void load() {
    }

    @Override
    public void enable() {
        // InventoryManager instanziieren
        new InventoryManager();

        // Commands registrieren
        skullManager.registerCommand();
    }

    @Override
    public void disable() {
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Getter der Singleton Instanz
    public static BanarniaAPI getInstance() {
        return instance;
    }
}
