package net.annova.object;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;

public enum RPGRarity {
    EVENT(NamedTextColor.BLUE),

    COMMON(NamedTextColor.GRAY),
    UNCOMMON(NamedTextColor.GREEN),
    RARE(NamedTextColor.AQUA),
    ELITE(NamedTextColor.LIGHT_PURPLE),
    EPIC(NamedTextColor.DARK_PURPLE),
    LEGEND(NamedTextColor.GOLD),
    MYTHIC(NamedTextColor.RED),
    IMPOSSIBLE(NamedTextColor.DARK_RED) //
    ;

    private final TextColor color;
    RPGRarity(TextColor color) {
        this.color = color;

        TextComponent text = Component.text("").color(TextColor.color(0x22f9f6)).toBuilder();
        text.compact();
        text.colo
        Player player = null;
        player.sendMessage(text);
        text.replaceText();
    }

    public TextColor getColor() {
        return color;
    }
}
