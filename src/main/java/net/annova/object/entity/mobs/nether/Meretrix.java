package net.annova.object.entity.mobs.nether;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.AbilityType;
import net.annova.object.entity.core.RPGEntity;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.chance;

public class Meretrix extends RPGEntity implements Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.VEX;
    }

    @Override
    public String name() {
        return "Meretrix";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 55;
    }

    @Override
    public double damage() {
        return 36;
    }

    @Override
    public float speed() {
        return 0.2f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of();
    }

    @Override
    public Set<AbilityType> abilities() {
        return Set.of();
    }

    @EventHandler
    private void spawn(CreatureSpawnEvent event) {
        LivingEntity ent = event.getEntity();
        if (!(ent.getType().equals(org.bukkit.entity.EntityType.SKELETON))) return;
        if (GlobalRPG.containEntity(ent)) return;
        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

        if (!chance(0.09)) return;
        LivingEntity entity = (LivingEntity) this.create(ent.getLocation()).getBukkitEntity();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!entity.isValid()) cancel();
                for (Entity check : entity.getNearbyEntities(5, 5, 5)) {
                    if (check instanceof Player player) {
                        entity.setVelocity(check.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(1.1));
                        break;
                    }
                }
            }
        }.runTaskTimer(getInstance(), 0L, 100L);
    }
}
