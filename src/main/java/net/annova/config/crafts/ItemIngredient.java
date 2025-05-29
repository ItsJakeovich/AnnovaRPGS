package net.annova.config.crafts;

import org.bukkit.inventory.ItemStack;

public class ItemIngredient {
    private final ItemStack material;
    private final int count;
    private final char letter;
    public ItemIngredient(ItemStack material, int count, String letter) {
        this.material = material;
        this.count = count;
        if (letter == null) this.letter = ' ';
        else this.letter = letter.charAt(0);
    }

    public ItemStack getMaterial() {
        return material;
    }

    public int getCount() {
        return count;
    }

    public char getLetter() {
        return letter;
    }
}
