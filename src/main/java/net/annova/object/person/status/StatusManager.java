package net.annova.object.person.status;

import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import static net.annova.main.AnnovaRPG.getInstance;

public class StatusManager {
    private final RPGPerson person;

    public StatusManager(RPGPerson person) {
        this.person = person;
    }

    private final Map<StatusType, Status> statuses = new EnumMap<>(StatusType.class);
    private final Map<ValueType, Double> finalValue = new EnumMap<>(ValueType.class) {{
        for (ValueType type : ValueType.values()) {
            this.put(type, 0.0);
        }
    }};

    public RPGPerson getPerson() {
        return person;
    }

    @Nullable
    public Status getStatus(StatusType type) {
        return statuses.get(type);
    }
    public boolean hasStatus(StatusType type) {
        return statuses.containsKey(type);
    }
    @Nullable
    public Status addStatus(StatusType type) {
        return this.addStatus(type, 0, null);
    }
    @Nullable
    public Status addStatus(StatusType type, long durationOffset) {
        return this.addStatus(type, durationOffset, null);
    }
    public Status addStatus(StatusType type, Object object) {
        return this.addStatus(type, 0, object);
    }
    private final Map<StatusType, Long> cooldowns = new EnumMap<>(StatusType.class);
    @Nullable
    public Status addStatus(StatusType type, long durationOffset, Object object) {
        Status status = new Status(type, durationOffset);
        long millis = System.currentTimeMillis();
        if (cooldowns.containsKey(type) && millis < cooldowns.get(type)) return status;

        if (type.getCooldown() != 0) cooldowns.put(type, millis+type.getCooldown());

        this.statuses.put(type, status);
        StatusActivity activity = type.getActivity();
        if (activity != null) {
            Player player = person.getPlayer();
            status.setRunnable(new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) return;
                    if (type.getActivationItem().has(player) && activity.activity(person, player, object)) return;

                    this.cancel();
                    StatusManager.this.removeStatus(type);
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
        this.person.updateAttributes();
    }

    /**
     * @return value of all status buffs (it can be < 0);
     */
    public double getFinalValue(ValueType type) {
        return this.finalValue.get(type);
    }

    public Collection<Status> getStatuses() {
        return statuses.values();
    }
}
