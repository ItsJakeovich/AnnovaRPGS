package net.annova.object.entity.core;

import net.annova.main.GlobalRPG;
import net.annova.object.LocationCategory;
import net.annova.object.entity.mobs.Boar;
import net.annova.object.entity.mobs.Snake;
import net.annova.object.entity.mobs.nether.*;
import net.annova.object.entity.mobs.prison.*;
import net.annova.object.entity.mobs.sector0.caves.*;
import net.annova.object.entity.mobs.sector0.oasis.AnglerFish;
import net.annova.object.entity.mobs.sector0.oasis.Crab;
import net.annova.object.entity.mobs.sector0.SmallEnt;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static net.annova.object.LocationCategory.*;

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

    BOAR(Boar.class), // unique legendary mob
    SNAKE(Snake.class),

    // nether mobs
    BERSERK(NETHER, Berserk.class),
    CUSTOS(NETHER, Custos.class),
    DODGER(NETHER, Dodger.class),
    DUELIST(NETHER, Duelist.class),
    LEX(NETHER, Lex.class),
    MERETRIX(NETHER, Meretrix.class),
    PAUPERES(NETHER, Pauperes.class),
    SOLDIER(NETHER, Soldier.class),

    // Sector 0 - Oasis
    ANGLER_FISH(S_OASIS, AnglerFish.class),
    CRAB(S_OASIS, Crab.class),
    // Sector 0 - Caves
    GOLEM(S0_CAVES, Golem.class),
    GOLEM_BOSS(S0_CAVES, GolemBoss.class),
    GUARDIAN(S0_CAVES, Guardian.class),
    MAGE(S0_CAVES, Mage.class),
    MINER(S0_CAVES, Miner.class),
    ORE_COLLECTOR(S0_CAVES, OreCollector.class),
    // Sector 0 - Forest
    SmalllEnt(S0_FOREST, SmallEnt.class)
    ;

    final LocationCategory category;
    final Class<? extends RPGEntity> clazz;

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
        this.category = category;
        this.clazz = clazz;
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
}
