package net.annova.environment.civilization.build;

import net.annova.object.Person;
import net.annova.object.SimpleStructure;
import net.annova.util.BukkitObjects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

import static net.annova.main.AnnovaGlobal.getStructure;
import static net.annova.main.AnnovaRPG.getInstance;

public enum BuildBase {
    CAMP(30, Items.OAK_DOOR, "build-camp", getStructure("camp-default"));

    private final int baseCost;
    private final Item icon;
    private final String local;
    private final SimpleStructure[] styles;

    private static final Map<SimpleStructure, BuildBase> buildByStructure = new HashMap<>();
    static {
        for (BuildBase base : BuildBase.values()) {
            for (SimpleStructure structure : base.styles) {
                buildByStructure.put(structure, base);
            }
        }
    }

    BuildBase(int baseCost, Item icon, String local, SimpleStructure... styles) {
        this.baseCost = baseCost;
        this.icon = icon;
        this.local = local;
        this.styles = styles;
    }

    public int getBaseCost() {
        return baseCost;
    }
    public Item getIcon() {
        return icon;
    }
    public String getLocal() {
        return local;
    }
    public SimpleStructure[] getStyles() {
        return styles;
    }

    public ItemStack createItem(Person person) {
        return BukkitObjects.createItem(icon, this.getLocalName(person), -1, "b-place-" + this.name());
    }

    public Component getLocalName(Person person) {
        return person.getAISString(getInstance(), this.local).getDefined(person);
    }

    public static BuildBase getBuildBase(SimpleStructure structure) {
        return buildByStructure.get(structure);
    }
}
