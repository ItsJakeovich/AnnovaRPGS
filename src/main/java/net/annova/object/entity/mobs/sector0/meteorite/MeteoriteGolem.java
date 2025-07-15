package net.annova.object.entity.mobs.sector0.meteorite;

import net.annova.object.CustomRunnable;
import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomMeteoriteGolem;
import net.annova.object.magic.physic.EntRoots;
import net.annova.object.magic.projectile.MeteoriteBall;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.*;
import static net.annova.util.BukkitObjects.random;

public class  MeteoriteGolem extends RPGEntity implements CustomType, EntityActivity, Listener {
    @Override
    public Class<?> clazz() { // там прописано лечение от огня
        return CustomMeteoriteGolem.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.IRON_GOLEM;
    }

    @Override
    public String name() {
        return " ";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 330;
    }

    @Override
    public double damage() {
        return 10;
    }

    @Override
    public float speed() {
        return 0.225f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(
                METEORITE_FRAGMENT.create(person, random.nextInt(7)+6),
                FIRE_CRYSTAL.create(person, random.nextInt(2)+1, 0.33),
                MAGMA_FRAGMENT.create(person, random.nextInt(4)+1, 0.45),
                METEORITE_GOLEM_CORE.create(person, 0.04)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    private int massivePullCooldown = 60;
                    private boolean explosive = false;
                    private int explosionCooldown = 4;
                    private int ballCooldown = 30;
                    private int pullCooldown = 90;
                    private boolean pullReturn = false;

                    @Override
                    public boolean activity() {
                        LivingEntity target = ((Mob) entity).getTarget();
                        if (target == null) return true;

                        int reduce = entity.getHealth() / entity.getMaxHealth() < 0.4 ? 2 : 1;
                        this.massivePullCooldown -= reduce;
                        this.ballCooldown -= reduce;
                        this.pullCooldown -= reduce;
                        if (explosive) this.explosionCooldown -= reduce;

                        ServerLevel level = entity.level().getMinecraftWorld();
                        Vec3 pos = entity.position();
                        if (explosionCooldown <= 0) {
                            level.explode0(entity, level.damageSources().explosion(entity, entity)
                                    , null, pos.x, pos.y, pos.z, 2, true, Level.ExplosionInteraction.NONE, ParticleTypes.FLAME, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE);
                            this.explosionCooldown = 4;
                            this.explosive = false;
                        }

                        if (massivePullCooldown <= 0) {
                            for (Entity aroundTarget : entity.level().getEntities(entity, entity.getBoundingBox().inflate(10, 7, 10))) {
                                if (aroundTarget instanceof LivingEntity living) {
                                    living.push(pos.subtract(living.position()).normalize().scale(2));
                                    living.hurtMarked = true; // Обновление позиции клиента
                                }
                            }
                            this.explosive = true;
                            this.massivePullCooldown = 60;
                        }
                        if (ballCooldown <= 0) {
                            Location location = MeteoriteGolem.this.getLocation().add(0, 2, 0);
                            for (int i = 0; i < 8; i++) {
                                new MeteoriteBall().create(location.clone(), location.clone().add(random.nextDouble(4) - 2, -1, random.nextDouble(4) - 2));
                            }
                            this.ballCooldown = 30;
                        }
                        if (pullCooldown <= 0) {
                            target.push(pos.subtract(target.position()).normalize().scale(4.0));
                            this.pullCooldown = 90;
                            this.pullReturn = true;
                        } else if (this.pullReturn) {
                            Vec3 direction = target.position().subtract(pos).normalize().scale(3);
                            target.push(direction);

                            target.hurtServer((ServerLevel) entity.level(), level.damageSources().mobAttack(entity), 4);
                            EntRoots.addPhysicDebuff(target);
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 20;
                    }
                }
        );
    }
}
