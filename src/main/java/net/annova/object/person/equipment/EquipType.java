package net.annova.object.person.equipment;

public enum EquipType {
    RING(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
    SCARF(10),
    HAIR_CLIP(11),
    BELT(12),
    AMULET(13),
    GLOVE(14, 15),
    BRACELET(16, 17),

    SHOULDER_PADS(18, 19),
    KNEE_PADS(20, 21),
    CLOAK(22),
    COMPANION(23);

    private final int[] slots;

    EquipType(int... slots) {
        this.slots = slots;
    }

    public int[] getSlots() {
        return slots;
    }
}
