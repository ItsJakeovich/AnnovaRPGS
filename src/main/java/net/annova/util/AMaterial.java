package net.annova.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public enum AMaterial {
    BW(Material.BLACKSTONE_WALL),
    BLACK_CONCTR(Material.BLACK_CONCRETE)
    ;
    private final Material material;

    AMaterial(Material material) {
        this.material = material;
    }

    public static Material getMaterial(String id) {
        id = id.toUpperCase();
        try {
            return AMaterial.valueOf(id).material;
        } catch (IllegalArgumentException e) {
            try {
                return Material.valueOf(id);
            } catch (IllegalArgumentException ex) {
                Bukkit.getLogger().warning("Не найден: " + id);
                return Material.REDSTONE_BLOCK;
            }
        }
    }
}
