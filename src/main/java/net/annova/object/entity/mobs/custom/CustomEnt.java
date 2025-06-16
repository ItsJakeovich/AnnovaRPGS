package net.annova.object.entity.mobs.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_21_R4.potion.CraftPotionEffectType;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public class CustomEnt extends Zombie {
    public CustomEnt(EntityType<? extends Zombie> entitytypes, Level world) {
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
}
