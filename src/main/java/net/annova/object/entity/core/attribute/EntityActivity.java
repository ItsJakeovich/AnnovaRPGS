package net.annova.object.entity.core.attribute;

import org.bukkit.entity.LivingEntity;

public interface EntityActivity {
    boolean activity();
    long repeating();
    boolean syncActivity(LivingEntity entity);
    long syncRepeating();
}
