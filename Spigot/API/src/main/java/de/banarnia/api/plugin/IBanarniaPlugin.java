package de.banarnia.api.plugin;

/* Interface IBanarniaPlugin
 * Enthält Standard-Methoden die optional aufgerufen werden können,
 * um den Code in den Main-Klassen zu verringern.
 */
interface IBanarniaPlugin {

    // Methoden zum Laden des Plugins
    default void load()     {}
    default void enable()   {}
    default void disable()  {}

    // Addons laden
    default void registerAddons() {}

}
