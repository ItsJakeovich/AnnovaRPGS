package net.annova.object.entity.mobs.sector0.mines;

import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntitySpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.mobs.custom.MinesMagicArmorNeutral;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.MAGIC_SCRAP;

public class Miner extends RPGEntity implements CustomType, EntitySpawn {
    @Override
    public Class<?> clazz() {
        return MinesMagicArmorNeutral.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Miner";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 45;
    }

    @Override
    public double damage() {
        return 3;
    }

    @Override
    public float speed() {
        return 0.3f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(MAGIC_SCRAP.create(0.03));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public void onSpawn(ServerLevel level, Vec3 position) {
        GoalSelector selector = ((Mob) entity).targetSelector;
        List<WrappedGoal> toRemove = new ArrayList<>();
        for (WrappedGoal goal : selector.getAvailableGoals()) {
            if (goal.getGoal() instanceof NearestAttackableTargetGoal) {
                toRemove.add(goal);
            }
        }
        for (WrappedGoal goal : toRemove) {
            selector.removeGoal(goal.getGoal());
        }
    }
}
