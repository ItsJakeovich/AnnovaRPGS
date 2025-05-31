package net.annova.object.person.status;

import java.util.HashMap;
import java.util.Map;

public enum StatusType {
    CB_DAMAGE_DEBUFF(0,0,new HashMap<>() {
        {
            this.put(ValueType.DAMAGE_DEBUFF, 0.5);
        }
    },null),

    HARRY_DEFENCE_DEBUFF(300,0,new HashMap<>(), null),

    ICENAGE(0, 0, new HashMap<>(), null),

    STOPPED(),

    FROZEN(),

    PASSIVE_SHIELD(),

    SHIELD_CHARGE(),

    IGNIS(1200, 0, new HashMap<>(), null),

    REPAIR_COOLDOWN(18000, 0, new HashMap<>(), null),

    BIOME_STONE(),

    REGENERATION(),

    TELEKINESIS(0, 0, new HashMap<>(), null),

    AURA_LIFE(0, 0, new HashMap<>(), null),

    GREEN_STONE_HEALTH_BUFF(0,0,new HashMap<>() {{
            this.put(ValueType.HEALTH_BUFF, 8.0);
        }},null),

    CAVIAR_DAMAGE_BUFF(60000, 0, new HashMap<>() {{
        this.put(ValueType.DAMAGE_BUFF, 12.0);
    }}, null),

    ELK_FISH_DAMAGE_BUFF(60000, 0, new HashMap<>() {{
            this.put(ValueType.DAMAGE_BUFF, 6.0);
        }}, null)
    ;

    private final long duration;
    private final long cooldown;
    private final Map<ValueType, Double> values;
    private final Class<? extends RPGStatus> activity;

    StatusType(long duration, long cooldown, Map<ValueType, Double> values, Class<? extends RPGStatus> activity) {
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

    public Class<? extends RPGStatus> getBaseActivity() {
        return activity;
    }
}
