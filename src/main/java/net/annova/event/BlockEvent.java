package net.annova.event;

import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.build.TownBuild;
import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.WorldSector;
import net.annova.event.custom.NonCommandBlockRestrictionCE;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.environment.world.GlobalWorld.getWorldSector;
import static net.annova.environment.world.GlobalWorld.worldSectors;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class BlockEvent implements Listener {
    private final MobEffectInstance FATIGUE = new MobEffectInstance(MobEffects.MINING_FATIGUE, 60, 2);
    @EventHandler
    public void blockPlaceBreak(NonCommandBlockRestrictionCE event) {
        ServerPlayer player = event.getPlayer();
        if (player == null) return;
        RPGPerson person = getRPGPerson(player);
        ServerLevel level = event.getLevel();
        if (!worldSectors.containsKey(level) && !person.getPerson().isCanBreakProtectedBlocks()) {
            event.setCancelled(true);
            return;
        }

        // Утомляет игрока при взаимодействии с блоками
        person.getPerson().decreaseStamina(0.005f);

        SectorLocation location = person.getSectorLocation();
        if (location != null && location.isProtected()) {
            if (location.isProtected()) {
                event.setCancelled(true);
                return;
            } else player.addEffect(FATIGUE);
        }
        TownBuild build = getWorldSector(level).getBuildAt(event.getBlockPos());
        if (build == null) return;
        Town town = person.getTown();
        if (build.getConstruction().isProtectedBlock(event.getBlockPos())
                || town == null
                || (!town.getMembers().contains(person) && !town.getCivilization().getLeaders().contains(person))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void explosion(NonCommandBlockRestrictionCE event) {
        if (event.getType() == NonCommandBlockRestrictionCE.Type.EXPLOSION) {
            Vec3i pos = event.getBlockPos();
            WorldSector sector = getWorldSector(event.getLevel());
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
