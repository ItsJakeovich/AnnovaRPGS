package net.annova.object.item.sector0.oasis;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;

import static net.annova.object.item.base.ItemType.ELK_FISH;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.ELK_FISH_DAMAGE_BUFF;

public class ElkFish extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(1f);
    }

    @Override
    public int stackSize() {
        return 16;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!ELK_FISH.equals(event.getItem())) return;

        Player player = event.getPlayer();
        getRPGPerson(player).getStatusManager().addStatus(ELK_FISH_DAMAGE_BUFF);
    }
}
