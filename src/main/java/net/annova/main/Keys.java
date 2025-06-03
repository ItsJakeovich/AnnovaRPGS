package net.annova.main;

import org.bukkit.NamespacedKey;

import static net.annova.main.AnnovaRPG.getInstance;

public class Keys {
    public static final NamespacedKey NK_ID = getNamespacedKey("id");
    public static final NamespacedKey NK_KEY = getNamespacedKey("key");
    public static final NamespacedKey NK_BIOME = getNamespacedKey("biome");
    public static final NamespacedKey NK_ATTACK_DAMAGE = getNamespacedKey("attack_damage");
    public static final NamespacedKey NK_ATTACK_SPEED = getNamespacedKey("attack_speed");
    public static final NamespacedKey NK_MILLIS = getNamespacedKey("millis");

    public static NamespacedKey getNamespacedKey(String string) {
        return new NamespacedKey("annovas", string);
    }
}
