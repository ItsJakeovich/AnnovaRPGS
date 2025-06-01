package net.annova.object.person.status;

import net.annova.item.base.Item;
import net.annova.object.person.RPGPerson;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.TreeType;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static net.annova.item.base.Item.*;
import static net.annova.item.crystal.DefenceStoneItem.DEFENCE_STONE_RESISTANCE;
import static net.annova.item.crystal.DefenceStoneItem.DEFENCE_STONE_SLOWNESS;
import static net.annova.object.person.status.ValueType.CHARGE;
import static net.annova.util.BukkitObjects.chance;

public enum StatusType {
    CB_DAMAGE_DEBUFF(0,0,new HashMap<>() {
        {
            this.put(ValueType.DAMAGE_DEBUFF, 0.5);
        }
    }),

    HARRY_DEFENCE_DEBUFF(300,0,new HashMap<>()),

    ICENAGE(0, 0, new HashMap<>()),

    STOPPED(0, 0, new HashMap<>()),

    FROZEN(0, 0, new HashMap<>()),

    PASSIVE_SHIELD(DEFENCE_STONE, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player) {
            player.addPotionEffect(DEFENCE_STONE_RESISTANCE);
            player.addPotionEffect(DEFENCE_STONE_SLOWNESS);
            return true;
        }

        @Override
        public long repeating() {
            return 100;
        }
    }),

    SHIELD_CHARGE(RUNIC_DEFENCE_STONE, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player) {
            Status status = person.getStatusManager().getStatus(SHIELD_CHARGE);
            double charges = status.getValue(CHARGE);
            if (charges < 3) status.setValue(CHARGE, charges + 1);

            return true;
        }

        @Override
        public long repeating() {
            return 18000;
        }
    }),

    IGNIS(1200),

    REPAIR_COOLDOWN(18000),

    BIOME_STONE(),

    REGENERATION(REGENERATION_STONE, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player) {
            person.regen(2);
            return true;
        }

        @Override
        public long repeating() {
            return 100;
        }
    }),

    TELEKINESIS(PSYONIC_STONE, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player) {
//            Описание: Можно «схватить»моба/предмет на расстоянии (например, до 10 блоков)
//            и переместить в другое место. Как мини-телекинез.
            return true;
        }

        @Override
        public long repeating() {
            return 5;
        }
    }),

    AURA_LIFE(NATURE_STONE, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -3; z <= 4; z++) {
                    for (int x = -5; x <= 5; x++) {
                        Block block = player.getLocation().add(x, y, z).getBlock();
                        Material type = block.getType();

                        // Проверяем тип блока и обновляем его состояние
                        if (type == Material.WHEAT || type == Material.CARROTS || type == Material.POTATOES || type == Material.BEETROOTS) {
                            if (block.getBlockData() instanceof Ageable ageable) {
                                if (ageable.getAge() == ageable.getMaximumAge()) continue;
                                ageable.setAge(ageable.getAge()+1);
                                block.setBlockData(ageable);
                            }
                        } else if (type == Material.GRASS_BLOCK) {
                            if (!chance(0.05)) continue;
                            block.getWorld().generateTree(block.getLocation(), TreeType.TREE); // Пример: вырастить дерево
                        }
                    }
                }
            }
            return true;
        }

        @Override
        public long repeating() {
            return 840;
        }
    }),

    GREEN_STONE_HEALTH_BUFF(GREEN_STONE, Map.of(ValueType.HEALTH_BUFF, 8.0)),

    CAVIAR_DAMAGE_BUFF(60000, Map.of(ValueType.DAMAGE_BUFF, 12.0)),

    ELK_FISH_DAMAGE_BUFF(60000,  Map.of(ValueType.DAMAGE_BUFF, 6.0)),
    ;

    private final long duration;
    private final long cooldown;
    private final Item whileHas;
    private final Map<ValueType, Double> values;
    private final StatusActivity activity;

    StatusType() {
        this(0, 0, null, new HashMap<>(), null);
    }
    StatusType(long duration) {
        this(duration, 0, null, new HashMap<>(), null);
    }
    StatusType(Item whileHas) {
        this(0, 0, whileHas, new HashMap<>(), null);
    }
    StatusType(Item whileHas, StatusActivity activity) {
        this(0, 0, whileHas, new HashMap<>(), activity);
    }
    StatusType(long duration, Map<ValueType, Double> values) {
        this(duration, 0, null, values, null);
    }
    StatusType(Item whileHas, Map<ValueType, Double> values) {
        this(0, 0, whileHas, values, null);
    }
    StatusType(long duration, long cooldown, Item whileHas, Map<ValueType, Double> values, StatusActivity activity) {
        this.duration = duration;
        this.cooldown = cooldown;
        this.whileHas = whileHas;
        this.values = values;
        this.activity = activity;
    }

    public long getDuration() {
        return duration;
    }

    public long getCooldown() {
        return cooldown;
    }

    public Item getWhileHas() {
        return whileHas;
    }

    public Map<ValueType, Double> getBaseValues() {
        return values;
    }

    public Map<ValueType, Double> getValues() {
        return values;
    }

    public StatusActivity getActivity() {
        return activity;
    }
}
