package net.annova.environment.civilization.tech;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;

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
    private final EnumSet<AnnovaTech> depends = EnumSet.noneOf(AnnovaTech.class);

    private static final EnumMap<AnnovaTech.Category, EnumSet<AnnovaTech>> TECHS_BY_CATEGORY = new EnumMap<>(AnnovaTech.Category.class);
    static {
        for (Category category : Category.values()) {
            TECHS_BY_CATEGORY.put(category, EnumSet.noneOf(AnnovaTech.class));
        }
        for (AnnovaTech tech : AnnovaTech.values()) {
            TECHS_BY_CATEGORY.get(tech.category).add(tech);
        }
    }

    AnnovaTech(Category category, Item icon, int baseScience, AnnovaTech... depends) {
        this.category = category;
        this.icon = icon;
        this.baseScience = baseScience;
        this.depends.addAll(Arrays.asList(depends));
    }

    public Category getCategory() {
        return category;
    }

    public Item getIcon() {
        return icon;
    }

    public int getBaseScience() {
        return baseScience;
    }

    public EnumSet<AnnovaTech> getDepends() {
        return depends;
    }

    public enum Type {
        CULTURE,
        COMMERCIAL,
        MILITARY
    }
    public enum Category {
        // Camp Categories
        PROSPERITY, // CULTURE
        HUNT,       // COMMERCIAL
        THREAT,     // MILITARY
    }

    public static EnumSet<AnnovaTech> getTechsByCategory(Category category) {
        return TECHS_BY_CATEGORY.get(category);
    }
}
