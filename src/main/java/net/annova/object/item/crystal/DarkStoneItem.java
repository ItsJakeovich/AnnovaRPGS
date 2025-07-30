package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.object.item.base.ItemType.DARK_STONE;

public class DarkStoneItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(308f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!DARK_STONE.use(player, item)) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 1, false, false, true));

        for (Entity entity : player.getNearbyEntities(16, 12, 16)) {
            if (!(entity instanceof Mob mob)) continue;
            for (Entity target : entity.getNearbyEntities(8, 6, 8)) {
                if (!(target instanceof LivingEntity targ)) continue;
                mob.setTarget(targ);
                break;
            }
        }
    }
}
