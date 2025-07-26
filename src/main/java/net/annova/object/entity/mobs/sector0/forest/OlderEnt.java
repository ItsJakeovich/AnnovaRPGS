package net.annova.object.entity.mobs.sector0.forest;

import net.annova.event.custom.MobDamageCE;
import net.annova.main.GlobalRPG;
import net.annova.object.CustomRunnable;
import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomOlderEnt;
import net.annova.object.magic.physic.EntRoots;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Color;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.annova.environment.world.SectorLocation.getChorusLocation;
import static net.annova.object.entity.core.RPGEntityType.ENT;
import static net.annova.object.item.base.ItemType.*;
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
        return RPGRarity.RARE;
    }

    @Override
    public int health() {
        return 170;
    }

    @Override
    public double damage() {
        return 6;
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
    public List<ItemStack> loot(Person person) {
        return List.of(
                ENT_ROOT.create(person, random.nextInt(7)+2),
                BLADE_IMBUED_WITH_MAGIC.create(person, 0.074),
                new ItemStack(Items.OAK_LOG, random.nextInt(25)+16),
                new ItemStack(Items.STICK, random.nextInt(225)+20),
                ENT_CORE.create(person, 0.1)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public boolean onTarget(Entity target) {
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        return true;
    }

    private int healingCooldown = 20; // 10 seconds
    private Vec3 vec3;
    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
        LivingEntity nonRPG = event.getEntity();
        RPGEntity rpg = GlobalRPG.getRPGEntity(nonRPG);
        if (rpg instanceof OlderEnt ent) {
            ent.ENTS.removeIf(entCheck -> !entCheck.isAlive());
            if (!ent.ENTS.isEmpty()) event.decreaseDamage(event.getBaseDamage() * 0.15);

            ent.healingCooldown = 20;
            ent.vec3 = null;
        }
    }

    @Override
    public void onDeath() {
    }

    private final List<net.minecraft.world.entity.LivingEntity> ENTS = new ArrayList<>();
    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        if (healingCooldown != 0) healingCooldown--;
                        else {
                            OlderEnt.this.regen(10f);
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
                        return 400;
                    }
                },
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        if (((net.minecraft.world.entity.Mob) entity).getTarget() != null) {
                            ServerLevel level = entity.level().getMinecraftWorld();
                            for (int i = random.nextInt(3) + 1; i != 0; i--) {
                                ENTS.add(ENT.getEntity().create(getChorusLocation(entity.position(), level, 8), level));
                            }
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 900;
                    }
                },
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        Vec3 position = entity.position();
                        for (Entity target : entity.level().getEntities(null, new AABB(position, position).inflate(8, 6, 8))) {
                            if (target instanceof LivingEntity living) {
                                EntRoots.addPhysicDebuff(living);
                                ServerLevel level = target.level().getMinecraftWorld();
                                living.hurtServer(level, level.damageSources().generic(), 1);
                            }
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 600;
                    }
                }
        );
    }
}
