package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.CustomRunnable;
import net.annova.object.LocationCategory;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomMelanox;
import net.annova.object.magic.projectile.MelanoxRay;
import net.annova.object.magic.projectile.WebBall;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static net.annova.object.entity.core.RPGEntityType.*;
import static net.annova.object.item.base.ItemType.SPIDER_CHITIN;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class Melanox extends RPGEntity implements CustomType, EntityActivity, EntityAction {
    @Override
    public Class<?> clazz() {
        return CustomMelanox.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.SPIDER;
    }

    @Override
    public String name() {
        return "Melanox";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public int health() {
        return 220;
    }

    @Override
    public double damage() {
        return 11;
    }

    @Override
    public float speed() {
        return 0.345f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(SPIDER_CHITIN.create(player, random.nextInt(9)+12)); // от 12 до 20
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        LivingEntity target = ((Mob) entity).getTarget();
                        if (target == null) return true;

                        Vec3 vec3 = target.position();
                        if (chance(0.3)) entity.teleportTo(vec3.x, vec3.y, vec3.z);
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 300;
                    }
                },
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        if (((Mob) entity).getTarget() == null) return true;

                        Location location = Melanox.this.getLocation();
                        for (int i = 0; i < 6; i++) {
                            new MelanoxRay().create(location.clone(), location.clone().add(random.nextDouble(2) - 1, random.nextDouble(0.4), random.nextDouble(2) - 1));
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 900; // 45s
                    }
                },
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        LivingEntity target = ((Mob) entity).getTarget();
                        if (target != null) {
                            new WebBall().create(entity, target.getBukkitEntity());
                        } return true;
                    }

                    @Override
                    public long repeating() {
                        return 400; // 20s
                    }
                },
                new CustomRunnable() {
                    private final EnumSet<RPGEntityType> spiders = getEntitiesByCategory(LocationCategory.S0_SWAMPS, MELANOX);
                    private final double chance = 1.0 / spiders.size();
                    private final List<LivingEntity> summoned = new ArrayList<>();

                    @Override
                    public boolean activity() {
                        List<LivingEntity> toRemove = new ArrayList<>();
                        for (LivingEntity summon  : summoned) {
                            if (summon.isAlive() && summon.valid) continue;
                            toRemove.add(summon);
                        }
                        this.summoned.removeAll(toRemove);

                        if (!summoned.isEmpty()) {
                            Melanox.this.entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1200, 0));
                        }

                        if (summoned.size() < 20) {
                            for (int i = 0;;) {
                                for (RPGEntityType type : spiders) {
                                    if (!chance(chance)) continue;
                                    summoned.add(type.getEntity().create(entity)); i++;
                                    if (i == 3) return true;
                                }
                            }
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 1100;
                    }
                }
        );
    }

    @Override
    public boolean onTarget(Entity target) {
        return true;
    }

    @Override
    public boolean onAttack(org.bukkit.entity.LivingEntity target) {
        return true;
    }

    @Override
    public void onDamage(MobDamageCE event, org.bukkit.entity.LivingEntity damager) {
        if (this.entity.getHealth() < this.entity.getMaxHealth()*0.15 && chance(0.1)) {
            damager.setVelocity(damager.getLocation().toVector().subtract(this.getLocation().toVector())
                    .normalize().multiply(3)
            );
        }
        return true;
    }

    @Override
    public void onDeath() {

    }
}
