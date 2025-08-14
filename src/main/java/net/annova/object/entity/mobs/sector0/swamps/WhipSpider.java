package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.CustomRunnable;
import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomSwampSpider;
import net.annova.object.magic.projectile.WebBall;
import net.annova.object.person.status.StatusType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.SPIDER_CHITIN;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class WhipSpider extends RPGEntity implements CustomType, EntityAction, EntityActivity {
    @Override
    public Class<?> clazz() {
        return CustomSwampSpider.class;
    }
    @Override
    public EntityType<?> type() {
        return EntityType.SPIDER;
    }

    @Override
    public String name() {
        return "   ";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 55;
    }

    @Override
    public double damage() {
        return 5;
    }

    @Override
    public float speed() {
        return 0.33f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(SPIDER_CHITIN.create(person, random.nextInt(3)+2, 0.45));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public boolean onTarget(Entity target) {
        new WebBall().create(entity, target);
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        return true;
    }

    private final MobEffectInstance BLINDNESS = new MobEffectInstance(MobEffects.BLINDNESS, 40, 0);
    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
        if (entity.getHealth() < entity.getMaxHealth()*0.5 && chance(0.3)) {
            double reflection = event.getBaseDamage() * 0.9;
            event.decreaseDamage(reflection);
            if (reflection > 1) damager.hurtServer(damager.level().getMinecraftWorld(), damager.damageSources().mobAttack(entity), (float) reflection);

            if (damager instanceof Player player) {
                getRPGPerson(player).getStatusManager().addStatus(StatusType.PARALYSIS);
            }
            damager.addEffect(BLINDNESS);
        }
    }

    @Override
    public void onDeath() {
    }

    @Override
    public List<CustomRunnable> runnables() {
        return List.of();
    }
}
