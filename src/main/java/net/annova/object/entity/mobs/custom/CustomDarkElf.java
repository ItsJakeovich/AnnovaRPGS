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
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.annova.main.GlobalRPG.getSoundFromString;
import static net.minecraft.world.effect.MobEffects.INVISIBILITY;

public class CustomDarkElf extends Bogged {
    public CustomDarkElf(EntityType<? extends Bogged> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel worldserver, @NotNull DamageSource damagesource, float f) {
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
    public boolean isSunBurnTick() {
        return false;
    }

    @Override
    protected int getAttackInterval() {
        return super.getAttackInterval()-20;
    }

    @Override
    protected int getHardAttackInterval() {
        return super.getHardAttackInterval()-20;
    }

    @Override
    protected @NotNull AbstractArrow getArrow(@NotNull ItemStack arrow, float velocity, @Nullable ItemStack weapon) {
        return super.getArrow(arrow, velocity, weapon);
    }

    private final SoundEvent ambient = getSoundFromString("custom.darkelf.idle");
    private final SoundEvent step = getSoundFromString("custom.darkelf.walk");
    private final SoundEvent hurt = getSoundFromString("custom.darkelf.damage");
    private final SoundEvent death = getSoundFromString("custom.darkelf.death");
    private final SoundEvent attack = getSoundFromString("custom.darkelf.attack");
    @Override
    public @NotNull SoundEvent getAmbientSound() {
        return ambient;
    }

    @Override
    protected @NotNull SoundEvent getStepSound() {
        return step;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource var0) {
        return hurt;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return death;
    }

    @Override
    protected void playAttackSound() {
        this.playSound(attack, 1.0f, 1.0f);
    }
}
