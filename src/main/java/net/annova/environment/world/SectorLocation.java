package net.annova.environment.world;

import net.annova.object.Vec3iSelection;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;

import java.util.EnumSet;

import static net.annova.util.BukkitObjects.random;

public abstract class SectorLocation {
    public abstract void movement(RPGPerson person, ServerPlayer player);

    public abstract EnumSet<RPGEntityType> entitiesToSpawn();
    public abstract Vec3iSelection[] borders();

    public boolean isOutside(Location location) {
        return !this.isInside(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    public boolean isOutside(Vec3i position) {
        return !this.isInside(position.getX(), position.getY(), position.getZ());
    }

    public boolean isInside(Location location) {
        return this.isInside(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    public boolean isInside(Vec3i position) {
        return this.isInside(position.getX(), position.getY(), position.getZ());
    }
    public boolean isInside(int posX, int posY, int posZ) {
        for (Vec3iSelection locationPoint : this.borders()) {
            Vec3i min = locationPoint.getMinPoint(), max = locationPoint.getMaxPoint();
            if (posX < min.getX() || posY < min.getY() || posZ < min.getZ()) continue;
            if (posX > max.getX() || posY > max.getY() || posZ > max.getZ()) continue;
            return true;
        }
        return false;
    }

    private RPGEntity boss;

    public RPGEntity getBoss() {
        return boss;
    }
    public void setBoss(RPGEntity boss) {
        this.boss = boss;
    }

    public static Vec3 getChorusLocation(Vec3 position, ServerLevel level, int radius) {
        if (radius == 0) return position;

        for (int i = 0; i < radius*radius; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2 * radius;
            double offsetY = (random.nextDouble() - 0.5) * radius;
            double offsetZ = (random.nextDouble() - 0.5) * 2 * radius;

            Vec3 targetPos = position.add(offsetX, offsetY, offsetZ);
            BlockPos blockPos = BlockPos.containing(targetPos);

            BlockPos above = blockPos.above();

            if (!level.getBlockState(blockPos.below()).isSolidRender()) continue;
            if (level.getBlockState(blockPos).isSolidRender()) continue;
            if (level.getBlockState(blockPos.above()).isSolidRender()) continue;

            return Vec3.atBottomCenterOf(blockPos);
        }
        return position; // fallback
    }

    public void showTerritory(ServerLevel level) {
        Vec3iSelection check = this.borders()[0];
        BlockState toSet = level.isEmptyBlock(new BlockPos(check.getMinPoint()))
                ? Blocks.REDSTONE_BLOCK.defaultBlockState()
                : Blocks.AIR.defaultBlockState();
        for (Vec3iSelection location : this.borders()) {
            Vec3i min = location.getMinPoint(), max = location.getMaxPoint();
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                for (int x = min.getX(); x <= max.getX(); x++) {
                    BlockPos pos = BlockPos.containing(x, 300, z);
                    BlockState state = level.getBlockState(pos);
                    if (state.isAir() || state.is(Blocks.REDSTONE_BLOCK)) {
                        level.setBlock(pos, toSet, 1);
                    }
                }
            }
        }
    }
}
