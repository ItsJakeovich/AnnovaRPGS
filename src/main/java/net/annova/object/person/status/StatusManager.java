package net.annova.object.person.status;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public Status addStatus(StatusType type) {
        Status status = new Status(type);
        this.statuses.put(type, status);
        this.update();
        return status;
    }
    public void removeStatus(StatusType type) {
        if (statuses.containsKey(type)) {
            RPGStatus status = statuses.get(type).getActivity();
            if (status != null) status.stop(type);
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
