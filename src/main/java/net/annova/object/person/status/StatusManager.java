package net.annova.object.person.status;

import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static net.annova.main.AnnovaRPG.getInstance;

public class StatusManager {
    private final Map<StatusType, Status> statuses = new HashMap<>();
    private final Map<ValueType, Double> finalValue = new HashMap<>() {{
        for (ValueType type : ValueType.values()) {
            this.put(type, 0.0);
        }
    }};

    @Nullable
    public Status getStatus(StatusType type) {
        return statuses.get(type);
    }
    public boolean hasStatus(StatusType type) {
        return statuses.containsKey(type);
    }
    public boolean containStatus(StatusType type) {
        return statuses.containsKey(type);
    }
    @Nullable
    public Status addStatus(RPGPerson person, StatusType type) {
        return this.addStatus(person, person.getPerson().getPlayer(), type);
    }
    @Nullable
    public Status addStatus(RPGPerson person, Player player, StatusType type) {
        Status status = new Status(type);
        this.statuses.put(type, status);
        StatusActivity activity = type.getActivity();
        if (activity == null) {
            status.setRunnable(new BukkitRunnable() {
                @Override
                public void run() {
                    if (activity.activity(person, player)) return;
                    this.cancel();
                }
            }.runTaskTimer(getInstance(), 0, activity.repeating()));
        }
        this.update();
        return status;
    }
    public void removeStatus(StatusType type) {
        if (statuses.containsKey(type)) {
            BukkitTask runnable = statuses.get(type).getRunnable();
            if (runnable != null) runnable.cancel();
            statuses.remove(type);
            this.update();
        }
    }

    private void update() {
        for (ValueType type : ValueType.values()) {
            finalValue.put(type, 0.0);
        }
        for (Status status : this.statuses.values()) {
            for (ValueType type : status.getValues().keySet()) {
                finalValue.merge(type, status.getValue(type), Double::sum);
            }
        }
    }
    public double getFinalValue(ValueType type) {
        return this.finalValue.get(type);
    }

    public Collection<Status> getStatuses() {
        return statuses.values();
    }
}
