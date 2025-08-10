package net.annova.environment.civilization.build;

import net.annova.environment.civilization.Town;
import net.annova.lang.AISReplacement;
import net.annova.lang.PathReplacement;
import net.annova.object.SimpleBlock;
import net.annova.object.SimpleConstruction;
import net.annova.object.SimpleStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.GlobalTasks.buildConstructionTick;

public class TownBuild {
    private final Town town;
    private final BuildBase base;
    private final SimpleConstruction construction;
    private final float cost;

    public TownBuild(Town town, SimpleConstruction construction) {
        ServerLevel level = construction.getLevel();
        Vec3i position = construction.getBuildPoint();

        this.town = town;
        this.base = BuildBase.getBuildBase(construction.getStructure());
        this.construction = construction;

        float cost = base.getBaseCost();
        System.out.println("1: " + cost);
        SimpleStructure structure = construction.getStructure();
        int startX = position.getX(), startY = position.getY(), startZ = position.getZ();
        int number = 0;
        List<SimpleBlock> blocks = structure.getBlocks(construction.getDirection());
        for (int y = 0; y < structure.getMaxY(); y++) {
            for (int z = 0; z < structure.getMaxZ(); z++) {
                for (int x = 0; x < structure.getMaxX(); x++) {
                    BlockState state = level.getBlockState(new BlockPos(startX+x, startY+y, startZ+z));
                    if (blocks.get(number).getBlockState().getBlock() != state.getBlock()
                            && !state.isAir()) {
                        if (state.is(Blocks.DIRT)
                                || state.is(Blocks.GRASS_BLOCK)) cost += 0.5f;
                        else if (state.is(Blocks.STONE)
                                || state.is(Blocks.GRANITE)
                                || state.is(Blocks.DIORITE)
                                || state.is(Blocks.ANDESITE)) cost += 2f;
                    }
                    number++;
                }
            }
        }
        System.out.println("2: " + cost);

        this.cost = cost;
    }

    public SimpleConstruction getConstruction() {
        return construction;
    }

    public BuildBase getBase() {
        return base;
    }

    public float getCost() {
        return cost;
    }

    /**
     * Добавляет прогресс постройке здания
     * @param hammers - сколько будет построено относительно стоимости
     * @return {@code true} if construction is complete
     */
    public boolean nextTick(float hammers) {
        buildConstructionTick(this.construction, hammers / this.cost);
        if (this.construction.isComplete()) {
            this.town.getCivilization().send("build-complete",
                    new PathReplacement("{construction}", getInstance(), base.getLocal()),
                    new AISReplacement("{town}", this.town.getName())
            );
            return true;
        } else {
            town.send("build-tick",
                    new PathReplacement("{construction}", getInstance(), base.getLocal()),
                    new AISReplacement("{town}", this.town.getName()),
                    new AISReplacement("{percent}", String.valueOf((int) (this.construction.getProgress()*100)))
            );
            return false;
        }
    }
}
