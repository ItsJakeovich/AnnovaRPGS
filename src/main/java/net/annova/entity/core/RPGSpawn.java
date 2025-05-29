package net.annova.entity.core;

import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Location;

public interface RPGSpawn {
    void onSpawn(LivingEntity entity, Location location);
}
