package net.annova.object.item.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.equipment.EquipType;
import net.annova.object.person.status.ValueType;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;

public class EntsPatronAmulet extends RPGItem implements ItemEquipment, Listener {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(415f);
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
        return EquipType.AMULET;
    }

    @Override
    public Map<ValueType, Double> values(RPGPerson person) {
        return Map.of();
    }
}
