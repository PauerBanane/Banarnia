package de.banarnia.api.util;

import de.banarnia.api.messages.Language;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.EntityType;

/* UtilEntity
 * Utility-Klasse für diverse Entity-Methoden.
 */
public class UtilEntity {

    // Gibt ein EntityType anhand seines Namens, oder seiner Übersetzung wieder
    public static EntityType getEntityType(String name) {
        // Null check
        Validate.notNull(name);

        // Alle EntityTypes durchgehen und die Namen vergleichen
        for (EntityType entityType : EntityType.values()) {
            // Übersetzung in Variable speichern
            String translatedName = Language.getName(entityType);

            // Input mit Namen vergleichen
            if (name.equalsIgnoreCase(translatedName) || name.equalsIgnoreCase(entityType.toString()))
                return entityType;
        }

        // Null zurückgeben, wenn keine Übereinstimmung vorhanden ist
        return null;
    }

}
