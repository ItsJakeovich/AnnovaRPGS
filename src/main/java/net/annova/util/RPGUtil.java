package net.annova.util;

import net.annova.object.person.status.ValueType;

import java.util.EnumMap;

public class RPGUtil {
    public static EnumMap<ValueType, Double> makeValueTypeMap(Object... entries) {
        EnumMap<ValueType, Double> map = new EnumMap<>(ValueType.class);
        for (int i = 0; i < entries.length; i+=2) {
            map.put((ValueType) entries[i], (Double) entries[i+1]);
        }
        return map;
    }
}
