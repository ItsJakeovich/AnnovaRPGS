package net.annova.object.entity.core.attribute;

import net.annova.event.custom.MobDamageCE;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public interface EntityAction {
    boolean onTarget(Entity target);
    boolean onAttack(LivingEntity target);
    void onDamage(MobDamageCE event, LivingEntity damager);
    void onDeath();
}
