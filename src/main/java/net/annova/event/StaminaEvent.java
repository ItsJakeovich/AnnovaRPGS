package net.annova.event;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.AnnovaGlobal.getPerson;

public class StaminaEvent implements Listener {
    @EventHandler
    public void jump(PlayerJumpEvent event) {
        getPerson(event.getPlayer()).decreaseStamina(0.05f);
    }
}
