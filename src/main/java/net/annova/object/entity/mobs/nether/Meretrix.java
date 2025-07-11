package net.annova.object.entity.mobs.nether;

import net.annova.object.CustomRunnable;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class Meretrix extends RPGEntity implements EntityActivity {
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
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

//    @EventHandler
//    private void spawn(CreatureSpawnEvent event) {
//        LivingEntity ent = event.getEntity();
//        if (!(ent.getType().equals(org.bukkit.entity.EntityType.SKELETON))) return;
//        if (GlobalRPG.containEntity(ent)) return;
//        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
//
//        if (!chance(0.09)) return;
//        LivingEntity entity = (LivingEntity) this.create(ent.getLocation()).getBukkitEntity();
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                if (!entity.isValid()) this.cancel();
//                for (Entity check : entity.getNearbyEntities(5, 5, 5)) {
//                    if (check instanceof Player player) {
//                        entity.setVelocity(check.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(1.1));
//                        break;
//                    }
//                }
//            }
//        }.runTaskTimer(getInstance(), 0L, 100L);
//    }
    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        LivingEntity ent = (LivingEntity) entity.getBukkitEntity();
                        for (Entity check : ent.getNearbyEntities(5, 5, 5)) {
                            if (check instanceof Player player) {
                                ent.setVelocity(check.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize().multiply(1.1));
                                break;
                            }
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 100;
                    }
                }
        );
    }
}
