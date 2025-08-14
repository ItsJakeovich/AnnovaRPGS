package net.annova.object.person.status;

import net.annova.environment.world.sector0.location.DarkForest;
import net.annova.object.item.base.ItemType;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.person.equipment.ArmorSet.CHITIN;
import static net.annova.object.item.base.ItemType.*;
import static net.annova.object.item.crystal.DefenceStoneItem.DEFENCE_STONE_RESISTANCE;
import static net.annova.object.item.crystal.DefenceStoneItem.DEFENCE_STONE_SLOWNESS;
import static net.annova.object.person.status.ValueType.*;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;
import static net.minecraft.world.level.block.LiquidBlock.LEVEL;

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
            ServerPlayer serverPlayer = person.getPerson().getServerPlayer();
            serverPlayer.connection.send(new ClientboundHurtAnimationPacket(serverPlayer.getId(), random.nextInt(360)));
            return true;
        }

        @Override
        public long repeating() {
            return 4;
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

    STRENGTH_BUFF_DEBUFF(STRENGTH_STONE, Map.of(DAMAGE_MULTIPLIER, 0.5, DEFENCE_MULTIPLIER, -0.3)),

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

    RED_STONE_RING_BUFF(RED_STONE, Map.of(RING_BUFF, 4.0)),

    CAVIAR_DAMAGE_BUFF(60_000, Map.of(ValueType.DAMAGE, 12.0)),

    ELK_FISH_DAMAGE_BUFF(60_000,  Map.of(ValueType.DAMAGE, 6.0)),

    FIRE_FISH_DAMAGE_BUFF(15_000, Map.of(DAMAGE, 4.0)),

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

    ENT_PATRON_BUFF(ENTS_PATRON_AMULET),
    PACK_LEADER_BUFF(PACK_LEADER_AMULET),
    ROOTS_LASH_BUFF(ROOTS_LASH),

    ABSORB_NIGHT_BUFF(ABSORBS_NIGHT_RING, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            if (!(person.getSectorLocation() instanceof DarkForest)) {
                person.minutesInDarkForest = 0;
                return true;
            }
            if (person.minutesInDarkForest < 60) {
                person.minutesInDarkForest++;
                if (person.minutesInDarkForest == 59) {
                    person.getPerson().send(getInstance(), "absorb-night-ring-limit");
                }
            }
            return true;
        }

        @Override
        public long repeating() {
            return 1200;
        }
    }),
    BLEEDING(3_000, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            person.damage(0.5f);
            return true;
        }

        @Override
        public long repeating() {
            return 25;
        }
    }),
    MAGMA_FRAGMENT_BURNING(new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            if (person.getArmorSet() != CHITIN) player.setFireTicks(100);
            return true;
        }

        @Override
        public long repeating() {
            return 80;
        }
    }, MAGMA_FRAGMENT),
    HOT_STONE_BURNING(new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            if (person.getArmorSet() != CHITIN) player.setFireTicks(100);
            return true;
        }

        @Override
        public long repeating() {
            return 80;
        }
    }, HOT_STONE),
    METEORITE_CORE_BURNING(new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            ServerPlayer serverPlayer = person.getServerPlayer();
            if (person.getArmorSet() != CHITIN) serverPlayer.igniteForTicks(100);

            BlockState state = serverPlayer.getInBlockState();
            if (state.is(Blocks.WATER) && state.getValue(LEVEL) == 0) {
                ServerLevel level = serverPlayer.serverLevel(); BlockPos pos = serverPlayer.blockPosition();
                level.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 1, 1);
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
            }
            return true;
        }

        @Override
        public long repeating() {
            return 80;
        }
    }, METEORITE_GOLEM_CORE),

    PHANTOM_FEAR(new StatusActivity() {
        private static final MobEffectInstance DARKNESS = new MobEffectInstance(MobEffects.DARKNESS, 80, 0);
        private static final MobEffectInstance SLOWNESS = new MobEffectInstance(MobEffects.SLOWNESS, 80, 0);
        private static final MobEffectInstance GLOWING = new MobEffectInstance(MobEffects.GLOWING, 80, 0);

        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            ServerPlayer server = person.getPerson().getServerPlayer();
            server.addEffect(DARKNESS);
            server.addEffect(SLOWNESS);
            server.addEffect(GLOWING);
            return true;
        }

        @Override
        public long repeating() {
            return 40;
        }
    }),

    DARKNESS_CONCENTRATION_STATUS(DARKNESS_CONCENTRATION, new StatusActivity() {
        @Override
        public boolean activity(RPGPerson person, Player player, Object object) {
            person.getPerson().send(getInstance(), "DARKNESS_CONCENTRATION-effect");
            person.damage(5);
            return true;
        }

        @Override
        public long repeating() {
            return 18000;
        }
    })
    ;

    private final long duration;
    private final long cooldown;
    private final ItemType whileHas;
    private final ItemType whileInHand;
    private final Map<ValueType, Double> values = new EnumMap<>(ValueType.class);
    private final StatusActivity activity;

    StatusType() {
        this(0, 0, null, null, new HashMap<>(), null);
    }
    StatusType(long duration) {
        this(duration, (StatusActivity) null);
    }
    StatusType(ItemType whileHas) {
        this(whileHas, (StatusActivity) null);
    }
    StatusType(Map<ValueType, Double> values) {
        this(0, 0, null, null, values, null);
    }
    StatusType(StatusActivity activity) {
        this(0, 0, null, null, new HashMap<>(), activity);
    }
    StatusType(StatusActivity activity, ItemType whileInHand) {
        this(0, 0, null, whileInHand, new HashMap<>(), activity);
    }
    StatusType(long duration, StatusActivity activity) {
        this(duration, 0, null, null, new HashMap<>(), activity);
    }
    StatusType(ItemType whileHas, StatusActivity activity) {
        this(0, 0, whileHas, null, new HashMap<>(), activity);
    }
    StatusType(long duration, Map<ValueType, Double> values) {
        this(duration, 0, null, null, values, null);
    }
    StatusType(ItemType whileHas, Map<ValueType, Double> values) {
        this(0, 0, whileHas, null, values, null);
    }
    StatusType(long duration, long cooldown, ItemType whileHas, ItemType whileInHand, Map<ValueType, Double> values, StatusActivity activity) {
        this.duration = duration;
        this.cooldown = cooldown;
        this.whileHas = whileHas;
        this.whileInHand = whileInHand;
        this.values.putAll(values);
        this.activity = activity;
    }

    private static final EnumSet<StatusType> forActivation = EnumSet.noneOf(StatusType.class);
    private static final EnumSet<StatusType> forActivationInHand = EnumSet.noneOf(StatusType.class);
    static {
        for (StatusType type : StatusType.values()) {
            if (type.whileHas != null) {
                forActivation.add(type);
            }
            if (type.whileInHand != null) {
                forActivationInHand.add(type);
            }
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

    public ItemType getActivationInHandItem() {
        return whileInHand;
    }
    public static EnumSet<StatusType> getForActivationInHand() {
        return forActivationInHand;
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
