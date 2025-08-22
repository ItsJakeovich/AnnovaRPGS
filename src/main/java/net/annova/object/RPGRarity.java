package net.annova.object;

import net.minecraft.core.particles.DustColorTransitionOptions;

public enum RPGRarity {
    EVENT(0x6060FF, new DustColorTransitionOptions(0x4040DD, 0x6060FF, 2f)), // blue

    COMMON(0xAAAAAA, new DustColorTransitionOptions(14540253, 16777215, 2f)), // gray
    UNCOMMON(0x55FF55, new DustColorTransitionOptions(0x33DD33, 0x55FF55, 2f)), // green
    RARE(0x55FFFF, new DustColorTransitionOptions(0x33DDDD, 0x55FFFF, 2f)), // aqua
    ELITE(0xFF55FF, new DustColorTransitionOptions(0xDD33DD, 0xFF55FF, 2f)), // light purple
    EPIC(0xAA00AA, new DustColorTransitionOptions(0x880088, 0xAA00AA, 2f)), // dark purple
    LEGEND(0xFFAA00, new DustColorTransitionOptions(0xDD8800, 0xFFAA00, 2f)), // gold
    MYTHIC(0xFF5555, new DustColorTransitionOptions(0xDD3333, 0xFF5555, 2f)), // red
    IMPOSSIBLE(0xAA0000, new DustColorTransitionOptions(0x880000, 0xAA0000, 2f)) // dark red
    ;

    private final int color;
    private final DustColorTransitionOptions dustOption;
    private final DustColorTransitionOptions dustContour;
    RPGRarity(int color, DustColorTransitionOptions dustOption) {
        this.color = color;
        this.dustOption = dustOption;
        this.dustContour = new DustColorTransitionOptions(dustOption.fromColor-0x333333, dustOption.toColor-0x333333, 2f);
    }

    public int getColor() {
        return color;
    }

    public DustColorTransitionOptions getContour() {
        return dustContour;
    }
    public DustColorTransitionOptions getDustOption() {
        return dustOption;
    }
}
