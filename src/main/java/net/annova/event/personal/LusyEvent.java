package net.annova.event.personal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.annova.main.AnnovaRPG.getInstance;

public class LusyEvent implements Listener {
    @EventHandler
    public void lusy(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals("Rokoler")) return;

        String[][] message = {
                {ChatColor.GOLD + "Зайка, добро пожаловать", ChatColor.GOLD +"снова на сервер!"},
                {ChatColor.GOLD + "Не уверен, со мной ты зашла", ChatColor.GOLD + "или сама, но это не важно!"},
                {ChatColor.GOLD + "Надеюсь, тебе тут интересно!", ""},
                {ChatColor.GOLD + "Надеюсь, ты не забыла", ChatColor.GOLD + "покушать!"},
                {ChatColor.GOLD + "Я тебя так люблю", ChatColor.RED + "♡"},
                {ChatColor.GOLD + "Великий Джака ваши восхищается", ChatColor.RED + "&b<3"}
        };

        for (int i = 0; i < message.length; i++) {
            int finalI = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () ->
                    player.sendTitle(message[finalI][0], message[finalI][1], 10, 70, 20)
                    , 160 * i);
        }
    }
}
