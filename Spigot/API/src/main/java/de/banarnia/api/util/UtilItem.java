package de.banarnia.api.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.banarnia.api.skulls.Skulls;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

/* UtilItem Klasse.
 * Enthält nützliche Methoden in Bezug auf Items.
 */
public class UtilItem {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Skulls ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ItemStack durch URL bekommen
    public static ItemStack getSkull(String url) {
        // Null check
        if (url == null)
            throw new IllegalArgumentException();

        // ItemStack anlegen
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);

        // Abfrage, ob URL stimmen kann - sonst wird eine Standard URL verwendet
        if (url.isEmpty())
            url = Skulls.SkullNotFound.getUrl();

        // Vollständige URL anlegen
        String fullUrl = "http://textures.minecraft.net/texture/" + url;

        // SkullMeta aufrufen
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        // GameProfile erstellen
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        // Head-URL im Profil eintragen
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        // Profil in den MetaDaten eintragen
        // Field anlegen
        Field profileField = null;

        // Versuchen das Field zu manipulieren
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        // ItemMeta setzen
        skull.setItemMeta(skullMeta);

        // Item zurückgeben
        return skull;
    }

}
