package net.annova.object.item.sector0.oasis;

import net.annova.object.RPGRarity;
import net.annova.object.entity.mobs.sector0.oasis.AnglerFish;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

import static net.annova.object.item.base.ItemType.ANGLER_FISH_SPAWN_EGG;

public class AnglerfishSpawnEgg extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.SALMON_SPAWN_EGG;
    }

    @Override
    public List<Float> modelData() {
        return List.of(11f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @EventHandler
    public void entitySpawn(PlayerInteractEvent event) {
        if (!ANGLER_FISH_SPAWN_EGG.equals(event.getItem())) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;
        new AnglerFish().create(block.getRelative(event.getBlockFace()).getLocation());

        event.setCancelled(true);
        player.swingMainHand();
        subtractItem(player);
    }
}
