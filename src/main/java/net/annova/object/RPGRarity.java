package net.annova.object;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;

public enum RPGRarity {
    EVENT(5592575), // blue

    COMMON(11184810), // gray
    UNCOMMON(5635925), // green
    RARE(5636095), // aqua
    ELITE(16733695), // light purple
    EPIC(11141290), // dark purple
    LEGEND(16755200), // gold
    MYTHIC(16733525), // red
    IMPOSSIBLE(11141120) // dark red
    ;

    private final int color;
    RPGRarity(int color) {
        this.color = color;

//        TextComponent text = Component.text("").color(TextColor.color(0x22f9f6)).toBuilder();
//        text.compact();
//        text.colo
//        Player player = null;
//        player.sendMessage(text);
//        text.replaceText();
    }

    public int getColor() {
        return color;
    }
}
