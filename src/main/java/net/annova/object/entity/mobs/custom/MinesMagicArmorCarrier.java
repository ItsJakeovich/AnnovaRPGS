package net.annova.object.entity.mobs.custom;

import net.annova.object.person.status.StatusType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.Nullable;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.ENT_CORE;
import static net.annova.util.BukkitObjects.chance;

public class MinesMagicArmorCarrier extends IronGolem {
    public MinesMagicArmorCarrier(EntityType<? extends IronGolem> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean setTarget(@Nullable LivingEntity target, @Nullable EntityTargetEvent.TargetReason reason) {
        if (reason == EntityTargetEvent.TargetReason.CLOSEST_PLAYER && target instanceof Player player) if (ENT_CORE.has(player)) return false;
        return super.setTarget(target, reason);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, (double)1.0F, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public boolean doHurtTarget(ServerLevel worldserver, Entity entity) {
        if (super.doHurtTarget(worldserver, entity)) {
            if (entity instanceof Player player && chance(0.5)) {
                getRPGPerson(player.getUUID()).addStatus(StatusType.PARALYSIS);
                player.playSound(SoundEvents.ANVIL_FALL, 1, 1);
            }
            return true;
        }
        return false;
    }
}
