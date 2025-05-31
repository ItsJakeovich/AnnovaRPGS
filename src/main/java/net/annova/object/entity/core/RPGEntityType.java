package net.annova.object.entity.core;

import net.annova.object.entity.mobs.Boar;
import net.annova.object.entity.mobs.Snake;
import net.annova.object.entity.mobs.nether.*;
import net.annova.object.entity.mobs.prison.*;
import net.annova.object.entity.mobs.sector0.EnchantedArmor;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum RPGEntityType {
    PRISONER(0, Prisoner.class),
    HERSON(1, Herson.class),
    VELDER(2, Velder.class),
    ZOGGA(3, Zogga.class),
    KENJI(4, Kenji.class),
    HARRY(5, Harry.class),
    JACK(6, Jack.class),
    KB(7, Counterbandit.class),
    LEADER(8, Leader.class),
    ZERO(9, Zero.class),
    MAJOR(10, Major.class),
    SECURITY(11, Security.class),

    BOAR(12, Boar.class), // unique legendary mob

    // nether mobs
    BERSERK(13, Berserk.class),
    CUSTOS(14, Custos.class),
    DODGER(15, Dodger.class),
    DUELIST(16, Duelist.class),
    LEX(17, Lex.class),
    MERETRIX(18, Meretrix.class),
    PAUPERES(19, Pauperes.class),
    SOLDIER(20, Soldier.class),

    SNAKE(21, Snake.class),

    // world0 mobs
    ENCHANTED_ARMOR(22, EnchantedArmor.class)
    ;

    final int key;
    final Class<? extends RPGEntity> clazz;

    private static final Map<Integer, RPGEntityType> BY_KEY = new HashMap<>();
    private static final Map<Class<? extends RPGEntity>, Integer> KEY_BY_CLASS = new HashMap<>();

    static {
        for (RPGEntityType type : values()) {
            BY_KEY.put(type.key, type);
            KEY_BY_CLASS.put(type.clazz, type.key);
        }
    }

    RPGEntityType(int key, Class<? extends RPGEntity> clazz) {
        this.key = key;
        this.clazz = clazz;
    }

    public RPGEntity getEntity() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |IllegalAccessException e) {
            throw new EnumConstantNotPresentException(RPGEntityType.class, "RPGEntity .class");
        }
    }

    public int getKey() {
        return key;
    }

    @Nullable
    public static RPGEntity getEntity(int key) {
        return BY_KEY.get(key).getEntity();
    }
    public static int getId(RPGEntity entity) {
        return KEY_BY_CLASS.get(entity.getClass());
    }
}
