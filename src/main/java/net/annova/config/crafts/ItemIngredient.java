package net.annova.config.crafts;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemType;

public class ItemIngredient {
    private final Item material;
    private final int count;
    private final char letter;
    public ItemIngredient(Material material, int count, String letter) {
        this(CraftItemType.bukkitToMinecraft(material), count, letter);
    }
    public ItemIngredient(Item item, int count, String letter) {
        this.material = item;
        this.count = count;
        if (letter == null) this.letter = ' ';
        else this.letter = letter.charAt(0);
    }

    public Item getMaterial() {
        return material;
    }

    public int getCount() {
        return count;
    }

    public char getLetter() {
        return letter;
    }
}
