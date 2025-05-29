package net.annova.event;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;

public class BedEvent implements Listener {
    public int sleep = 0;
    @EventHandler
    public void bed(PlayerBedEnterEvent event) {
        if (event.isCancelled()) return;
        sleep++;
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(
                            player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(" ")));
            getPerson(event.getPlayer()).sendBar(getInstance(), "night-enter");
        }, 1L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
            if (sleep >= Math.max(Bukkit.getOnlinePlayers().size() / 2.5, 1)) {
                World world = event.getPlayer().getWorld();
                world.setFullTime((world.getFullTime() / 24000 + 1) * 24000);
                Bukkit.getOnlinePlayers().forEach(
                        player -> getPerson(player).send(getInstance(), "night-skip")
                );
            }
        }, 100);
    }
    @EventHandler
    public void leave(PlayerBedLeaveEvent event) {
        sleep = Math.max(sleep-1, 0);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(),
                () -> Bukkit.getOnlinePlayers().forEach(
                        player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(" "))
                ), 1L);
    }
}
