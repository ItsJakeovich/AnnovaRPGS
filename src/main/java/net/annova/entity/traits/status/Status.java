package net.annova.entity.traits.status;

import net.annova.environment.world.sector0.SuruWahare;

import java.util.HashMap;
import java.util.Map;

public class Status {
    public enum ValueType {
        DAMAGE_BUFF,
        DAMAGE_DEBUFF,

        DEFENCE
    }
    public enum Type {
        CB_DAMAGE_DEBUFF(0, 0, new HashMap<>() {{
            this.put(ValueType.DAMAGE_DEBUFF, 0.5);
        }}, null),
        DEFENCE_DEBUFF(),
        NETHER_EMPEROR(),
        STOPPED(),
        FROZEN(),
        PASSIVE_SHIELD(),
        SHIELD_CHARGE(),
        DAGGER_HITS(),
        CHARGING(0, null, new Charging()),
        IGNIS(),
        ICENAGE(),
        REPAIR_COOLDOWN(),
        BIOME_STONE(),
        REGENERATION(),
        TELEKINESIS(),
        AURA_LIFE(),

        CAVIAR_DAMAGE_BUFF,
        ELK_FISH_DAMAGE_BUFF
        ;

        private final long duration;
        private final long cooldown;
        private final Map<ValueType, Double> values;
        private final RPGStatus activity;
        Type(long duration, long cooldown, Map<ValueType, Double> values, RPGStatus activity) {
            this.duration = duration;
            this.cooldown = cooldown;
            this.values = values;
            this.activity = activity;
        }

        public long getDuration() {
            return duration;
        }
        public long getCooldown() {
            return cooldown;
        }
        public Map<ValueType, Double> getBaseValues() {
            return values;
        }
    }

    private final Type type;
    private final Map<ValueType, Double> values = new HashMap<>();
    private final long expires;

    public Status(Type type) {
        this.type = type;
        this.values.putAll(type.getBaseValues());
        this.expires = System.currentTimeMillis()+type.duration;
    }

    public Type getType() {
        return type;
    }

    public Double getValue(ValueType type) {
        return values.getOrDefault(type, 0.0);
    }
    public Status setValue(ValueType type, double value) {
        this.values.put(type, value);
        return this;
    }

    public RPGStatus getActivity() {
        return type.activity;
    }

    public boolean isNotExpired() {
        return expires == 0 || System.currentTimeMillis() < expires;
    }
}
