package net.annova.thread;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.type.Prison;
import net.annova.object.person.Vec3iSelection;
import net.annova.thread.event.HourTickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.environment.world.GlobalWorld.getWorldSector;
import static net.annova.environment.world.GlobalWorld.getWorldSectors;

public class DungeonTickEvent implements Listener {
    private int cooldown = 3; // раз в 3 часа
    @EventHandler
    public void run(HourTickEvent event) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        for (ServerLevel level : getWorldSectors().keySet()) {
            createRandomPosDungeon(level);
        }
    }

    public static BlockPos createRandomPosDungeon(ServerLevel level) {
        BlockPos.MutableBlockPos vec = new BlockPos.MutableBlockPos();
        Vec3iSelection selection = getWorldSector(level).border();
        while (true) {
            vec.set(selection.getRandomPoint());
            vec.setY(level.getChunk(vec.getX() >> 4, vec.getZ() >> 4, ChunkStatus.EMPTY, true)
                    .getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, vec.getX(), vec.getZ())
            );
            if (level.getBlockState(vec).is(Blocks.COMMAND_BLOCK)) continue;
            break;
        }
        new Dungeon(new Prison(), vec, level).createDungeon();
        return vec;
    }
}
