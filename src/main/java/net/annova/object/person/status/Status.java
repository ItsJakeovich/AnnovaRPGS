package net.annova.object.person.status;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Status {
    private final StatusType type;
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

    public RPGStatus getActivity() {
        try {
            return type.getBaseActivity().getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isNotExpired() {
        return expires == 0 || System.currentTimeMillis() < expires;
    }
}
