package net.annova.object.entity.core;

import net.annova.main.GlobalRPG;
import net.annova.object.LocationCategory;
import net.annova.object.entity.mobs.Boar;
import net.annova.object.entity.mobs.Frog;
import net.annova.object.entity.mobs.Snake;
import net.annova.object.entity.mobs.nether.*;
import net.annova.object.entity.mobs.prison.*;
import net.annova.object.entity.mobs.sector0.dryad.DarkWolf;
import net.annova.object.entity.mobs.sector0.dryad.ForestWolf;
import net.annova.object.entity.mobs.sector0.forest.DarkElf;
import net.annova.object.entity.mobs.sector0.forest.OlderEnt;
import net.annova.object.entity.mobs.sector0.meteorite.MeteoriteGolem;
import net.annova.object.entity.mobs.sector0.meteorite.Shards;
import net.annova.object.entity.mobs.sector0.mines.*;
import net.annova.object.entity.mobs.sector0.oasis.AnglerFish;
import net.annova.object.entity.mobs.sector0.oasis.Crab;
import net.annova.object.entity.mobs.sector0.forest.Ent;
import net.annova.object.entity.mobs.sector0.swamps.FatSpider;
import net.annova.object.entity.mobs.sector0.swamps.Melanox;
import net.annova.object.entity.mobs.sector0.swamps.SwampSpider;
import net.annova.object.entity.mobs.sector0.swamps.WhipSpider;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.environment.world.SectorLocation.getChorusLocation;
import static net.annova.main.GlobalRPG.getAmountByTypeEntity;
import static net.annova.object.LocationCategory.*;
import static net.annova.util.BukkitObjects.chance;

public enum RPGEntityType {
    PRISONER(D_PRISON, Prisoner.class),
    HERSON(D_PRISON, Herson.class),
    VELDER(D_PRISON, Velder.class),
    ZOGGA(D_PRISON, Zogga.class),
    KENJI(D_PRISON, Kenji.class),
    HARRY(D_PRISON, Harry.class),
    JACK(D_PRISON, Jack.class),
    COUNTERBANDIT(D_PRISON, Counterbandit.class),
    LEADER(D_PRISON, Leader.class),
    ZERO(D_PRISON, Zero.class),
    MAJOR(D_PRISON, Major.class),
    SECURITY(D_PRISON, Security.class),

    BOAR(EVERYWHERE, Boar.class, 0.05), // unique legendary mob
    FROG(EVERYWHERE, Frog.class, 0.01666), // 5/3
    SNAKE(EVERYWHERE, Snake.class, 0.05),

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
    DARK_WOLF(S0_DRYAD, DarkWolf.class),
    FOREST_WOLF(S0_DRYAD, ForestWolf.class),
    // Sector 0 - Forest
    DARK_ELF(S0_FOREST, DarkElf.class),
    ENT(S0_FOREST, Ent.class),
    OLDER_ENT(S0_FOREST, OlderEnt.class, 0.03),
    // Sector - Meteorite
    METEORITE_GOLEM(S0_METEORITE, MeteoriteGolem.class),
    SHARDS(S0_METEORITE, Shards.class),
    // Sector 0 - Mines
    GOLEM(S0_MINES, Golem.class),
    GOLEM_BOSS(S0_MINES, GolemBoss.class),
    GUARDIAN(S0_MINES, Guardian.class),
    MAGE(S0_MINES, Mage.class),
    MINER(S0_MINES, Miner.class),
    ORE_COLLECTOR(S0_MINES, OreCollector.class),
    // Sector 0 - Oasis
    ANGLER_FISH(S0_OASIS, AnglerFish.class),
    CRAB(S0_OASIS, Crab.class),
    // Sector 0 - Swamps
    FAT_SPIDER(S0_SWAMPS, FatSpider.class),
    SWAMP_SPIDER(S0_SWAMPS, SwampSpider.class),
    WHIP_SPIDER(S0_SWAMPS, WhipSpider.class),
    MELANOX(S0_SWAMPS, Melanox.class)
    ;

    final LocationCategory category;
    final Class<? extends RPGEntity> clazz;

    private final double chance;
    private final int maxAmount;

    private static final Map<LocationCategory, Set<RPGEntityType>> ENTITIES_BY_CATEGORY = new EnumMap<>(LocationCategory.class);
    private static final Map<Class<? extends RPGEntity>, String> KEY_BY_CLASS = new HashMap<>();

    static {
        for (LocationCategory cat : LocationCategory.values()) {
            ENTITIES_BY_CATEGORY.put(cat, new HashSet<>());
        }
        for (RPGEntityType type : values()) {
            ENTITIES_BY_CATEGORY.get(type.category).add(type);
            KEY_BY_CLASS.put(type.clazz, type.name());
        }
    }

    RPGEntityType(Class<? extends RPGEntity> clazz) {
        this(WITHOUT, clazz);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz) {
        this(category, clazz, 1.0);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, double chance) {
        this(category, clazz, chance, 0);
    }
    RPGEntityType(LocationCategory category, Class<? extends RPGEntity> clazz, double chance, int maxAmount) {
        this.category = category;
        this.clazz = clazz;
        this.chance = chance;
        this.maxAmount = maxAmount;
    }

    public RPGEntity getEntity() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |IllegalAccessException e) {
            throw new EnumConstantNotPresentException(RPGEntityType.class, "RPGEntity .class");
        }
    }

    public static String getId(RPGEntity entity) {
        return KEY_BY_CLASS.get(entity.getClass());
    }
    public static Set<RPGEntityType> getEntitiesByCategory(LocationCategory category) {
        return ENTITIES_BY_CATEGORY.get(category);
    }

    public boolean equals(LivingEntity entity) {
        return this.equals(GlobalRPG.getEntity(entity));
    }
    public boolean equals(RPGEntity entity) {
        return entity != null && this.name().equals(entity.getId());
    }

    public boolean tryNaturalSpawn(Location location) {
        if (!chance(chance)) return false;
        if (this.maxAmount != 0 && this.maxAmount >= getAmountByTypeEntity(this)) {
            /* for test */ sendInfo("Entities - " + this.name() +  " amount: " + getAmountByTypeEntity(this));
            return false;
        }

        Location spawn = getChorusLocation(location, 30);
        if (spawn.distanceSquared(location) < 64) return false;
        if (spawn.getBlock().getLightFromBlocks() > 7) return false;
        this.getEntity().create(spawn);
        return true;
    }
}
