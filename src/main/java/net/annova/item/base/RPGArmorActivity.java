package net.annova.item.base;

import org.bukkit.entity.Player;

public interface RPGArmorActivity {
    void activity(Player player);
    long repeating();
}
