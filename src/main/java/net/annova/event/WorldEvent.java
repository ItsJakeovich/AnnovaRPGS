package net.annova.event;

import net.annova.object.person.advancement.RPGAdvancement;
import net.annova.object.person.RPGPerson;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.GlobalRPG.*;

public class WorldEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!containsRPGPerson(player)) addRPGPerson(getPerson(player));
        RPGPerson person = getRPGPerson(player);
        person.getEquipment().update();
        person.updateAttributes();
        person.updateHealth();

        for (RPGAdvancement rpg : RPGAdvancement.values()) {
            Advancement advancement = rpg.resolve();
            if (advancement == null) {
                sendInfo("| Missing advancement: " + rpg.getKey().getKey());
                continue;
            }
            if (player.getAdvancementProgress(advancement).isDone()) {
                person.addAdvancement(rpg);
            }
        }
        person.getPerson().send("{a}Сервер сейчас находится на стадии бета-теста. Возможны различные недочёты или баги, но именно вместе с вами мы сделаем проект лучше! Спасибо, что помогаете нам развиваться. Желаем вам приятной игры и ярких впечатлений!");
    }

    @EventHandler
    public void change(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World.Environment world = player.getWorld().getEnvironment();

        getRPGPerson(player).updateHealth();
    }
}
