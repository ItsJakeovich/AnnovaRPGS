package net.annova.object.item.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.equipment.EquipType;
import net.annova.object.person.status.ValueType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;

public class RootsLash extends RPGItem implements ItemEquipment {
    @Override
    public Item type() {
        return Items.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(430f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public EquipType getEquipmentType() {
        return EquipType.GLOVE;
    }

    @Override
    public Map<ValueType, Double> values(RPGPerson person) {
        return Map.of(
                ValueType.DEFENCE, 0.5,
                ValueType.BLOCK_RANGE, 1.0,
                ValueType.ENTITY_RANGE, 0.2
        );
    }
}
