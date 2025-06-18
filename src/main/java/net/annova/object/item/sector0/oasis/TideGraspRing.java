package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.annova.object.person.Equipment;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static net.annova.object.item.base.ItemType.TIDE_GRASP_RING;

public class TideGraspRing extends RPGItem implements ItemEquipment {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
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
    public List<String> lore() {
        return List.of("&3= &fСозданный из закалённой клешни краба&3, &fэтот перстень наполняет владельца мощью прилива&3. &fЕго носитель тянется дальше обычного&3.");
    }

    @Override
    public Equipment.Type getEquipmentType() {
        return Equipment.Type.RING;
    }

    @Override
    public boolean effect(Player player) {
        if (!TIDE_GRASP_RING.has(player)) return false;


        return true;
    }
}
