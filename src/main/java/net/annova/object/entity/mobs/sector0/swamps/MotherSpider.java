package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.event.custom.MobDamageCE;
import net.annova.main.AnnovaAPI;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomSwampSpider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.entity.core.RPGEntityType.SWAMP_SPIDER;
import static net.annova.util.BukkitObjects.random;

public class MotherSpider extends RPGEntity implements CustomType, EntityAction {
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
        return ".";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 15;
    }

    @Override
    public double damage() {
        return 2;
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
        return List.of(
                createItem(Items.STRING, AnnovaAPI.random.nextInt(4)+1, 0.5)
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

    private final List<net.minecraft.world.entity.LivingEntity> summoned = new ArrayList<>();
    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
        List<net.minecraft.world.entity.LivingEntity> toRemove = new ArrayList<>();
        for (net.minecraft.world.entity.LivingEntity summon  : summoned) {
            if (summon.isAlive() && summon.valid) continue;
            toRemove.add(summon);
        }
        this.summoned.removeAll(toRemove);

        if (summoned.size() < 5) {
            for (int i = random.nextInt(2); i < 2; i++) { // от 1 до 2
                summoned.add(SWAMP_SPIDER.getEntity().create(entity));
            }
        }
    }

    @Override
    public void onDeath() {

    }
}
