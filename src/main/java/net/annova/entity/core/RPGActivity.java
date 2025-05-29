package net.annova.entity.core;

import org.bukkit.entity.LivingEntity;

public interface RPGActivity {
    boolean activity(LivingEntity entity);
    long repeating();
    boolean syncActivity(LivingEntity entity);
    long syncRepeating();
}
