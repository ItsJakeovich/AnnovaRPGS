package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.equipment.EquipType;
import net.annova.object.person.status.ValueType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TideGraspRing extends RPGItem implements ItemEquipment {
    @Override
    public Item type() {
        return Items.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(408f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public EquipType getEquipmentType() {
        return EquipType.RING;
    }
    @Override
    public Map<ValueType, Double> values(RPGPerson person) {
        Map<ValueType, Double> values = new EnumMap<>(ValueType.class);
        values.put(ValueType.DAMAGE, 2.0);
        values.put(ValueType.BLOCK_RANGE, -0.5);
        values.put(ValueType.ENTITY_RANGE, -0.2);
        return values;
    }
}
