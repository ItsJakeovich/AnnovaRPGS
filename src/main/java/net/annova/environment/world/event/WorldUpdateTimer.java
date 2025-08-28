package net.annova.environment.world.event;

import net.annova.thread.event.MinuteTickEvent;
import net.annova.thread.event.SecondTickEvent;
import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.List;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.object.item.base.ItemType.ELF_TREE_APPLE;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

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

    @EventHandler
    public void minuteTick(MinuteTickEvent event) {
        if (chance(0.2)) {
            world0.addFreshEntity(new ItemEntity(world0,
                    random.nextInt(-865, -825)+0.5,
                    75.5,
                    random.nextInt(-243, -203)+0.5,
                    ELF_TREE_APPLE.create(random.nextInt(6)+1)
            ), CreatureSpawnEvent.SpawnReason.CUSTOM);
        }
    }
}
