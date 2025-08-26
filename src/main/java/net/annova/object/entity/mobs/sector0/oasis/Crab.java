package net.annova.object.entity.mobs.sector0.oasis;

import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntitySpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomCrab;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.CRAB_CLAW;
import static net.annova.util.BukkitObjects.random;
import static net.minecraft.world.entity.EntityType.CAVE_SPIDER;

public class Crab extends RPGEntity implements CustomType, EntitySpawn {
    @Override
    public Class<?> clazz() {
        return CustomCrab.class;
    }

    @Override
    public EntityType<?> type() {
        return CAVE_SPIDER;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 35;
    }

    @Override
    public double damage() {
        return 3;
    }

    @Override
    public float speed() {
        return 0.375f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(
                CRAB_CLAW.create(person, random.nextInt(2)+1, 0.33)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public void onSpawn(ServerLevel level, Vec3 position) {
        List<WrappedGoal> toRemove = new ArrayList<>();
        GoalSelector selector = ((Mob) entity).goalSelector;
        for (WrappedGoal goal : selector.getAvailableGoals()) {
            if (goal.getGoal() instanceof LeapAtTargetGoal) toRemove.add(goal);
            if (goal.getGoal() instanceof FloatGoal) toRemove.add(goal);
        }
        for (WrappedGoal goal : toRemove) {
            selector.removeGoal(goal);
        }
    }

    // Кормежка и нейтральность написаны в CustomCrab.java
}
