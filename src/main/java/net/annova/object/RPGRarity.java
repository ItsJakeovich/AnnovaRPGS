package net.annova.object;

import org.bukkit.ChatColor;
import java.awt.Color;

public enum RPGRarity {
    EVENT(ChatColor.DARK_BLUE),

    COMMON(ChatColor.GRAY),
    UNCOMMON(ChatColor.GREEN),
    RARE(ChatColor.AQUA),
    ELITE(ChatColor.LIGHT_PURPLE),
    EPIC(ChatColor.DARK_PURPLE),
    LEGEND(ChatColor.GOLD),
    MYTHIC(ChatColor.RED),
    IMPOSSIBLE(ChatColor.DARK_RED) //
    ;

    private final ChatColor color;
    RPGRarity(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }
}
