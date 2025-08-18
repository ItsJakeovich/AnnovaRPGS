package net.annova.environment.civilization.event;

import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.build.TownBuild;
import net.annova.environment.world.SectorLocation;
import net.annova.event.custom.NonCommandBlockRestrictionCE;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

import static net.annova.environment.world.GlobalWorld.getWorldSector;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class BlockEvent implements Listener {
    private final MobEffectInstance FATIGUE = new MobEffectInstance(MobEffects.MINING_FATIGUE, 60, 2);
    @EventHandler
    public void blockPlaceBreak(NonCommandBlockRestrictionCE event) {
        ServerPlayer player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);
        SectorLocation location = person.getSectorLocation();
        if (location != null && location.isProtected()) {
            if (location.isProtected()) {
                event.setCancelled(true);
                return;
            } else player.addEffect(FATIGUE);
        }
        TownBuild build = getWorldSector(player.serverLevel()).getBuildAt(event.getBlockPos());
        if (build == null) return;
        person.getPerson().setStamina(0.005f);
        Town town = person.getTown();
        if (build.getConstruction().isProtectedBlock(event.getBlockPos())
                || town == null
                || (!town.getMembers().contains(person) && !town.getCivilization().getLeaders().contains(person))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void explosion(EntityExplodeEvent event) {
        List<Block> blocks = new ArrayList<>();
        ((CraftEntity) event.getEntity()). // тут не уверен, скорее всего в api
        for (Block block : event.blockList()) {
            BlockPos pos = ((CraftBlock) block).getPosition();

            blocks.add(block);
        }
        event.blockList().removeAll(blocks);
    }
}
