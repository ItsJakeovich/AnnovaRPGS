package net.annova.event;

import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.build.TownBuild;
import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.WorldSector;
import net.annova.event.custom.CapitatorCE;
import net.annova.event.custom.NonCommandBlockRestrictionCE;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.environment.world.GlobalWorld.getWorldSector;
import static net.annova.environment.world.GlobalWorld.worldSectors;
import static net.annova.main.GlobalRPG.getAreaChunk;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemModulit.SEASMITH_PICKAXE;

public class BlockEvent implements Listener {
    @EventHandler
    public void capitator(CapitatorCE event) {
        BlockPos.MutableBlockPos pos = event.getPos();
        AreaChunk chunk = getAreaChunk(event.getLevel(), new ChunkPos(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())));
        if (chunk != null && chunk.getTown() != getRPGPerson(event.getPlayer()).getTown())
            event.setCancelled(true);
    }

    @EventHandler
    public void blockPlaceBreak(NonCommandBlockRestrictionCE event) {
        ServerPlayer player = event.getPlayer();
        if (player == null) return;
        RPGPerson person = getRPGPerson(player);
        if (person.getPerson().isCanBreakProtectedBlocks()) return;
        ServerLevel level = event.getLevel();
        if (!worldSectors.containsKey(level)) {
            event.setCancelled(true);
            return;
        }
        Vec3i pos = event.getBlockPos();
        AreaChunk chunk = getAreaChunk(level, new ChunkPos(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())));
        if (chunk != null && chunk.getTown() != person.getTown()) {
            event.setCancelled(true);
            return;
        }

        // Утомляет игрока при взаимодействии с блоками
        person.getPerson().decreaseStamina(0.005f);

        SectorLocation location = person.getSectorLocation();
        if (location != null) {
            if (location.isProtected()) {
                event.setCancelled(true);
                return;
            } else player.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 120, 1));
        }
        TownBuild build = getWorldSector(level).getBuildAt(pos);
        if (build != null) {
            Town town = person.getTown();
            if (build.getConstruction().isProtectedBlock(pos)
                    || town == null
                    || (!town.getMembers().contains(person) && !town.getCivilization().getLeaders().contains(person))) {
                event.setCancelled(true);
            }
        }

        if (!player.isShiftKeyDown() && event.getType() == NonCommandBlockRestrictionCE.Type.BREAK) {
            ItemStack item = player.getMainHandItem();
            if (item.isEmpty()) return;
            CustomData data = item.get(DataComponents.CUSTOM_DATA);
            if (data == null) return;
            if (SEASMITH_PICKAXE.hasModulit(data.getUnsafe())) {
                BlockPos destroyPos = new BlockPos(pos.below());
                BlockState state = level.getBlockState(destroyPos);
                if (!state.isDestroyable()) return;
                if (state.destroySpeed > level.getBlockState(new BlockPos(pos)).destroySpeed) return;

                level.destroyBlock(destroyPos, player.gameMode() != GameType.CREATIVE);
            }
        }
    }

    @EventHandler
    public void explosionPiston(NonCommandBlockRestrictionCE event) {
        if (event.getType() == NonCommandBlockRestrictionCE.Type.EXPLOSION
                || event.getType() == NonCommandBlockRestrictionCE.Type.PISTON) {
            Vec3i pos = event.getBlockPos();
            if (getAreaChunk(event.getLevel(), new ChunkPos(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) != null) {
                event.setCancelled(true);
                return;
            }
            WorldSector sector = getWorldSector(event.getLevel());
            if (sector == null) {
                event.setCancelled(true);
                return;
            }
            for (SectorLocation location : sector.zones()) {
                if (location.isInside(pos)) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (sector.getBuildAt(pos) != null) event.setCancelled(true);
        }
    }
}
