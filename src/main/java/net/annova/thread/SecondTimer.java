package net.annova.thread;

import net.annova.object.person.status.Status;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import net.annova.thread.event.SecondTickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class SecondTimer implements Listener {
    @EventHandler
    public void onTick(SecondTickEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            RPGPerson pers = getRPGPerson(player);
            Person person = pers.getPerson();
            if (pers.getStatus(Status.Type.STOPPED) != null) continue;

            person.increaseStamina(0.05f);
            pers.updateSpeed(130);
        }
    }

//    public static void decreasePlayerHealth(Player player, double damage) {
//        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) return;
//        double newHealth = player.getHealth()-damage;
//        if (newHealth <= 0) {
//            if (getRPGPerson(player).getStatus(Status.Type.NETHER_EMPEROR) != null) {
//                player.teleport(new Location(Bukkit.getWorld("world_nether"), 0, 100, 0));
//                return;
//            }
//        }
//        player.setHealth(Math.max(newHealth, 0));
//    }
}
