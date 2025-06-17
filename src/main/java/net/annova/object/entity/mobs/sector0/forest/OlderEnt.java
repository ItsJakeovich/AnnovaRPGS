package net.annova.object.entity.mobs.sector0.forest;

import net.annova.object.CustomRunnable;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomOlderEnt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.world.SectorLocation.getChorusLocation;
import static net.annova.object.entity.core.RPGEntityType.ENT;
import static net.annova.object.item.base.Item.BLADE_IMBUED_WITH_MAGIC;
import static net.annova.object.item.base.Item.ENT_ROOT;
import static net.annova.util.BukkitObjects.random;

public class OlderEnt extends RPGEntity implements CustomType, EntityAction, EntityActivity {
    @Override
    public Class<?> clazz() {
        return CustomOlderEnt.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.IRON_GOLEM;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 500;
    }

    @Override
    public double damage() {
        return 14;
    }

    @Override
    public float speed() {
        return 0.285f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(
                ENT_ROOT.create(player, random.nextInt(7)+1),
                BLADE_IMBUED_WITH_MAGIC.create(player, 0.04)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    private int healingCooldown = 20; // 10 seconds
    Vec3 vec3;

    @Override
    public boolean onTarget(Entity target) {
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        return true;
    }

    @Override
    public boolean onDamage(Entity damager) {
        this.healingCooldown = 20;
        this.vec3 = null;
        return true;
    }

    @Override
    public void onDeath() {

    }

    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        if (healingCooldown != 0) healingCooldown--;
                        else {
                            OlderEnt.this.regen(2.5f);
                            if (vec3 == null) vec3 = OlderEnt.this.entity.position();
                            entity.level().getWorld().spawnParticle(Particle.TINTED_LEAVES,
                                    vec3.x, vec3.y + 1, vec3.z,
                                    8, 0.6, 1.2, 0.6, Color.GREEN
                            );
                            entity.setPos(vec3);
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 0;
                    }
                },
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        if (((Mob) entity).getTarget() != null) {
                            for (int i = random.nextInt(3) + 1; i != 0; i--) {
                                ENT.getEntity().create(getChorusLocation(OlderEnt.this.getLocation(), 8));
                            }
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 300;
                    }
                }
        );
    }
}
