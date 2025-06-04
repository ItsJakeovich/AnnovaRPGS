package net.annova.thread;

import net.annova.object.person.RPGPerson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class PersonUpdateTimer extends AnnovaAsyncTask {
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
        }
    }
}
