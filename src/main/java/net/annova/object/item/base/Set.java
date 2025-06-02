package net.annova.object.item.base;

import net.annova.object.item.equipment.prison.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public enum Set {
    PRISON(new PrisonSet(), Item.PRISON_HELMET, Item.PRISON_CHESTPLATE, Item.PRISON_LEGGINGS, Item.PRISON_BOOTS),
    ;

    private final RPGSet set;
    private final Item[] armor;

    Set(RPGSet set, Item... armor) {
        this.set = set;
        this.armor = armor;
    }

    public RPGSet getSet() {
        return set;
    }
    public Item[] getArmor() {
        return armor;
    }

    public static Set getSet(Player player) {
        loop:
        for (Set set : Set.values()) {
            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < 4; i++) {
                ItemStack item = null;
                switch (i) {
                    case 0 -> item = inventory.getHelmet();
                    case 1 -> item = inventory.getChestplate();
                    case 2 -> item = inventory.getLeggings();
                    case 3 -> item = inventory.getBoots();
                }
                if (!set.armor[i].equals(item)) {
                    continue loop;
                }
            }
            return set;
        }
        return null;
    }
}
