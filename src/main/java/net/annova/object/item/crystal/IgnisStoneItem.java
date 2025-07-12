package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.person.status.Status;
import net.annova.event.custom.BattleCE;
import net.annova.event.custom.MobDamageCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static net.annova.object.item.base.ItemType.IGNIS_STONE;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.IGNIS;

public class IgnisStoneItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(303f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!IGNIS_STONE.use(player, item)) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0));
        RPGPerson person = getRPGPerson(player);
        StatusManager manager = person.getStatusManager();
        Status status = manager.getStatus(IGNIS);
        if (status != null) return;

        manager.addStatus(IGNIS);
    }

    private final Map<Entity, Long> targets = new HashMap<>();

    @EventHandler
    private void damager(BattleCE event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!getRPGPerson(player).getStatusManager().hasStatus(IGNIS)) return;

        LivingEntity entity = event.getEntity();
        if (targets.containsKey(entity)) return;
        targets.put(entity, System.currentTimeMillis()+1200);
    }
    @EventHandler
    private void damager(MobDamageCE event) {
        LivingEntity entity = event.getEntity();
        if (!targets.containsKey(entity)) return;
        if (System.currentTimeMillis() >= targets.get(entity)) {
            targets.remove(entity);
            return;
        }
        event.increaseDamage(10);
    }
}