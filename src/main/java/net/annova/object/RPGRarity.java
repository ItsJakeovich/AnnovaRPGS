package net.annova.object;

public enum RPGRarity {
    EVENT(0x6060FF), // blue

    COMMON(0xAAAAAA), // gray
    UNCOMMON(0x55FF55), // green

    RARE(0x55FFFF), // aqua
    ELITE(0xFF55FF), // light purple
    EPIC(0xAA00AA), // dark purple
    LEGEND(0xFFAA00), // gold
    MYTHIC(0xFF5555), // red
    IMPOSSIBLE(0xAA0000) // dark red
    ;

    private final int color;
    RPGRarity(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
