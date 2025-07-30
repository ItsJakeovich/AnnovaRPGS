package net.annova.object.item.sector0.oasis;

import net.annova.object.RPGRarity;
import net.annova.object.person.RPGPerson;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;

import static net.annova.object.item.base.ItemType.FURY_CAVIAR;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.CAVIAR_DAMAGE_BUFF;

public class FuryCaviar extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(13f);
    }

    @Override
    public int stackSize() {
        return 16;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!FURY_CAVIAR.equals(event.getItem())) return;

        RPGPerson person = getRPGPerson(event.getPlayer());
        person.getStatusManager().addStatus(CAVIAR_DAMAGE_BUFF);
    }
}
