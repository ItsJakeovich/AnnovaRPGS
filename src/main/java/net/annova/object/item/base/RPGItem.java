package net.annova.object.item.base;

import net.annova.object.RPGRarity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.Item;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.annova.main.Keys.*;
import static org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack.asNMSCopy;

public abstract class RPGItem {
    public abstract Item type();
    public abstract List<Float> modelData();
    public abstract int stackSize();
    public abstract RPGRarity rarity();

    public static void subtractItem(Player player) {
        subtractItem(player, player.getInventory().getItemInMainHand());
    }
    public static void subtractItem(Player player, ItemStack item) {
        if (item.getAmount() > 1) item.setAmount(item.getAmount()-1);
        else player.getInventory().remove(item);

        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);
    }

    public static boolean hasItemId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(NK_ID, PersistentDataType.STRING);
    }
    @Nullable
    public static String getItemId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;

        return ((StringTag) asNMSCopy(item).get(DataComponents.CUSTOM_DATA).getUnsafe().get("annovas:id")).value();
    }
}
