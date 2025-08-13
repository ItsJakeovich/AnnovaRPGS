package net.annova.environment.civilization.event;

import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.build.TownBuild;
import net.annova.event.custom.NonCommandBlockRestrictionCE;
import net.annova.object.person.RPGPerson;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.GlobalRPG.getTownBuild;

public class BlockEvent implements Listener {
    @EventHandler
    public void blockPlaceBreak(NonCommandBlockRestrictionCE event) {
        ServerPlayer player = event.getPlayer();
        TownBuild build = getTownBuild(player.serverLevel(), event.getBlockPos());
        if (build == null) return;
        RPGPerson person = getRPGPerson(player);
        Town town = person.getTown();
        if (build.getConstruction().isProtectedBlock(event.getBlockPos())
                || town == null
                || (!town.getMembers().contains(person) && !town.getCivilization().getLeaders().contains(person))) {
            event.setCancelled(true);
        }
    }
}
