package net.annova.object.entity.mobs.sector0.forest;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.CustomRunnable;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.EntityEquipment;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomDarkElf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.world.GlobalWorld.isNight;
import static net.annova.object.item.base.ItemType.*;
import static net.annova.util.BukkitObjects.random;

public class DarkElf extends RPGEntity implements CustomType, EntityEquipment, EntityActivity, EntityAction {
    @Override
    public Class<?> clazz() {
        return CustomDarkElf.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.BOGGED;
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
        return 110;
    }

    @Override
    public double damage() {
        return 6;
    }

    @Override
    public float speed() {
        return 0.315f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(
                DARKNESS_CONCENTRATION.create(player, 1),
                DARK_ELF_BOW.create(player, 0.091),
                ABSORBS_NIGHT_RING.create(player, 0.087),
                DARKNESS_FRAGMENT.create(player, 0.125)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public ItemStack[] equipment() {
        return new ItemStack[] {
                new ItemStack(Material.BOW),
                null,
                null,
                null,
                null,
                null
        };
    }

    private static final MobEffectInstance INVISIBILITY = new MobEffectInstance(MobEffects.INVISIBILITY, 40, 0);
    public static AttributeModifier SPEED_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("annovas", "dark_elf_buff")
            , 0.25f, AttributeModifier.Operation.ADD_VALUE
    );
    private static final MobEffectInstance BLINDNESS = new MobEffectInstance(MobEffects.BLINDNESS, 600, 0);
    private static final MobEffectInstance SLOWNESS = new MobEffectInstance(MobEffects.SLOWNESS, 600, 2);
    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() { // тут и восстановление, и град стрел
                    int cooldown = 60;

                    @Override
                    public boolean activity() {
                        AttributeInstance instance = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                        if (isNight(entity.level().getWorld())) {
                            DarkElf.this.regen(1.0f);
                            if (!instance.hasModifier(SPEED_MODIFIER.id())) {
                                instance.addTransientModifier(SPEED_MODIFIER);
                            }
                        } else {
                            instance.removeModifier(SPEED_MODIFIER);
                        }

                        if (((Mob) entity).getTarget() == null || cooldown == -4) {
                            cooldown = 60;
                            return true;
                        }
                        if (cooldown > 0) {  // значения: 0 -1 -2 -3 * запускают по 5 стрел
                            cooldown--;
                            return true;
                        }
                        AbstractArrow arrow = EntityType.ARROW.create(entity.level(), EntitySpawnReason.MOB_SUMMONED);
                        Vec3 vec3 = entity.position();
                        for (int i = 0; i <= 5; i++) {
                            double x = random.nextInt(15) - 7 + vec3.x;
                            double y = random.nextInt(5) + 3 + vec3.y;
                            double z = random.nextInt(15) - 7 + vec3.z;
                            arrow.snapTo(x, y, z, 0, 90);
                            arrow.shoot(0, -0.5, 0, 2.0f, 1.0f);
                            arrow.setBaseDamage(2.0);
                            entity.level().addFreshEntity(arrow);
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 20;
                    }
                }, // abilities
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        entity.addEffect(INVISIBILITY); // неуязвимость в классе типа.
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
                        for (Entity craftTarget : entity.level().getEntities(entity, entity.getBoundingBox().inflate(20, 12, 20))) {
                            if (craftTarget instanceof LivingEntity target) {
                                target.addEffect(BLINDNESS);
                                target.addEffect(SLOWNESS);
                            }
                        }
//                        LivingEntity target = ((Creature) entity).getTarget();
//                        if (target != null && chance(0.1)) {
//                            target.addPotionEffect(effects[random.nextInt(effects.length)]);
//                            target.getWorld().spawnParticle(Particle.WITCH, target.getEyeLocation(), 8, 0.6, 1.2, 0.6);
//                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 2400;
                    }
                }
        );
    }

    @Override
    public boolean onTarget(org.bukkit.entity.Entity target) {
        return true;
    }

    @Override
    public boolean onAttack(org.bukkit.entity.LivingEntity target) {
        return true;
    }

    private Long cooldown = 0L;
    @Override
    public void onDamage(MobDamageCE event, org.bukkit.entity.LivingEntity damager) {
        long millis = System.currentTimeMillis();
        if (millis < cooldown) return;
        this.cooldown = millis + 35_000;
        damager.damage(event.getBaseDamage());
    }

    @Override
    public void onDeath() {

    }
}
