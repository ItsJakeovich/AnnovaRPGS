package net.annova.thread;

import net.annova.entity.traits.status.Status;
import net.annova.object.person.RPGPerson;
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

            long millis = System.currentTimeMillis();
            Set<Status.Type> toRemove = new HashSet<>();
            for (Status status : person.getStatuses()) {
                if (status.isNotExpired()) continue;
                person.removeStatus(status.getType());
                toRemove.add(status.getType());
            }
            toRemove.forEach(person::removeStatus);
        }
    }
}