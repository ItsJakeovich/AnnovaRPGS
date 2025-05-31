package net.annova.item.base;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.annova.main.Keys.*;

public abstract class RPGItem {
    public abstract Material type();
    public abstract List<Float> modelData();
    public abstract boolean unstackable();
    /**
     * | &0 = Черный
     * | &1 = Тёмно-синий
     * | &2 = Тёмно-зеленый
     * | &3 = Бирюзовый
     * | &4 = Тёмно-красный
     * | &5 = Тёмно-фиолетовый
     * | &6 = Золотой
     * | &7 = Серый
     * | &8 = Тёмно-серый
     * | &9 = Синий
     * | &a = Зеленый
     * | &b = Голубой
     * | &c = Красный
     * | &d = Сиреневый
     * | &e = Жёлтый
     * | &f = Белый
     * | &k = белиберда
     * | &l = Толстый
     * | &m = Линия
     * | &n = Подчеркнутый
     * | &o = Курсив
     * @return lore for item
     */
    public abstract List<String> lore();

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

        return item.getItemMeta().getPersistentDataContainer().get(NK_ID, PersistentDataType.STRING);
    }
}
