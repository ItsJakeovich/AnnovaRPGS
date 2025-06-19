package net.annova.object.item.base.attribute;

import net.annova.object.person.equipment.EquipType;
import net.annova.object.person.status.ValueType;
import org.bukkit.entity.Player;

import java.util.Map;

public interface ItemEquipment {
    EquipType getEquipmentType();
    Map<ValueType, Double> values(Player player);
    boolean effect(Player player);
}
