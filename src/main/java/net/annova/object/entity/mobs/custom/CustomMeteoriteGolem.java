package net.annova.object.entity.mobs.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static net.annova.main.GlobalRPG.getSoundFromString;
import static net.minecraft.world.damagesource.DamageTypes.IN_FIRE;
import static net.minecraft.world.damagesource.DamageTypes.ON_FIRE;

public class CustomMeteoriteGolem extends IronGolem {
    public CustomMeteoriteGolem(EntityType<? extends IronGolem> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, (double)1.0F, true) {
            @Override
            protected int getAttackInterval() {
                if (CustomMeteoriteGolem.this.getHealth() / CustomMeteoriteGolem.this.getMaxHealth() < 0.4)
                    return this.adjustedTickDelay(13);
                return this.adjustedTickDelay(20);
            }
        });
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }


    @Override
    public boolean hurtServer(ServerLevel worldserver, DamageSource damagesource, float f) {
        if (damagesource.is(ON_FIRE) || damagesource.is(IN_FIRE)) {
            this.heal(1.0f);
            return false;
        }
        return super.hurtServer(worldserver, damagesource, f);
    }

    private final SoundEvent ambient = getSoundFromString("custom.somethingo.idle");
    private final SoundEvent step = getSoundFromString("custom.somethingo.walk");
    private final SoundEvent hurt = getSoundFromString("custom.somethingo.damage");
    private final SoundEvent death = getSoundFromString("custom.somethingo.death");
    private final SoundEvent attack = getSoundFromString("custom.somethingo.attack");

    @Override
    protected SoundEvent getAmbientSound() {
        return ambient;
    }

    @Override
    protected void playStepSound(BlockPos blockposition, BlockState iblockdata) {
        this.playSound(step, 1.0F, 1.0F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource var0) {
        return hurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return death;
    }

    @Override
    protected void playAttackSound() {
        this.playSound(attack, 1.0f, 1.0f);
    }
}
