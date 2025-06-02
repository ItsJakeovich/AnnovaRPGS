package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static net.annova.object.item.base.Item.REPAIR_STONE;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.REPAIR_COOLDOWN;

public class RepairStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(307f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fВостанавливает прочность выбраного предмета"
        );
    }

    private final String title = STR."\{ChatColor.BLUE}Нажатием почините предмет:";
    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;

        ItemStack item = event.getItem();
        if (!REPAIR_STONE.equals(item)) return;
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);

        if (person.getStatusManager().getStatus(REPAIR_COOLDOWN) != null) return;

        Inventory repairInventory = Bukkit.createInventory(null, 54, title);
        for (ItemStack content : player.getInventory().getContents()) {
            if (content == null) continue;
            repairInventory.addItem(content);
        }
        player.openInventory(repairInventory);
    }
    @EventHandler
    private void click(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(title)) return;
        event.setCancelled(true);
        ItemStack item =  event.getCurrentItem();
        if (item == null) return;
        if (!item.hasItemMeta()) return;
        if (!(item.getItemMeta() instanceof Damageable)) return;
        Player player = (Player) event.getWhoClicked();
        this.repairItem(player, item);

        RPGPerson person = getRPGPerson(player);
        person.getStatusManager().addStatus(REPAIR_COOLDOWN);

        event.getWhoClicked().closeInventory();
    }

    private void repairItem(Player player, ItemStack toRepair) {
        Inventory playerInventory = player.getInventory();

        // Находим и восстанавливаем предмет в инвентаре игрока
        for (ItemStack item : playerInventory.getContents()) {
            if (item == null || !item.isSimilar(toRepair)) continue;
            if (item.getType().getMaxDurability() > 0) {
                ItemMeta itemMeta = item.getItemMeta();
                if (itemMeta instanceof Damageable damageable) {
                    // Устанавливаем урон предмета в 0 (восстанавливаем всю прочность)
                    damageable.setDamage(0);
                    item.setItemMeta(damageable);

                    // Воспроизводим звук восстановления
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }
}
