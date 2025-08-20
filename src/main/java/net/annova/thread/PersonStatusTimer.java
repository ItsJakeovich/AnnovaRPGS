package net.annova.thread;

import net.annova.object.person.status.Status;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
import net.annova.thread.event.SecondTickEvent;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class PersonStatusTimer implements Listener {
    @EventHandler
    public void onTick(SecondTickEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            RPGPerson person = getRPGPerson(player);
            Set<StatusType> toRemove = new HashSet<>();
            StatusManager manager = person.getStatusManager();
            ServerPlayer serverPlayer = person.getServerPlayer();
            for (Status status : manager.getStatuses()) {
                if (status.toRemove(serverPlayer)) {
                    toRemove.add(status.getType());
                }
            }
            toRemove.forEach(manager::removeStatus);
        }
    }
}