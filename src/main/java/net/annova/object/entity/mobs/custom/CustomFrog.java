package net.annova.object.entity.mobs.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CustomFrog extends Frog {
    public CustomFrog(EntityType<? extends Animal> var0, Level var1) {
        super(var0, var1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 3.5));
    }

    @Override
    public boolean hurtServer(ServerLevel worldserver, DamageSource damagesource, float f) {
        if (damagesource.is(DamageTypes.FALL)) return false;
        return super.hurtServer(worldserver, damagesource, f);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Vec3 vec3d = this.getDeltaMovement();
        if (!this.onGround()) {
            if (vec3d.y < 0.0F) {
                this.setDeltaMovement(vec3d.multiply((double) 1.0F, 0.8, (double) 1.0F));
            } else if (vec3d.y > 0.0F) {
                this.setDeltaMovement(vec3d.multiply((double) 1.0F, 0.4, (double) 1.0F));
            }
        }
    }
}
