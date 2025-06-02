package net.annova.object.person.status;

import net.annova.object.item.base.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class Status {
    private final StatusType type;
    private BukkitTask runnable = null;
    private final Map<ValueType, Double> values = new HashMap<>();
    private final long expires;

    public Status(StatusType type) {
        this.type = type;
        this.values.putAll(type.getBaseValues());
        this.expires = System.currentTimeMillis()+type.getDuration();
    }

    public StatusType getType() {
        return type;
    }

    public Double getValue(ValueType type) {
        return values.getOrDefault(type, 0.0);
    }
    public Status setValue(ValueType type, double value) {
        this.values.put(type, value);
        return this;
    }
    public Map<ValueType, Double> getValues() {
        return values;
    }

    public BukkitTask getRunnable() {
        return runnable;
    }
    public void setRunnable(BukkitTask runnable) {
        this.runnable = runnable;
    }

    public boolean toRemove(Player player) {
        boolean byTime = type.getDuration() != 0 && System.currentTimeMillis() >= expires;

        Item item = this.type.getWhileHas();
        boolean byItem = item != null && !item.has(player);

        return byTime || byItem;
    }
}
