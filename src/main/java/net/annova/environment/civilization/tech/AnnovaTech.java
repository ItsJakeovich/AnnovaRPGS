package net.annova.environment.civilization.tech;

import net.annova.environment.civilization.Civilization;
import net.annova.lang.AISString;
import net.annova.object.person.RPGPerson;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.*;

import static net.annova.main.AnnovaRPG.getInstance;

public enum AnnovaTech {
    COLLECTING(Category.PROSPERITY, Items.SWEET_BERRIES, 0),
    FIRE(Category.PROSPERITY, Items.CAMPFIRE, 0, COLLECTING),
    WEAVING(Category.PROSPERITY, Items.STRING, 0, FIRE),

    JOINT_HUNTING(Category.HUNT, Items.BUNDLE, 0),
    SPEAR(Category.HUNT, Items.STICK, 0, JOINT_HUNTING),
    GESTICULATION(Category.HUNT, Items.BUNDLE, 0, JOINT_HUNTING),
    TRAPS(Category.HUNT, Items.OAK_LEAVES, 0, JOINT_HUNTING, FIRE)
    ;
    private final Category category;
    private final Item icon;
    private final int baseScience;
    private Set<AnnovaTech> depends;

    private static final EnumMap<Category, EnumSet<AnnovaTech>> TECHS_BY_CATEGORY = new EnumMap<>(Category.class);
    static {
        for (Category category : Category.values()) {
            TECHS_BY_CATEGORY.put(category, EnumSet.noneOf(AnnovaTech.class));
        }
        for (AnnovaTech tech : AnnovaTech.values()) {
            EnumSet<AnnovaTech> newDepends = EnumSet.noneOf(AnnovaTech.class);
            newDepends.addAll(tech.depends); tech.depends = newDepends;
            TECHS_BY_CATEGORY.get(tech.category).add(tech);
        }
    }

    AnnovaTech(Category category, Item icon, int baseScience, AnnovaTech... depends) {
        this.category = category;
        this.icon = icon;
        this.baseScience = baseScience;
        this.depends = new HashSet<>(List.of(depends));
    }

    public Category getCategory() {
        return category;
    }
    public Type getType() {
        return category.type;
    }

    public Item getIcon() {
        return icon;
    }

    public int getBaseCost() {
        return baseScience;
    }

    public Set<AnnovaTech> getDepends() {
        return depends;
    }

    public AISString toString(RPGPerson person) {
        return person.getPerson().getAISString(getInstance(), this.name());
    }

    public enum Type {
        CULTURE,
        COMMERCIAL,
        MILITARY
    }
    public enum Category {
        // Camp Categories
        PROSPERITY(Type.CULTURE, Civilization.Stage.CAMP),
        HUNT(Type.COMMERCIAL, Civilization.Stage.CAMP),
        THREAT(Type.MILITARY, Civilization.Stage.CAMP),
        ;
        private final Type type;
        private final Civilization.Stage stage;
        private static final EnumMap<Civilization.Stage, EnumMap<Type, Category>> CATEGORY_BY_STAGE_AND_TYPE = new EnumMap<>(Civilization.Stage.class);
        static {
            for (Civilization.Stage stage : Civilization.Stage.values()) {
                CATEGORY_BY_STAGE_AND_TYPE.put(stage, new EnumMap<>(Type.class));
            }
            for (Category category : Category.values()) {
                CATEGORY_BY_STAGE_AND_TYPE.get(category.stage).put(category.type, category);
            }
        }

        Category(Type type, Civilization.Stage stage) {
            this.type = type;
            this.stage = stage;
        }

        public Type getType() {
            return type;
        }
        public Civilization.Stage getStage() {
            return stage;
        }

        public static Category getByStageAndType(Civilization.Stage stage, Type type) {
            return CATEGORY_BY_STAGE_AND_TYPE.get(stage).get(type);
        }
    }

    public static EnumSet<AnnovaTech> getTechsByCategory(Category category) {
        return TECHS_BY_CATEGORY.get(category);
    }
}
