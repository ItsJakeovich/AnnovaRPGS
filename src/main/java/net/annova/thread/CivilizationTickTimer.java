package net.annova.thread;

import net.annova.environment.civilization.Civilization;
import net.annova.environment.civilization.Town;
import net.annova.thread.event.TenMinutesTickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getCivilizations;

public class CivilizationTickTimer implements Listener {
    @EventHandler
    public void tick(TenMinutesTickEvent event) {
        civilizationsTick();
    }

    public static void civilizationsTick() {
        for (Civilization civilization : getCivilizations()) {
            System.out.println(civilization.getName());
            for (Town town : civilization.getTowns()) town.updateConstruction();
        }
    }
}
