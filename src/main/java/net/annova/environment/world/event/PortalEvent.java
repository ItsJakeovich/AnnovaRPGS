package net.annova.environment.world.event;

import net.annova.event.custom.PostPortalTeleportCE;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PortalEvent implements Listener {
    @EventHandler
    private void portal(PostPortalTeleportCE event) {
        event.getPerson().getPerson().send("{b}Сплю, сплю, бам, бам, сплю, бам. Идем работать.");
    }
}
