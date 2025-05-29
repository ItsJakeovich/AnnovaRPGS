package net.annova.entity.traits.status;

import net.annova.object.person.RPGPerson;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class Charging extends RPGStatus implements Listener {
    @Override
    public boolean activity(RPGPerson person, Player player, int max) {
        Status status = person.getStatus(Status.Type.CHARGING);
        double charge = status.getValue("charge");
        person.getPerson().sendBar(getInstance(), "weapon-cast", "{percent}", String.valueOf((int) (charge / max * 100)));
        if (charge == max) {
            if (status.getValue("charged") == null) {
                player.playSound(player.getLocation(), Sound.ITEM_WOLF_ARMOR_REPAIR, 1, 1);
                status.setValue("charged", 1);
            }
            return true;
        }

        player.playSound(player.getLocation(), Sound.ITEM_DYE_USE, 1, 1);
        status.setValue("charge", charge + 1);
        return false;
    }

    @EventHandler
    private void handSwitch(PlayerItemHeldEvent event) {
        RPGPerson person = getRPGPerson(event.getPlayer());
        if (person.getStatus(Status.Type.CHARGING) == null) return;
        person.removeStatus(Status.Type.CHARGING);
    }
}
