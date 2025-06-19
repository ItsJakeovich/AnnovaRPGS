package net.annova.object.entity.mobs.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Bogged;
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
    protected SoundEvent getAmbientSound() {
        return getSoundFromString("custom.darkelf.idle");
    }

    @Override
    protected SoundEvent getStepSound() {
        return super.getStepSound();
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
