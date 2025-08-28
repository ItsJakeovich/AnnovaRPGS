package net.annova.thread;

import net.annova.object.person.RPGPerson;
import net.annova.thread.event.SecondTickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.environment.world.GlobalWorld.CHERNU;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.RPGRarity.EPIC;

public class ChernuTickTimer implements Listener {
    private static final RPGPerson chernu = getRPGPerson(CHERNU);
    @EventHandler
    public void tick(SecondTickEvent event) {
        if (chernu != null && chernu.getPerson().isOnline()) {
            ServerPlayer player = chernu.getServerPlayer();
            BlockPos pos = player.blockPosition();
            player.serverLevel().sendParticles(EPIC.getDustOption(), true, false,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    3, 0.5, 0.1, 0.5,
                    0
            );
        }
    }
}
