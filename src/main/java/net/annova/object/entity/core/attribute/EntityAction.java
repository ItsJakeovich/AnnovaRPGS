package net.annova.object.entity.core.attribute;

import net.annova.event.custom.MobDamageCE;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface EntityAction {
    boolean onTarget(Entity target);
    boolean onAttack(LivingEntity target);
    void onDamage(MobDamageCE event, LivingEntity damager);
    void onDeath();
}
