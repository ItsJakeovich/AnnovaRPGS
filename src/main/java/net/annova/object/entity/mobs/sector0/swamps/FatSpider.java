package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.Item.SPIDER_CHITIN;

public class FatSpider extends RPGEntity implements EntityAction {
    @Override
    public EntityType<?> type() {
        return EntityType.SPIDER;
    }

    @Override
    public String name() {
        return "FatSpider";
    }

    @Override
    public RPGRarity rarity() {
        return null;
    }

    @Override
    public int health() {
        return 200;
    }

    @Override
    public double damage() {
        return 9;
    }

    @Override
    public float speed() {
        return 0;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(SPIDER_CHITIN.create(player, 0.01));
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
    public boolean onDamage(Entity damager) {
        return true;
    }

    @Override
    public void onDeath() {
        AreaEffectCloud cloud = (AreaEffectCloud) entity.level().getWorld().spawnEntity(this.getLocation(), org.bukkit.entity.EntityType.AREA_EFFECT_CLOUD);

        cloud.setRadius(3.0f);
        cloud.setDuration(150); // 7,5s
        cloud.setReapplicationDelay(20);
        cloud.setWaitTime(0);
        cloud.setColor(Color.LIME);
        cloud.setParticle(Particle.ENTITY_EFFECT);

        cloud.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 100, 4), true); // 3 секунды яда II
    }
}
