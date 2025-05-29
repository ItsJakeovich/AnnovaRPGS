package net.annova.event;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;

public class AnvilEvent implements Listener {
    @EventHandler
    private void onUse(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack base = inventory.getItem(0); // Основной предмет
        ItemStack addition = inventory.getItem(1); // Книга

        // Проверяем, что оба слота заполнены, и дополнительный предмет - книга
        if (base == null || addition == null) return;
        if (base.getType().equals(Material.ENCHANTED_BOOK) || !addition.getType().equals(Material.ENCHANTED_BOOK)) return;

        ItemStack result = base.clone(); // Копируем основной предмет
        EnchantmentStorageMeta additionMeta = (EnchantmentStorageMeta) addition.getItemMeta();

        if (additionMeta != null) {
            Map<Enchantment, Integer> additionEnchantments = additionMeta.getStoredEnchants();

            // Переносим зачарования с книги на предмет
            for (Map.Entry<Enchantment, Integer> entry : additionEnchantments.entrySet()) {
                Enchantment enchantment = entry.getKey();
                int level = entry.getValue();

                // Накладываем зачарование, игнорируя стандартные ограничения
                result.addUnsafeEnchantment(enchantment, level);
            }

            // Устанавливаем изменённый результат
            event.setResult(result);
        }
    }
}
