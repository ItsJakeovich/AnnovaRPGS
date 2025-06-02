package net.annova.object.entity.core;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public interface EntityAction {
    boolean onTarget(Entity target);
    boolean onAttack(LivingEntity target);
    boolean onDamage(Entity damager);
    void onDeath();
}
