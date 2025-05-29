package net.annova.item.equipment.oasis;

import net.annova.item.base.RPGEquipment;
import net.annova.item.base.RPGItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static net.annova.item.base.Item.TIDE_GRASP_RING;

public class TideGraspRing extends RPGItem implements RPGEquipment {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of();
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fСозданный из закалённой клешни краба&3, &fэтот перстень наполняет владельца мощью прилива&3. &fЕго носитель тянется дальше обычного&3.");
    }

    @Override
    public boolean effect(Player player) {
        if (!TIDE_GRASP_RING.has(player)) return false;


        return true;
    }
}
