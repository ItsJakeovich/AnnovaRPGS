package net.annova.object.person;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

import static net.annova.util.BukkitObjects.getNewIcon;

public class Equipment {
    public enum Type {
        RING,
        BELT,
        AMULET,
        GLOVE,
        BRACELET,

        SHOULDER_PADS,
        KNEE_PADS,
        CLOAK,
        COMPANION;
    }

    private final ItemStack[] equipment = new ItemStack[22];


    /** Slots:
     * 0-9 ring's,
     * 10 belt,
     * 11 amulet,
     * 12-13 glove's,
     * 14-15 bracelet's,
     * 16-17 shoulder pads,
     * 18-19 knee pads,
     * 20 cloak,
     * 21 is pet companion.
     * @param index ItemStack[] array
     * @return ItemStack / null if slot is empty
     */
    @Nullable
    public ItemStack getEquipment(int index) {
        return equipment[index];
    }
    public ItemStack[] getAllEquipment() {
        return equipment;
    }

    /**
     * 0 - ring
     * 1 - belt
     * 2 - amulet
     * 3 - glove
     * 4 - bracelet
     * 5 - shoulder pads
     * 6 - knee pads
     * 7 - cloak
     * 8 - companion
     */
    private static final ItemStack[] icons = new ItemStack[] {
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
    };
    /** (e - belt)
     * R R R - A - R R R   7
     * R R - - C - - R R   5
     * G B - - P - - B G   5
     * - - - - e - - - -   1
     * - - - - - - - - -   0
     * S K - - - - - K S   4
     * @return Inventory with equipment slot's
     */
    public Inventory open() {
        Inventory inv = Bukkit.createInventory(null, 54);
        inv.setItem(0, this.getItemSlot(0, 0)); // rings
        inv.setItem(1, this.getItemSlot(1, 0));
        inv.setItem(2, this.getItemSlot(2, 0));
        inv.setItem(6, this.getItemSlot(3, 0));
        inv.setItem(7, this.getItemSlot(4, 0));
        inv.setItem(8, this.getItemSlot(5, 0));
        inv.setItem(9, this.getItemSlot(6, 0));
        inv.setItem(10, this.getItemSlot(7, 0));
        inv.setItem(16, this.getItemSlot(8, 0));
        inv.setItem(17, this.getItemSlot(9, 0));

        inv.setItem(31, this.getItemSlot(10, 1)); // belt

        inv.setItem(4, this.getItemSlot(11, 2)); // amulet

        inv.setItem(18, this.getItemSlot(12, 3)); // gloves
        inv.setItem(26, this.getItemSlot(13, 3));

        inv.setItem(19, this.getItemSlot(14, 4)); // bracelets
        inv.setItem(25, this.getItemSlot(15, 4));

        inv.setItem(45, this.getItemSlot(16, 5)); // shoulder pads
        inv.setItem(53, this.getItemSlot(17, 5));

        inv.setItem(46, this.getItemSlot(18, 6)); // knee pads
        inv.setItem(52, this.getItemSlot(19, 6));

        inv.setItem(13, this.getItemSlot(20, 7)); // cloak
        inv.setItem(22, this.getItemSlot(21, 8)); // companion
        return inv;
    }
    private ItemStack getItemSlot(int slot, int iconIndex) {
        ItemStack item = equipment[slot];
        return item == null ? icons[iconIndex] : item;
    }
}
