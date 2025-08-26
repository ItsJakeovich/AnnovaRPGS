package net.annova.environment.world.sector0.location.event;

import net.annova.environment.world.sector0.location.Meteorite;
import net.annova.event.custom.NonCommandBlockRestrictionCE;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.core.RPGEntityType.METEORITE_GOLEM;

public class MeteoriteEvent implements Listener {
    @EventHandler
    public void breakBlock(NonCommandBlockRestrictionCE event) {
        if (event.getType() == NonCommandBlockRestrictionCE.Type.BREAK) {
            ServerPlayer player = event.getPlayer();
            if (getRPGPerson(player).getSectorLocation() instanceof Meteorite) {
                METEORITE_GOLEM.tryNaturalSpawn(player.position(), player.serverLevel(), 5, false);
            }
        }
    }
}
