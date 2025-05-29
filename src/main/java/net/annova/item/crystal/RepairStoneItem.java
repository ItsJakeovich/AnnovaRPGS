package net.annova.item.crystal;

import net.annova.entity.traits.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.addItems;
import static net.annova.util.BukkitObjects.chance;

public class RepairStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.GRAY + "Кристалл ремесла";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{307};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fВостанавливает прочность выбраного предмета"
        );
    }

    @EventHandler
    private void dropBlock(BlockBreakEvent event) {
        Material material = event.getBlock().getType();

        if (!material.equals(Material.CLAY)
                && !material.equals(Material.SAND)
                && !material.equals(Material.GRAVEL)
        ) return;

        if (!chance(0.0005)) return;
        Location location = event.getBlock().getLocation();
        location.getWorld().dropItemNaturally(location, this.create());
    }
    @EventHandler
    private void dropItem(PlayerItemBreakEvent event) {
        if (event.getBrokenItem().getType().getMaxDurability() == 0) return;
        if (!chance(0.0005)) return;
        addItems(event.getPlayer(), this.create());
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;

        ItemStack item = event.getItem();
        if (!equalItem(item)) return;
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);

        if (person.getStatus(Status.Type.REPAIR_COOLDOWN) != null) return;

        Inventory repairInventory = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Нажатием почините предмет:");
        for (ItemStack content : player.getInventory().getContents()) {
            if (content == null) continue;
            repairInventory.addItem(content);
        }
        player.openInventory(repairInventory);
    }
    @EventHandler
    private void click(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.BLUE + "Нажатием почините предмет:")) return;
        event.setCancelled(true);
        ItemStack item =  event.getCurrentItem();
        if (item == null) return;
        if (!item.hasItemMeta()) return;
        if (!(item.getItemMeta() instanceof Damageable)) return;
        Player player = (Player) event.getWhoClicked();
        repairItem(player, item);

        RPGPerson person = getRPGPerson(player);
        person.addStatus(Status.Type.REPAIR_COOLDOWN);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () ->
                person.removeStatus(Status.Type.REPAIR_COOLDOWN)
                , 18000);

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
