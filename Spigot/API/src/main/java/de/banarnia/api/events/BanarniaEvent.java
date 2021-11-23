package de.banarnia.api.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public abstract class BanarniaEvent extends Event {

    public void callEvent() {
        Bukkit.getPluginManager().callEvent(this);
    }

}
