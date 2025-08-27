package net.annova.object.entity.mobs.custom;

import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffectType;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.GlobalRPG.getSoundFromString;
import static net.annova.object.item.base.ItemType.ENT_CORE;
import static net.minecraft.world.damagesource.DamageTypes.IN_FIRE;
import static net.minecraft.world.damagesource.DamageTypes.ON_FIRE;

public class CustomOlderEnt extends CustomIronGolem {
    public CustomOlderEnt(EntityType<? extends IronGolem> entitytypes, Level world) {
        super(entitytypes, world);

        this.getAttribute(Attributes.SCALE).setBaseValue(2.5);
    }



    @Override
    public boolean setTarget(LivingEntity entityliving, EntityTargetEvent.TargetReason reason) {
        if (reason == EntityTargetEvent.TargetReason.CLOSEST_PLAYER && entityliving instanceof Player player) {
            RPGPerson person = getRPGPerson(player.getUUID());
            if (person.getStatusManager().hasStatus(StatusType.ENT_PATRON_BUFF)) return false;
            if (ENT_CORE.has(player.getBukkitEntity())) return false;
        }
        return super.setTarget(entityliving, reason);
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
        if (damagesource.is(ON_FIRE)
                || damagesource.is(IN_FIRE)) f*=3;
        return super.hurtServer(worldserver, damagesource, f);
    }

    private final SoundEvent ambient = getSoundFromString("custom.olderent.idle");
    private final SoundEvent step = getSoundFromString("custom.olderent.walk");
    private final SoundEvent hurt = getSoundFromString("custom.olderent.damage");
    private final SoundEvent death = getSoundFromString("custom.olderent.death");
    private final SoundEvent attack = getSoundFromString("custom.olderent.attack");
    @Override
    public SoundEvent getAmbientSound() {
        return ambient;
    }
    @Override
    protected void playStepSound(BlockPos blockposition, BlockState iblockdata) {
        this.playSound(step, 1.0F, 1.0F);
    }
    @Override
    public SoundEvent getHurtSound(DamageSource var0) {
        return hurt;
    }
    @Override
    public SoundEvent getDeathSound() {
        return death;
    }
    @Override
    protected void playAttackSound() {
        this.playSound(attack, 1.0f, 1.0f);
    }
}
