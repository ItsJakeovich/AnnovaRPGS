package net.annova.object.entity.core.attribute;

import org.bukkit.inventory.ItemStack;

public interface EntityEquipment {
    /*
     0 - mainhand
     1 - offhand
     2 - helmet
     3 - chestplate
     4 - leggings
     5 - boots
     */
    ItemStack[] equipment();
}
