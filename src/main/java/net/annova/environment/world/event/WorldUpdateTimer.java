package net.annova.environment.world.event;

import net.annova.thread.event.MinuteTickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public class WorldUpdateTimer implements Listener {
    private static final Set<UpdateTask> tasks = new HashSet<>();

    @EventHandler
    public void tick(MinuteTickEvent event) {
        for (UpdateTask task : tasks) {
            if (task.isAvailable()) task.update();
        }
    }

    public static void addTask(UpdateTask task) {
        tasks.add(task);
    }
}
