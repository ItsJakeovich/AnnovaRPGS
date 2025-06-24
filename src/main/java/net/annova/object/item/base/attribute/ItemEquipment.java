package net.annova.object.item.base.attribute;

import net.annova.object.person.RPGPerson;
import net.annova.object.person.equipment.EquipType;
import net.annova.object.person.status.ValueType;
import org.bukkit.entity.Player;

import java.util.Map;

public interface ItemEquipment {
    EquipType getEquipmentType();
    Map<ValueType, Double> values(RPGPerson person);
    boolean effect(Player player);
}
