package net.annova.object.entity.mobs.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.purpurmc.purpur.entity.ai.HasRider;

public class CustomSwampSpider extends Spider {
    public CustomSwampSpider(EntityType<? extends Spider> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel worldserver, DamageSource damagesource, float f) {
        if (damagesource.is(DamageTypes.DROWN)) return false;
        return super.hurtServer(worldserver, damagesource, f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new SpiderAttackGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HasRider(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new SpiderTargetGoal<>(this, Player.class));
        this.targetSelector.addGoal(3, new SpiderTargetGoal<>(this, IronGolem.class));
    }
}
