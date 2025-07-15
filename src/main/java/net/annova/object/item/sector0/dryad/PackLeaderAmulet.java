package net.annova.object.item.sector0.dryad;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.equipment.EquipType;
import net.annova.object.person.status.ValueType;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;

import static net.minecraft.world.item.Items.NAUTILUS_SHELL;

public class PackLeaderAmulet extends RPGItem implements ItemEquipment {
    @Override
    public Item type() {
        return NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(424f);
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
        return EquipType.AMULET;
    }

    @Override
    public Map<ValueType, Double> values(RPGPerson person) {
        return Map.of();
    }
}
