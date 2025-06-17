package net.annova.object.entity.mobs.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_21_R4.potion.CraftPotionEffectType;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public class CustomOlderEnt extends IronGolem {
    public CustomOlderEnt(EntityType<? extends IronGolem> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean doHurtTarget(ServerLevel worldserver, Entity entity) {
        if (super.doHurtTarget(worldserver, entity)) {
            if (entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(
                                CraftPotionEffectType.bukkitToMinecraftHolder(PotionEffectType.SLOWNESS), 60, 1, true, false),
                        EntityPotionEffectEvent.Cause.PLUGIN
                );
            }
            return true;
        } else return false;
    }

    @Override
    public boolean hurtServer(ServerLevel worldserver, DamageSource damagesource, float f) {
        if (damagesource.is(DamageTypes.ON_FIRE)) f*=3;
        return super.hurtServer(worldserver, damagesource, f);
    }
}
