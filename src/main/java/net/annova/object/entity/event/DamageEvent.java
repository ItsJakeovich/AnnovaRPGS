package net.annova.object.entity.event;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.ValueType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static net.annova.environment.world.SectorLocation.getChorusLocation;
import static net.annova.main.GlobalRPG.getRPGEntity;
import static net.annova.util.BukkitObjects.chance;
import static net.minecraft.world.effect.MobEffects.INVISIBILITY;

public class DamageEvent implements Listener {
    @EventHandler
    public void onDamage(MobDamageCE event) {
        LivingEntity entity = event.getEntity();
        RPGEntity rpg = getRPGEntity(entity.getUUID());
        if (rpg == null) return;

        for (RPGAbility ability : rpg.abilities()) {
            if (!chance(ability.getValue(ValueType.CHANCE))) continue;
            switch (ability.getType()) {
                case EXPLOSION_NEUTRALITY -> {
                    EntityDamageEvent.DamageCause cause = event.getEvent().getCause();
                    if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                            || cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) continue;
                    event.setCancelled(true);
                }
                case RESISTANCE -> event.increaseDamage(event.getBaseDamage()*ability.getValue(ValueType.MAIN));
                case AVOIDANCE -> {
                    Vec3 newPos = getChorusLocation(entity.position(), entity.level().getMinecraftWorld(),
                            ability.getValue(ValueType.RADIUS).intValue());
                    entity.teleportTo(newPos.x, newPos.y, newPos.z);
                    entity.addEffect(new MobEffectInstance(INVISIBILITY,
                            ability.getValue(ValueType.DURATION).intValue(), 0)
                    );
                }
                case IMMORTAL, SCHIVARE -> event.setCancelled(true);
            }
        }
    }
}
