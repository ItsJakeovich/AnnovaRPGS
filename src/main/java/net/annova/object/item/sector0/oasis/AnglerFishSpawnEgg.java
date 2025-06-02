package net.annova.object.item.sector0.oasis;

import net.annova.object.RPGRarity;
import net.annova.object.entity.mobs.sector0.AnglerFish;
import net.annova.object.item.base.RPGItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

import static net.annova.object.item.base.Item.ANGLER_FISH_SPAWN_EGG;

public class AnglerFishSpawnEgg extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON_SPAWN_EGG;
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
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fМаленький&3, &fно упрямый рыбёныш&3, &fкоторый так и норовит помогать рыбачить&3. &fОдинокий в глубинах&3, &fон ищет напарника&3, &fкоторому сможет посвятить всю свою биолюминесцентную жизнь");
    }

    @EventHandler
    public void entitySpawn(PlayerInteractEvent event) {
        if (!ANGLER_FISH_SPAWN_EGG.equals(event.getItem())) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;
        new AnglerFish().create(block.getLocation().add(0, 1, 0));

        event.setCancelled(true);
        player.swingMainHand();
        subtractItem(player);
    }
}
