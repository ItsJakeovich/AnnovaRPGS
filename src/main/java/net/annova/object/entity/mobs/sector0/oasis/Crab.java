package net.annova.object.entity.mobs.sector0.oasis;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.RPGSpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.Item.CRAB_CLAW;
import static net.annova.util.BukkitObjects.random;

public class Crab extends RPGEntity implements RPGSpawn {
    @Override
    public EntityType<?> type() {
        return EntityType.CAVE_SPIDER;
    }

    @Override
    public String name() {
        return "Crab";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 250;
    }

    @Override
    public double damage() {
        return 12;
    }

    @Override
    public float speed() {
        return 0.36f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(CRAB_CLAW.create(player, random.nextInt(3), 0.125));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public void onSpawn(Location location) {
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

    //дать покушать бедному крабу, еще он вроде бы не должен быть агрессивным
}
