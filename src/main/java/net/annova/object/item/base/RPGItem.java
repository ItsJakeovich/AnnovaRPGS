package net.annova.object.item.base;

import net.annova.object.RPGRarity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.annova.main.Keys.*;
import static net.minecraft.sounds.SoundEvents.PISTON_EXTEND;
import static org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack.asNMSCopy;

public abstract class RPGItem {
    public abstract Item type();
    public abstract List<Float> modelData();
    public abstract int stackSize();
    public abstract RPGRarity rarity();
    /**
     * Определяет, может ли быть использован предмет (PlayerInteractEvent)
     * @return true - не может, false - может.
     */
    public abstract boolean unusable();

    public static void subtractItem(Player player) {
        subtractItem(player, player.getInventory().getItemInMainHand());
    }
    public static void subtractItem(Player player, ItemStack item) {
        if (item.getAmount() > 1) item.setAmount(item.getAmount()-1);
        else player.getInventory().remove(item);

        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);
    }
    public static void subtractItem(ServerPlayer player, net.minecraft.world.item.ItemStack item) {
        int count = item.getCount();
        if (count > 1) item.setCount(count-1);
        else player.getInventory().removeItem(item);

        player.playSound(PISTON_EXTEND);
    }

    public static boolean hasItemId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(NK_ID, PersistentDataType.STRING);
    }
    @Nullable
    public static String getItemId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return getItemId(asNMSCopy(item));
    }
    @Nullable
    public static String getItemId(net.minecraft.world.item.ItemStack item) {
        if (item == null) return ""; // that's return the EMPTY item.
        CustomData data = item.get(DataComponents.CUSTOM_DATA);
        if (data == null) return null;
        return ((StringTag) data.getUnsafe().get("annovas:id")).value();
    }
}
