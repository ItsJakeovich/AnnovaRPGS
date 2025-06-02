package net.annova.object.entity.core;

import java.util.HashMap;
import java.util.Map;

public enum AbilityType {
    RESISTANCE, // сопративление урону
    KNOCKBACK_RESISTANCE, // сопративление отталкиванию
    LEVELLER, // слепота при ударе
    SCHIVARE, // уклонение
    SLOWDOWN, // наложение замедление при нанесении урона
    AVOIDANCE, // телепорт после получения урона и невидимость
    VAMPIRISM, // вампиризм
    VITIATION, // накладывает один из 3: слепота, замедление, тошнота при нанесении урона
    REFLECT, // шанс отразить удар
    ORATOR, // увеличение урона ближайшим союзникам (урон, здоровье, регенерация)
    EXPLOSION_NEUTRALITY,
    IMMORTAL,
    SPIKES
    ;

//    public abstract void mechanic(Event event);

    private final Map<ValueType, Double> values = new HashMap<>();

    public Double getValue(ValueType type) {
        if (!values.containsKey(type)) return 1.0;
        return values.get(type);
    }
    public AbilityType setValue(ValueType type, double value) {
        this.values.put(type, value);
        return this;
    }
}
