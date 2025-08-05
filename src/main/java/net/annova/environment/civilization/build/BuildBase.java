package net.annova.environment.civilization.build;

import net.annova.object.Person;
import net.annova.util.BukkitObjects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static net.annova.main.AnnovaRPG.getInstance;

public enum BuildBase {
    CAMP(Items.OAK_DOOR, "build-camp");

    private final Item icon;
    private final String local;
    BuildBase(Item icon, String local) {
        this.icon = icon;
        this.local = local;
    }

    public Item getIcon() {
        return icon;
    }

    public String getLocal() {
        return local;
    }

    public ItemStack createItem(Person person) {
        return BukkitObjects.createItem(icon, person.getAISString(getInstance(), local).getDefined(person), -1, null);
    }
}
