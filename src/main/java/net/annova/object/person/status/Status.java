package net.annova.object.person.status;

import net.annova.item.base.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class Status {
    private final StatusType type;
    private BukkitTask runnable = null;
    private final Map<ValueType, Double> values = new HashMap<>();
    private final long expires;

    public Status(StatusType StatusType) {
        this.type = StatusType;
        this.values.putAll(StatusType.getBaseValues());
        this.expires = System.currentTimeMillis()+StatusType.getDuration();
    }

    public StatusType getType() {
        return type;
    }

    public Double getValue(ValueType StatusType) {
        return values.getOrDefault(StatusType, 0.0);
    }
    public Status setValue(ValueType StatusType, double value) {
        this.values.put(StatusType, value);
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
        boolean byTime = expires != 0 && System.currentTimeMillis() >= expires;

        Item item = this.type.getWhileHas();
        boolean byItem = item != null && !item.has(player);

        return byTime || byItem;
    }
}
