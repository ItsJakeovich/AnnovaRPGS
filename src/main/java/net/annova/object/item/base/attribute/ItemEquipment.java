package net.annova.object.item.base.attribute;

import net.annova.object.person.Equipment;
import org.bukkit.entity.Player;

public interface ItemEquipment {
    Equipment.Type getEquipmentType();
    boolean effect(Player player);
}
