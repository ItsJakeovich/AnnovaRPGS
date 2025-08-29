package net.annova.thread;

import net.annova.object.person.RPGPerson;
import net.annova.thread.event.SecondTickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.environment.world.GlobalWorld.CHERNU_PERSON;
import static net.annova.object.RPGRarity.EPIC;

public class ChernuTickTimer implements Listener {
    @EventHandler
    public void tick(SecondTickEvent event) {
        RPGPerson person = CHERNU_PERSON;
        if (person != null && person.getPerson().isOnline()) {
            ServerPlayer player = person.getServerPlayer();
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
