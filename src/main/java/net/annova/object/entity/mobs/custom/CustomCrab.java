package net.annova.object.entity.mobs.custom;

import io.papermc.paper.event.entity.EntityKnockbackEvent;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.advancement.RPGAdvancement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.RPGItem.subtractItem;

public class CustomCrab extends CaveSpider {
    public CustomCrab(EntityType<? extends CaveSpider> entitytypes, Level world) {
        super(entitytypes, world);
        this.setInvisible(true);
        this.getAttribute(Attributes.SCALE);
        this.moveControl = new CrabMoveControl(this);
    }

    private static final Set<Item> FISH_ITEMS = Set.of(
            Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH
    );
    @Override
    protected @NotNull InteractionResult mobInteract(Player entityhuman, @NotNull InteractionHand enumhand) {
        ItemStack itemstack = entityhuman.getItemInHand(enumhand);
        if (FISH_ITEMS.contains(itemstack.getItem())) {
            RPGPerson person = getRPGPerson(entityhuman.getUUID());
            subtractItem(person.getPlayer());
            person.completeAdvancement(RPGAdvancement.CRAB_IS_HAPPY);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(entityhuman, enumhand);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0F, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(CaveSpider.class));
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel worldserver, @NotNull Entity entity) {
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        ItemStack itemstack = this.getWeaponItem();
        DamageSource damagesource = Optional.ofNullable(itemstack.getItem().getDamageSource(this)).orElse(this.damageSources().mobAttack(this));
        f = EnchantmentHelper.modifyDamage(worldserver, itemstack, entity, damagesource, f);
        f += itemstack.getItem().getAttackDamageBonus(entity, f, damagesource);
        boolean flag = entity.hurtServer(worldserver, damagesource, f);
        if (flag) {
            float f1 = this.getKnockback(entity, damagesource);
            if (f1 > 0.0F && entity instanceof LivingEntity entityliving) {
                entityliving.knockback(f1 * 0.5F, Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))), this, EntityKnockbackEvent.Cause.ENTITY_ATTACK);
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0F, 0.6));
            }

            if (entity instanceof LivingEntity entityliving1) {
                itemstack.hurtEnemy(entityliving1, this);
            }

            EnchantmentHelper.doPostAttackEffects(worldserver, entity, damagesource);
            this.setLastHurtMob(entity);
            this.playAttackSound();
        }

        return flag;
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel worldserver, DamageSource damagesource, float f) {
        if (damagesource.is(DamageTypes.DROWN)) return false;
        return super.hurtServer(worldserver, damagesource, f);
    }

    @Override
    public @NotNull SoundEvent getAmbientSound() {
        return SoundEvents.TURTLE_AMBIENT_LAND;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource var0) {
        return SoundEvents.TURTLE_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.TURTLE_DEATH;
    }

    static class CrabMoveControl extends MoveControl {
        private final CustomCrab crab;

        public CrabMoveControl(CustomCrab var0) {
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
                float var11 = Mth.lerp(0.3F, this.crab.getSpeed(), var10);
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
