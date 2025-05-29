package net.annova.item.container;

import net.annova.item.base.RPGItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.environment.dungeon.Loot.random;
import static net.annova.item.base.Item.*;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.*;

public class PrisonContainerItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public String name() {
        return ChatColor.GOLD + "Пыльный контейнер";
    }

    @Override
    public List<Float> modelData() {
        return List.of(10f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fБольшой пыльный контейнер"
        );
    }

    @EventHandler
    private void secure(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Контейнер")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getAction().name().charAt(0) != 'R') return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!PRISON_CONTAINER.equals(item)) return;

        removeItem(player, item);
        Inventory inv = Bukkit.createInventory(null, 45, "Контейнер");
        ItemStack[] items = new ItemStack[45];

        for (int i = 0; i < 45; i++) {
            ItemStack[] list = getItems();
            ItemStack cont = new ItemStack(Material.AIR);
            while (cont.getType() == Material.AIR) {
                cont = list[random.nextInt(list.length)];
            }
            items[i] = cont;
        }

        // 0 1 2 3 4 5 6 7 8
        // 9 1 2 3 4 5 6 7 8
        // 18 1 2 3 22 5 6 7 8
        // 27 1 2 3 4 5 6 7 8
        // 36 1 2 3 4 5 6 7 8
        // 45
        new BukkitRunnable() {
            private int last = 0;
            private final ItemStack glass = getNewItem(Material.GREEN_STAINED_GLASS_PANE);
            @Override
            public void run() {
                if (!player.isValid() || !player.isOnline()) this.cancel();

                for (int i = 0; i < 45; i++) {
                    int item = last + i;
                    if (item >= 45) item -= 45;
                    switch (i) {
                        case 21, 23, 13, 31 -> {
                            inv.setItem(i, glass);
                            continue;
                        }
                    }
                    inv.setItem(i, items[item]);
                }
                if (chance(0.025)) {
                    addItems(player, inv.getItem(22));
                    player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
                    player.closeInventory();
                    this.cancel();
                }

                if (last == 45) {
                    last = 0;
                }
                last++;

                if (!player.getOpenInventory().getTopInventory().equals(inv)) {
                    player.openInventory(inv);
                }
            }
        }.runTaskTimer(getInstance(), 0L, 2L);
    }

    public static ItemStack[] getItems() {
        return new ItemStack[] {
                new ItemStack(Material.PAPER, 64),
                new ItemStack(Material.DIAMOND, 24),
                new ItemStack(Material.EMERALD, 16),
                new ItemStack(Material.IRON_INGOT, 64),
                new ItemStack(Material.GOLD_INGOT, 48),
                PRISON_TOKEN.create(4, 0.6),

                PRISON_HELMET.create(0.2),
                PRISON_CHESTPLATE.create(0.2),
                PRISON_LEGGINGS.create(0.2),
                PRISON_BOOTS.create(0.2),
                PRISON_STEEL_SWORD.create(0.2),

                FLY_TOKEN.create(0.033),
                SATURATION_TOKEN.create(0.033),
                CREEPER_TOKEN.create(0.033),
//                new TeleportTokenItem().create(0.033),
                DURABILITY_TOKEN.create(0.033),
                RANGE_TOKEN.create(0.02)
        };
    }
}
