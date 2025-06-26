package net.annova.object.person.status;

import net.annova.object.item.base.ItemType;
import net.annova.object.person.RPGPerson;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.object.item.base.ItemType.*;
import static net.annova.object.item.crystal.DefenceStoneItem.DEFENCE_STONE_RESISTANCE;
import static net.annova.object.item.crystal.DefenceStoneItem.DEFENCE_STONE_SLOWNESS;
import static net.annova.object.person.status.ValueType.*;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public enum StatusType {
    CB_DAMAGE_DEBUFF(Map.of(ValueType.DAMAGE, -0.5)),

    HARRY_DEFENCE_DEBUFF(15_000, Map.of(DEFENCE_MULTIPLIER, 0.5)),

    ICENAGE(ICE_STONE, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            for (Entity entity : player.getNearbyEntities(7, 5 ,7)) {
                if (!(entity instanceof LivingEntity ent)) continue;
                ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 30, 0));
            }
            return true;
        }

        @Override
        public long repeating() {
            return 20;
        }
    }),

    SHAKING(new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            Location location = player.getLocation();

            player.setRotation(location.getYaw() + random.nextInt(9)-4, location.getPitch() + random.nextInt(5)-2);
            return true;
        }

        @Override
        public long repeating() {
            return 2;
        }
    }),

    STOPPED(),

    FROZEN(new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            if (object == null) return false;
            person.teleport(player.getLocation());
            return true;
        }

        @Override
        public long repeating() {
            return 2L;
        }
    }),

    PASSIVE_SHIELD(DEFENCE_STONE, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
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
        public boolean activity(RPGPerson person, Player player, Object object) {
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

    RUNIC_BUFF_DEBUFF(RUNIC_STONE, Map.of(DAMAGE_MULTIPLIER, 1.0, DEFENCE_MULTIPLIER, -1.0)),

    STRENGTH_BUFF_DEBUFF(STRENGTH_STONE, Map.of(DAMAGE_MULTIPLIER, 0.5, DEFENCE_MULTIPLIER, 0.3)),

    RANGE_TOKEN_BUFF(RANGE_TOKEN, Map.of(BLOCK_RANGE, 2.5)),

    IGNIS(300_000),

    REPAIR_COOLDOWN(900_000),

    BIOME_STONE(),

    REGENERATION(REGENERATION_STONE, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
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
        public boolean activity(RPGPerson person, Player player, Object object) {
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
        public boolean activity(RPGPerson person, Player player, Object object) {
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

    GREEN_STONE_HEALTH_BUFF(GREEN_STONE, Map.of(ValueType.HEALTH, 8.0)),

    RED_STONE_RING_BUFF(RED_STONE, Map.of(RING_BUFF, 0.8)),

    CAVIAR_DAMAGE_BUFF(60_000, Map.of(ValueType.DAMAGE, 12.0)),

    ELK_FISH_DAMAGE_BUFF(60_000,  Map.of(ValueType.DAMAGE, 6.0)),

    PARALYSIS(2_000, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            Location location = player.getLocation();
            player.teleport(location);
            player.spawnParticle(Particle.CLOUD, location, 2);
            player.playSound(location, Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 3, 3);
            return true;
        }

        @Override
        public long repeating() {
            return 2;
        }
    }),
    ;

    private final long duration;
    private final long cooldown;
    private final ItemType whileHas;
    private final Map<ValueType, Double> values = new EnumMap<>(ValueType.class);
    private final StatusActivity activity;

    StatusType() {
        this(0, 0, null, new HashMap<>(), null);
    }
    StatusType(long duration) {
        this(duration, 0, null, new HashMap<>(), null);
    }
    StatusType(Map<ValueType, Double> values) {
        this(0, 0, null, values, null);
    }
    StatusType(StatusActivity activity) {
        this(0, 0, null, new HashMap<>(), activity);
    }
    StatusType(long duration, StatusActivity activity) {
        this(duration, 0, null, new HashMap<>(), activity);
    }
    StatusType(ItemType whileHas, StatusActivity activity) {
        this(0, 0, whileHas, new HashMap<>(), activity);
    }
    StatusType(long duration, Map<ValueType, Double> values) {
        this(duration, 0, null, values, null);
    }
    StatusType(ItemType whileHas, Map<ValueType, Double> values) {
        this(0, 0, whileHas, values, null);
    }
    StatusType(long duration, long cooldown, ItemType whileHas, Map<ValueType, Double> values, StatusActivity activity) {
        this.duration = duration;
        this.cooldown = cooldown;
        this.whileHas = whileHas;
        this.values.putAll(values);
        this.activity = activity;
    }

    private static final EnumSet<StatusType> forActivation = EnumSet.noneOf(StatusType.class);
    static {
        for (StatusType type : StatusType.values()) {
            if (type.whileHas == null) continue;
            forActivation.add(type);
            sendInfo("For activation: " + type.name());
        }
    }

    public long getDuration() {
        return duration;
    }

    public long getCooldown() {
        return cooldown;
    }

    public ItemType getActivationItem() {
        return whileHas;
    }
    public static EnumSet<StatusType> getForActivation() {
        return forActivation;
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
