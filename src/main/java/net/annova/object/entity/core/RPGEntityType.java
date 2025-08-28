package net.annova.object.entity.core;

import net.annova.main.GlobalRPG;
import net.annova.object.LocationCategory;
import net.annova.object.entity.mobs.Boar;
import net.annova.object.entity.mobs.Frog;
import net.annova.object.entity.mobs.Snake;
import net.annova.object.entity.mobs.nether.*;
import net.annova.object.entity.mobs.prison.*;
import net.annova.object.entity.mobs.sector0.dryad.DarkWolf;
import net.annova.object.entity.mobs.sector0.dryad.Fang;
import net.annova.object.entity.mobs.sector0.dryad.ForestWolf;
import net.annova.object.entity.mobs.sector0.dryad.Werewolf;
import net.annova.object.entity.mobs.sector0.forest.DarkElf;
import net.annova.object.entity.mobs.sector0.forest.OlderEnt;
import net.annova.object.entity.mobs.sector0.meteorite.MeteoriteGolem;
import net.annova.object.entity.mobs.sector0.meteorite.Shards;
import net.annova.object.entity.mobs.sector0.mines.*;
import net.annova.object.entity.mobs.sector0.oasis.AnglerFish;
import net.annova.object.entity.mobs.sector0.oasis.Crab;
import net.annova.object.entity.mobs.sector0.forest.Ent;
import net.annova.object.entity.mobs.sector0.swamps.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static net.annova.environment.world.GlobalWorld.isNight;
import static net.annova.environment.world.SectorLocation.getChorusLocation;
import static net.annova.environment.world.SectorLocation.getWaterChorusLocation;
import static net.annova.main.GlobalRPG.getAmountByTypeEntity;
import static net.annova.object.LocationCategory.*;
import static net.annova.util.BukkitObjects.chance;

public enum RPGEntityType {
    // Длинее 60 нельзя! Если что меняй в RPGEntity.init();
    PRISONER(D_PRISON, Prisoner.class),
    HERSON(D_PRISON, Herson.class),
    VELDER(D_PRISON, Velder.class),
    ZOGGA(D_PRISON, Zogga.class),
    KENJI(D_PRISON, Kenji.class),
    HARRY(D_PRISON, Harry.class),
    JACK(D_PRISON, Jack.class),
    KB(D_PRISON, Counterbandit.class),
    LEADER(D_PRISON, Leader.class),
    ZERO(D_PRISON, Zero.class),
    MAJOR(D_PRISON, Major.class),
    SECURITY(D_PRISON, Security.class),

    // 'EVERYWHERE' category doesn't have a boss.
    BOAR(EVERYWHERE, Boar.class, 0.04), // unique legendary mob
    FROG(EVERYWHERE, Frog.class, 0.0044),
    SNAKE(EVERYWHERE, Snake.class, 0.04),

    // nether mobs
    BERSERK(NETHER, Berserk.class),
    CUSTOS(NETHER, Custos.class),
    DODGER(NETHER, Dodger.class),
    DUELIST(NETHER, Duelist.class),
    LEX(NETHER, Lex.class),
    MERETRIX(NETHER, Meretrix.class),
    PAUPERES(NETHER, Pauperes.class),
    SOLDIER(NETHER, Soldier.class),

    // Sector 0 - Dryad Pedestal
    DARK_WOLF(S0_DRYAD, DarkWolf.class, (Vec3 position, ServerLevel level) -> isNight(level)),
    FOREST_WOLF(S0_DRYAD, ForestWolf.class),
    FANG(S0_DRYAD, Fang.class, 0.9,true),
    WEREWOLF(S0_DRYAD, Werewolf.class, 0.9, 1, true, false, (Vec3 position, ServerLevel level) -> isNight(level)),
    // Sector 0 - Forest
    ENT(S0_FOREST, Ent.class, 0.9, 40),
    OLDER_ENT(S0_FOREST, OlderEnt.class, 0.9, true),
    DARK_ELF(S0_FOREST, DarkElf.class, 0.8, 1, true, false, (Vec3 position, ServerLevel level) -> isNight(level)),
    // Sector - Meteorite
    METEORITE_GOLEM(S0_METEORITE, MeteoriteGolem.class, 0.9, 1),
    SHARDS(S0_METEORITE, Shards.class),
    // Sector 0 - Mines
    CARRIER(S0_MINES, Carrier.class, 0.9),
    GOLEM_BOSS(S0_MINES, GolemBoss.class, 0.9, true),
    GUARDIAN(S0_MINES, Guardian.class, 0.9),
    MINER(S0_MINES, Miner.class, 0.9),
    ORE_COLLECTOR(S0_MINES, OreCollector.class, 0.9),
    PHANTOM(S0_MINES, Phantom.class),
    PHANTOM_MINION(S0_MINES, PhantomMinion.class),
    // Sector 0 - Oasis
    ANGLER_FISH(S0_OASIS, AnglerFish.class),
    CRAB(S0_OASIS, Crab.class),
    // Sector 0 - Swamps
    MELANOX(S0_SWAMPS, Melanox.class, 0.3, true),
    FAT_SPIDER(S0_SWAMPS, FatSpider.class, true),
    MOTHER_SPIDER(S0_SWAMPS, MotherSpider.class, true),
    SWAMP_SPIDER(S0_SWAMPS, SmallSpider.class, true),
    WHIP_SPIDER(S0_SWAMPS, WhipSpider.class, true)
    ;

    final LocationCategory category;
    final Class<? extends RPGEntity> clazz;

    private final double chance;
    private final int maxAmount;
    private final boolean boss;
    private final boolean waterSpawn;
    private final SpawnCondition condition;

    private static final Map<LocationCategory, EnumSet<RPGEntityType>> ENTITIES_BY_CATEGORY = new EnumMap<>(LocationCategory.class);
    private static final Map<Class<? extends RPGEntity>, RPGEntityType> TYPE_BY_CLASS = new HashMap<>();

    static {
        for (LocationCategory cat : LocationCategory.values()) {
            ENTITIES_BY_CATEGORY.put(cat, EnumSet.noneOf(RPGEntityType.class));
        }
        for (RPGEntityType type : values()) {
            ENTITIES_BY_CATEGORY.get(type.category).add(type);
            TYPE_BY_CLASS.put(type.clazz, type);
        }
    }

    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz) {
        this(category, clazz, false);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, boolean waterSpawn) {
        this(category, clazz, (Vec3 position, ServerLevel level) -> true, waterSpawn);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, boolean boss, boolean waterSpawn) {
        this(category, clazz, (Vec3 position, ServerLevel level) -> true, boss, waterSpawn);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, SpawnCondition condition) {
        this(category, clazz, 1.0, 10, false, false, condition);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, SpawnCondition condition, boolean waterSpawn) {
        this(category, clazz, 1.0, 10, false, waterSpawn, condition);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, SpawnCondition condition, boolean boss, boolean waterSpawn) {
        this(category, clazz, 1.0, 10, boss, waterSpawn, condition);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, double chance) {
        this(category, clazz, chance, 10, false, false, (Vec3 position, ServerLevel level) -> true);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, double chance, boolean boss) {
        this(category, clazz, chance, 10, boss, false, (Vec3 position, ServerLevel level) -> true);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, double chance, int maxAmount) {
        this(category, clazz, chance, maxAmount, false, false, (Vec3 position, ServerLevel level) -> true);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, double chance, int maxAmount, boolean boss, boolean waterSpawn, SpawnCondition condition) {
        this.category = category;
        this.clazz = clazz;
        this.chance = chance;
        this.boss = boss;
        this.maxAmount = boss ? 1 : maxAmount;
        this.waterSpawn = waterSpawn;
        this.condition = condition;
    }

    public interface SpawnCondition {
        boolean canSpawn(Vec3 position, ServerLevel level);
    }

    public RPGEntity getEntity() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |IllegalAccessException e) {
            throw new EnumConstantNotPresentException(RPGEntityType.class, "RPGEntity .class");
        }
    }

    public static RPGEntityType getType(RPGEntity entity) {
        return TYPE_BY_CLASS.get(entity.getClass());
    }
    public static EnumSet<RPGEntityType> getEntitiesByCategory(LocationCategory category, RPGEntityType... without) {
        EnumSet<RPGEntityType> entities = ENTITIES_BY_CATEGORY.get(category).clone();
        for (RPGEntityType type : without) {
            entities.remove(type);
        }
        return entities;
    }

    public boolean equals(LivingEntity entity) {
        return this.equals(GlobalRPG.getRPGEntity(entity));
    }
    public boolean equals(RPGEntity entity) {
        return this.equals(entity.getRPGType());
    }

    public double getChance() {
        return chance;
    }

    public boolean isBoss() {
        return boss;
    }

    public boolean isWaterSpawn() {
        return waterSpawn;
    }

    public SpawnCondition getCondition() {
        return condition;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public RPGEntity tryNaturalSpawn(Vec3 position, ServerLevel level, int chorus, boolean waterSpawn) {
        if (!chance(chance)) return null;

        if (getAmountByTypeEntity(this) > this.maxAmount) return null;

        Vec3 spawn = waterSpawn ? getWaterChorusLocation(position, level, chorus) : getChorusLocation(position, level, chorus);
        if (position == spawn) return null;
        BlockPos pos = BlockPos.containing(spawn);
        if (waterSpawn && level.getBrightness(LightLayer.SKY, pos) == 0) return null;
        if (level.getBrightness(LightLayer.BLOCK, pos) > 7) return null;
        RPGEntity entity = this.getEntity();
        entity.create(spawn, level);
        System.out.println(entity.entity.level().getMinecraftWorld().serverLevelData.getLevelName()+ ", " + entity.entity.position() + ": " + entity.getRPGType().name());
        return entity;
    }
}
