package net.annova.object.entity.mobs.custom;

import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_21_R4.potion.CraftPotionEffectType;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffectType;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class CustomOlderEnt extends IronGolem {
    public CustomOlderEnt(EntityType<? extends IronGolem> entitytypes, Level world) {
        super(entitytypes, world);

        this.getAttribute(Attributes.SCALE).setBaseValue(3.0);
    }

    @Override
    public boolean setTarget(LivingEntity entityliving, EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        if (entityliving instanceof Player player) {
            RPGPerson person = getRPGPerson(player.getUUID());
            if (person.getStatusManager().hasStatus(StatusType.ENT_PATRON_BUFF)) return false;
        }
        return super.setTarget(entityliving, reason, fireEvent);
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
