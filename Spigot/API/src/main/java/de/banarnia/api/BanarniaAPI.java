package de.banarnia.api;

import org.bukkit.plugin.java.JavaPlugin;

public class BanarniaAPI extends JavaPlugin {

    private static BanarniaAPI instance;

    public static BanarniaAPI getInstance() {
        return instance;
    }
}
