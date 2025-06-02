package net.annova.object.entity.core;

import org.bukkit.entity.LivingEntity;

public interface RPGActivity {
    boolean activity();
    long repeating();
    boolean syncActivity(LivingEntity entity);
    long syncRepeating();
}
