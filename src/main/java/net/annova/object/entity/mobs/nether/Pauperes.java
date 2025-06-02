package net.annova.object.entity.mobs.nether;

import net.annova.object.RPGRarity;
import net.annova.object.entity.ability.AbilityType;
import net.annova.object.entity.core.RPGActivity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class Pauperes extends RPGEntity implements RPGActivity, Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.GHAST;
    }

    @Override
    public String name() {
        return "Pauperes";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 150;
    }

    @Override
    public double damage() {
        return 6;
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
        if (!(ent.getType().equals(org.bukkit.entity.EntityType.GHAST))) return;
        if (GlobalRPG.containEntity(ent)) return;
        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

        if (!chance(0.09)) return;
        this.create(ent.getLocation());
        event.setCancelled(true);
    }

    @Override
    public boolean activity(LivingEntity entity) {
        LivingEntity target = ((Mob) entity).getTarget();
        if (target == null) return true;

        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 0));

        new BukkitRunnable() {
            private int i = 0;
            private final int max = random.nextInt(6)+2;
            @Override
            public void run() {
                if (i > max) cancel();
                Fireball fireball = entity.launchProjectile(Fireball.class);
                fireball.setYield(2);
                fireball.setVelocity(target.getLocation().toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(1.5));
                i++;
            }
        }.runTaskTimer(getInstance(), 0L, 5L);
        return  true;
    }

    @Override
    public long repeating() {
        return 60;
    }

    @Override
    public boolean syncActivity(LivingEntity entity) {
        return true;
    }

    @Override
    public long syncRepeating() {
        return 0;
    }
}
