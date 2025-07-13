package net.annova.object.entity.mobs.custom;

import net.annova.object.magic.physic.EntRoots;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.ENT_CORE;
import static net.minecraft.world.damagesource.DamageTypes.IN_FIRE;
import static net.minecraft.world.damagesource.DamageTypes.ON_FIRE;
import static net.minecraft.world.effect.MobEffects.SLOWNESS;

public class CustomEnt extends Zombie {
    public CustomEnt(EntityType<? extends Zombie> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean setTarget(LivingEntity entityliving, EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        if (entityliving instanceof Player player) {
            RPGPerson person = getRPGPerson(player.getUUID());
            if (person.getStatusManager().hasStatus(StatusType.ENT_PATRON_BUFF)) return false;
            if (ENT_CORE.has(player.getBukkitEntity())) return false;
        }
        return super.setTarget(entityliving, reason, fireEvent);
    }

    @Override
    public boolean doHurtTarget(ServerLevel worldserver, Entity entity) {
        if (super.doHurtTarget(worldserver, entity)) {
            if (entity instanceof LivingEntity living) {
                if (living.hasEffect(SLOWNESS)) {
                    EntRoots.addPhysicDebuff(living);
                }
                living.addEffect(new MobEffectInstance(
                        SLOWNESS, 60, 0, true, false),
                        EntityPotionEffectEvent.Cause.PLUGIN
                );
            }
            return true;
        } else return false;
    }

    @Override
    public boolean hurtServer(ServerLevel worldserver, DamageSource damagesource, float f) {
        if (damagesource.is(ON_FIRE)
                || damagesource.is(IN_FIRE)) f*=3;
        return super.hurtServer(worldserver, damagesource, f);
    }
}
