package net.annova.event.personal;

import net.annova.entity.traits.status.Status;
import net.annova.object.Person;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class ChernuEvent implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals("chernu")) return;
        Person chernu = getPerson(player);
        getRPGPerson(chernu).addStatus(Status.Type.NETHER_EMPEROR);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) cancel();
                if (player.getWorld().getEnvironment() == World.Environment.NETHER) return;
                chernu.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "В Е Р Н И С Ь", "", 100, 200, 100);
            }
        }.runTaskTimer(getInstance(), 0L, 12000);
    }
}
