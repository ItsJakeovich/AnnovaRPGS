package net.annova.object.entity.mobs.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static net.annova.main.GlobalRPG.getSoundFromString;

public class CustomMelanox extends Spider {
    public CustomMelanox(EntityType<? extends Spider> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getSoundFromString("custom.melanox.idle");
    }

    @Override
    protected void playStepSound(BlockPos blockposition, BlockState iblockdata) {
        this.playSound(getSoundFromString("custom.melanox.walk"), 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource var0) {
        return getSoundFromString("custom.melanox.damage");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return getSoundFromString("custom.melanox.death");
    }

    @Override
    protected void playAttackSound() {
        this.playSound(getSoundFromString("custom.melanox.attack"), 1.0f, 1.0f);
    }
}
