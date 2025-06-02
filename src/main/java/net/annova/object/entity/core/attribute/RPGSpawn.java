package net.annova.object.entity.core.attribute;

import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Location;

public interface RPGSpawn {
    void onSpawn(LivingEntity entity, Location location);
}
