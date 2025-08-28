package net.annova.environment.world.event;

import net.annova.thread.event.SecondTickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class WorldUpdateTimer implements Listener {
    private static final List<UpdateTask> tasks = new ArrayList<>();

    @EventHandler
    public void tick(SecondTickEvent event) {
        for (UpdateTask task : tasks) {
            if (task.isAvailable()) task.update();
        }
    }

    public static void addTask(UpdateTask task) {
        tasks.add(task);
    }
}
