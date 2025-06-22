package net.annova.object.person.equipment;

import org.bukkit.inventory.ItemStack;

import static net.annova.util.BukkitObjects.getNewIcon;

public enum EquipType {
    RING(getNewIcon(20000f), new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, new int[]{0, 1, 2, 6, 7, 8, 9, 10, 16, 17}),
    SCARF(getNewIcon(20001f), new int[]{10}, new int[]{3}),
    HAIR_CLIP(getNewIcon(20002f), new int[]{11}, new int[]{5}),
    BELT(getNewIcon(20003f), new int[]{12}, new int[]{31}),
    AMULET(getNewIcon(20004f), new int[]{13}, new int[]{4}),
    GLOVE(getNewIcon(20005f), new int[]{14, 15}, new int[]{18, 26}),
    BRACELET(getNewIcon(20006f), new int[]{16, 17}, new int[]{19, 25}),

    SHOULDER_PADS(getNewIcon(20007f), new int[]{18, 19}, new int[]{45, 53}),
    KNEE_PADS(getNewIcon(20008f), new int[]{20, 21}, new int[]{46, 52}),
    CLOAK(getNewIcon(20009f), new int[]{22}, new int[]{13}),
    COMPANION(getNewIcon(20010f), new int[]{23}, new int[]{22});

    private final ItemStack icon;
    private final int[] indices;
    private final int[] slots;

    EquipType(ItemStack icon, int[] indices, int[] slots) {
        this.icon = icon;
        this.indices = indices;
        this.slots = slots;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public int[] getIndices() {
        return indices;
    }

    public int[] getSlots() {
        return slots;
    }
}
