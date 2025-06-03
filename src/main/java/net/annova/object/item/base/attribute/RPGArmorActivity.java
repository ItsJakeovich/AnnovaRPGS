package net.annova.object.item.base.attribute;

import org.bukkit.entity.Player;

public interface RPGArmorActivity {
    void activity(Player player);
    long repeating();
}
