package net.annova.object.person.status;

import net.annova.object.item.base.ItemType;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.scheduler.BukkitTask;

import java.util.EnumMap;
import java.util.Map;

public class Status {
    private final StatusType type;
    private BukkitTask runnable = null;
    private final Map<ValueType, Double> values = new EnumMap<>(ValueType.class);
    private final long expires;

    public Status(StatusType type, long durationOffset) {
        this.type = type;
        this.values.putAll(type.getBaseValues());
        long duration = type.getDuration()+durationOffset;
        if (duration == 0) expires = 0;
        else this.expires = System.currentTimeMillis() + duration;
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

    public boolean toRemove(ServerPlayer player) {
        boolean byTime = this.expires != 0 && System.currentTimeMillis() >= expires;

        ItemType itemType = this.type.getActivationItem();
        boolean byHasItem = itemType != null && !itemType.has(player);
        ItemType handItem  = this.type.getActivationInHandItem();
        boolean byHandItem = handItem != null && !handItem.equals(player.getMainHandItem());

        return byTime || byHasItem || byHandItem;
    }
}
