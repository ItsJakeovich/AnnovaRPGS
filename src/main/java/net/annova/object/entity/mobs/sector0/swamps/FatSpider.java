package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.EntitySpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static net.annova.main.AnnovaAPI.random;
import static net.annova.object.item.base.ItemType.SPIDER_CHITIN;

public class FatSpider extends RPGEntity implements EntityAction, EntitySpawn {
    @Override
    public EntityType<?> type() {
        return EntityType.SPIDER;
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
        return 35;
    }

    @Override
    public double damage() {
        return 7;
    }

    @Override
    public float speed() {
        return 0.27f;
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
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        return true;
    }

    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
    }

    @Override
    public void onDeath() {
        AreaEffectCloud cloud = (AreaEffectCloud) entity.level().getWorld().spawnEntity(this.getLocation(), org.bukkit.entity.EntityType.AREA_EFFECT_CLOUD);

        cloud.setRadius(3.0f);
        cloud.setDuration(120); // 5s
        cloud.setReapplicationDelay(20);
        cloud.setWaitTime(0);
        cloud.setColor(Color.LIME);
        cloud.setParticle(Particle.ENTITY_EFFECT);

        cloud.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 100, 3), true); // 3 секунды яда 4
    }

    @Override
    public void onSpawn(Location location) {
        entity.getAttribute(Attributes.SCALE).setBaseValue(1.2);
    }
}
