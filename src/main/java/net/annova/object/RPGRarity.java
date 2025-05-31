package net.annova.object;

import org.bukkit.ChatColor;
import java.awt.Color;

public enum RPGRarity {
    COMMON(ChatColor.GRAY),
    RARE(new Color(0x22f9f6)),
    ELITE(ChatColor.LIGHT_PURPLE),
    EPIC(ChatColor.DARK_PURPLE),
    LEGEND(ChatColor.GOLD),
    MYTHIC(ChatColor.RED);
    ;

    private final Color color;
    RPGRarity(Color color) {
        this.color = color;
    }
    RPGRarity(ChatColor color) {
        this.color =color.asBungee().getColor();
    }

    public Color getColor() {
        return color;
    }
}
