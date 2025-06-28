package net.annova.object.item.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.equipment.EquipType;
import net.annova.object.person.status.ValueType;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class AbsorbsNightRing extends RPGItem implements ItemEquipment {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(416f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public EquipType getEquipmentType() {
        return EquipType.RING;
    }

    @Override
    public Map<ValueType, Double> values(RPGPerson person) {
        int minutes = person.minutesInDarkForest;
        return Map.of(
                ValueType.SPEED, minutes == 60 ? -0.24 : minutes * -0.002,
                ValueType.DEFENCE, minutes == 60 ? -12 : minutes * -0.1
        );
    }
}
