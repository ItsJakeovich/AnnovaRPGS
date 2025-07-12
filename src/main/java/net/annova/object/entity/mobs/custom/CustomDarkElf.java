package net.annova.object.entity.mobs.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static net.annova.main.GlobalRPG.getSoundFromString;
import static net.minecraft.world.effect.MobEffects.INVISIBILITY;

public class CustomDarkElf extends Bogged {
    public CustomDarkElf(EntityType<? extends Bogged> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean hurtServer(ServerLevel worldserver, DamageSource damagesource, float f) {
        if (this.hasEffect(INVISIBILITY)) return false;
        return super.hurtServer(worldserver, damagesource, f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getSoundFromString("custom.darkelf.idle");
    }

    @Override
    protected SoundEvent getStepSound() {
        return getSoundFromString("custom.darkelf.walk");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource var0) {
        return getSoundFromString("custom.darkelf.damage");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return getSoundFromString("custom.darkelf.death");
    }

    @Override
    protected void playAttackSound() {
        this.playSound(getSoundFromString("custom.darkelf.attack"), 1.0f, 1.0f);
    }
}
