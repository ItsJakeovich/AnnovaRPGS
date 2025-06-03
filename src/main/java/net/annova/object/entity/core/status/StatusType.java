package net.annova.object.entity.core.status;

import net.annova.object.person.status.ValueType;

import java.util.EnumMap;
import java.util.Map;

import static net.annova.object.person.status.ValueType.ORATOR_DAMAGE_BUFF;

public enum StatusType {
    LEADER_DAMAGE_BUFF(Map.of(ORATOR_DAMAGE_BUFF, 0.15))
    ;

    private final Map<ValueType, Double> values = new EnumMap<>(ValueType.class);

    StatusType(Map<ValueType, Double> values) {
        this.values.putAll(values);
    }

    public Map<ValueType, Double> getBaseValues() {
        return values;
    }
    public double getValue(ValueType type) {
        return this.values.get(type);
    }
}
