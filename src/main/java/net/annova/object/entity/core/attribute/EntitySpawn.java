package net.annova.object.entity.core.attribute;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;

public interface EntitySpawn {
    void onSpawn(ServerLevel level, Vec3 position);
}
