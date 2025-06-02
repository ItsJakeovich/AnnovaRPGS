package net.annova.thread;

import net.annova.object.person.status.Status;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class UpdateStatusTimer extends AnnovaAsyncTask {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            RPGPerson person = getRPGPerson(player);
            person.updateSet();
            person.updateArmor();
            person.regen();

//            long millis = System.currentTimeMillis();
//            for (String modifier : person.getSpeedModifiers().keySet()) {
//                if (millis-person.getSpeedModifier(modifier) < 11000) continue;
//                person.removeSpeedModifier(modifier);
//            }

            Set<StatusType> toRemove = new HashSet<>();
            StatusManager manager = person.getStatusManager();
            for (Status status : manager.getStatuses()) {
                if (status.toRemove(player)) {
                    toRemove.add(status.getType());
                }
            }
            toRemove.forEach(manager::removeStatus);
        }
    }
}