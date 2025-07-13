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

    private final SoundEvent ambient = getSoundFromString("custom.melanox.idle");
    private final SoundEvent step = getSoundFromString("custom.melanox.walk");
    private final SoundEvent hurt = getSoundFromString("custom.melanox.damage");
    private final SoundEvent death = getSoundFromString("custom.melanox.death");
    private final SoundEvent attack = getSoundFromString("custom.melanox.attack");
    @Override
    protected SoundEvent getAmbientSound() {
        return ambient;
    }

    @Override
    protected void playStepSound(BlockPos blockposition, BlockState iblockdata) {
        this.playSound(step, 0.15F, 1.0F);
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
