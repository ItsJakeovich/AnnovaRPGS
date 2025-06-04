package net.annova.object.entity.mobs.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.annova.object.entity.core.RPGEntity.register;
import static net.minecraft.world.item.Items.*;

public class MagicArmor extends Zombie {
//    public static final EntityType<MagicArmor> MAGIC_ARMOR;
//
//    static {
//        MAGIC_ARMOR = (EntityType<MagicArmor>) register("zombie", EntityType.Builder.of(MagicArmor::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8));
//    }

    public MagicArmor(EntityType<? extends Zombie> entitytypes, Level world) {
        super(entitytypes, world);
        this.setInvisible(true);

        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(IRON_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(IRON_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(IRON_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(IRON_BOOTS));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(MagicArmor.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ARMOR_EQUIP_IRON.value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource var0) {
        return SoundEvents.ANVIL_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ANVIL_BREAK;
    }
}
