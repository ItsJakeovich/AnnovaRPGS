package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.EntitySpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomSwampSpider;
import net.annova.util.BukkitObjects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.main.AnnovaAPI.random;
import static net.annova.object.item.base.ItemType.SPIDER_CHITIN;
import static net.minecraft.world.effect.MobEffects.POISON;

public class FatSpider extends RPGEntity implements CustomType, EntityAction, EntitySpawn {
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
        return List.of(
                SPIDER_CHITIN.create(person, random.nextInt(3)+2, 0.45),
                createItem(Items.STRING, random.nextInt(2)+1, 0.5)
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
    public boolean onAttack(net.minecraft.world.entity.LivingEntity target) {
        return true;
    }

    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
    }

    @Override
    public void onDeath() {
        ServerLevel level = entity.level().getMinecraftWorld();
        AreaEffectCloud cloud = new AreaEffectCloud(EntityType.AREA_EFFECT_CLOUD, level);
        cloud.setRadius(3.0f);
        cloud.setDuration(120); // 5s
        cloud.reapplicationDelay = 20;
        cloud.setWaitTime(0);
        PotionContents old = cloud.potionContents;
        cloud.setPotionContents(new PotionContents(old.potion(), Optional.of(65280), old.customEffects(), old.customName()));
        cloud.setParticle(() -> ParticleTypes.ENTITY_EFFECT);

        cloud.addEffect(new MobEffectInstance(POISON, 100, 3));
    }

    @Override
    public void onSpawn(ServerLevel level, Vec3 position) {
        entity.getAttribute(Attributes.SCALE).setBaseValue(1.2);
    }
}
