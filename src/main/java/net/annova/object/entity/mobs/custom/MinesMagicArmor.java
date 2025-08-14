package net.annova.object.entity.mobs.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.Nullable;

import static net.annova.object.item.base.ItemType.ENT_CORE;
import static net.minecraft.world.item.Items.*;

public class MinesMagicArmor extends Zombie {
    public MinesMagicArmor(EntityType<? extends Zombie> entitytypes, Level world) {
        super(entitytypes, world);
        this.setInvisible(true);

        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(IRON_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(IRON_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(IRON_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(IRON_BOOTS));
    }

    @Override
    public boolean setTarget(@Nullable LivingEntity target, @Nullable EntityTargetEvent.TargetReason reason) {
        if (reason == EntityTargetEvent.TargetReason.CLOSEST_PLAYER && target instanceof Player player) if (ENT_CORE.has(player)) return false;
        return super.setTarget(target, reason);
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
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Zombie.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ARMOR_EQUIP_IRON.value();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource var0) {
        return SoundEvents.ANVIL_HIT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ANVIL_BREAK;
    }

    @Override
    public boolean isSunBurnTick() {
        return false;
    }
}
