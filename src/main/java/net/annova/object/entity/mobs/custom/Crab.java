package net.annova.object.entity.mobs.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Crab extends CaveSpider {
    public Crab(EntityType<? extends CaveSpider> entitytypes, Level world) {
        super(entitytypes, world);
        this.setInvisible(true);
        this.moveControl = new CrabMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0F, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.TURTLE_AMBIENT_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource var0) {
        return SoundEvents.TURTLE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.TURTLE_DEATH;
    }

    static class CrabMoveControl extends MoveControl {
        private final Crab crab;

        public CrabMoveControl(Crab var0) {
            super(var0);
            this.crab = var0;
        }

        public void tick() {
            LivingEntity var0 = this.crab.getTarget();
            if (this.crab.isInWater()) {
                this.crab.setDeltaMovement(this.crab.getDeltaMovement().add(0.0F, 0.002, 0.0F));

                if (this.operation != Operation.MOVE_TO || this.crab.getNavigation().isDone()) {
                    this.crab.setSpeed(0.0F);
                    return;
                }

                double var1 = this.wantedX - this.crab.getX();
                double var3 = this.wantedY - this.crab.getY();
                double var5 = this.wantedZ - this.crab.getZ();
                double var7 = Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
                var3 /= var7;
                float var9 = (float)(Mth.atan2(var5, var1) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.crab.setYRot(this.rotlerp(this.crab.getYRot(), var9, 90.0F));
                this.crab.yBodyRot = this.crab.getYRot();
                float var10 = (float)(this.speedModifier * this.crab.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float var11 = Mth.lerp(0.125F, this.crab.getSpeed(), var10);
                this.crab.setSpeed(var11);
                this.crab.setDeltaMovement(this.crab.getDeltaMovement().add((double)var11 * var1 * 0.005, (double)var11 * var3 * 0.1, (double)var11 * var5 * 0.005));
            } else {
                if (!this.crab.onGround()) {
                    this.crab.setDeltaMovement(this.crab.getDeltaMovement().add(0.0F, -0.008, 0.0F));
                }

                super.tick();
            }

        }
    }
}
