package net.annova.object.entity.mobs.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.Nullable;

import static net.annova.main.GlobalRPG.getSoundFromString;
import static net.annova.object.item.base.ItemType.ENT_CORE;

public class MinesGolemBoss extends CustomIronGolem {
    public MinesGolemBoss(EntityType<? extends IronGolem> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean hurtServer(ServerLevel worldserver, DamageSource damagesource, float f) {
        if (this.getHealth() / this.getMaxHealth() <= 0.2) {
            f = f * 1.5f;
        }
        return super.hurtServer(worldserver, damagesource, f);
    }

    private final SoundEvent ambient = getSoundFromString("custom.eldergolem.idle");
    private final SoundEvent step = getSoundFromString("custom.eldergolem.walk");
    private final SoundEvent hurt = getSoundFromString("custom.eldergolem.damage");
    private final SoundEvent death = getSoundFromString("custom.eldergolem.death");
    private final SoundEvent attack = getSoundFromString("custom.eldergolem.attack");
    @Override
    public SoundEvent getAmbientSound() {
        return ambient;
    }

    @Override
    protected void playStepSound(BlockPos blockposition, BlockState iblockdata) {
        this.playSound(step, 1.0F, 1.0F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource var0) {
        return hurt;
    }

    @Override
    public SoundEvent getDeathSound() {
        return death;
    }

    @Override
    protected void playAttackSound() {
        this.playSound(attack, 1.0f, 1.0f);
    }
}
