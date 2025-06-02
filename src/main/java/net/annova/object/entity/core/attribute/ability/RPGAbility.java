package net.annova.object.entity.core.attribute.ability;

import java.util.HashMap;
import java.util.Map;

public class RPGAbility {
    public enum Type {
        RESISTANCE, // сопративление урону
        KNOCKBACK_RESISTANCE, // сопративление отталкиванию
        LEVELLER, // слепота при ударе
        SCHIVARE, // уклонение
        SLOWDOWN, // наложение замедление при нанесении урона
        AVOIDANCE, // телепорт после получения урона и невидимость
        VAMPIRISM, // вампиризм
        VITIATION, // накладывает один из 3: слепота, замедление, тошнота при нанесении урона
        REFLECT, // шанс отразить удар
        EXPLOSION_NEUTRALITY,
        IMMORTAL,
        SPIKES,
        POISON,
        ;

        public RPGAbility get() {
            return new RPGAbility(this);
        }
    }

//    public abstract void mechanic(Event event);

    private final RPGAbility.Type type;
    private final Map<ValueType, Double> values = new HashMap<>();

    public RPGAbility(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Double getValue(ValueType type) {
        if (!values.containsKey(type)) return 1.0;
        return values.get(type);
    }
    public RPGAbility setValue(ValueType type, double value) {
        this.values.put(type, value);
        return this;
    }
}
