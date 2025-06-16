package net.annova.object.entity.mobs.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.bukkit.event.entity.EntityKnockbackEvent;

import java.util.Optional;

public class CustomShards extends WitherSkeleton {
    public CustomShards(EntityType<? extends WitherSkeleton> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean doHurtTarget(ServerLevel worldserver, Entity entity) {
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        ItemStack itemstack = this.getWeaponItem();
        DamageSource damagesource = (DamageSource) Optional.ofNullable(itemstack.getItem().getDamageSource(this)).orElse(this.damageSources().mobAttack(this));
        f = EnchantmentHelper.modifyDamage(worldserver, itemstack, entity, damagesource, f);
        f += itemstack.getItem().getAttackDamageBonus(entity, f, damagesource);
        boolean flag = entity.hurtServer(worldserver, damagesource, f);
        if (flag) {
            float f1 = this.getKnockback(entity, damagesource);
            if (f1 > 0.0F && entity instanceof LivingEntity entityliving) {
                entityliving.knockback((double)(f1 * 0.5F), (double) Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))), this, EntityKnockbackEvent.KnockbackCause.ENTITY_ATTACK);
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, (double)1.0F, 0.6));
            }

            if (entity instanceof LivingEntity entityliving) {
                itemstack.hurtEnemy(entityliving, this);
                entityliving.setRemainingFireTicks(60);
            }

            EnchantmentHelper.doPostAttackEffects(worldserver, entity, damagesource);
            this.setLastHurtMob(entity);
            this.playAttackSound();
        }

        return flag;
    }

    /*@Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource var0) {
        return SoundEvents.WITHER_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_SKELETON_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockposition, BlockState iblockdata) {
        this.playSound(SoundEvents.WITHER_SKELETON_STEP, 0.15F, 1.0F);
    }*/
}
