package net.annova.object.item.container;

import net.annova.object.item.base.RPGItem;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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

import static net.annova.object.item.base.ItemType.*;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.*;

public class PrisonContainerItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(10f);
    }

    @Override
    public int stackSize() {
        return 4;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public boolean unusable() {
        return false;
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
        if (!PRISON_CONTAINER.use(player, item)) return;

        Inventory inv = Bukkit.createInventory(null, 45, "Контейнер");
        ItemStack[] items = new ItemStack[45];

        for (int i = 0; i < 45; i++) {
            net.minecraft.world.item.ItemStack[] list = getItems(getPerson(player));
            net.minecraft.world.item.ItemStack cont = net.minecraft.world.item.ItemStack.EMPTY;
            while (cont.getItem() == Items.AIR) {
                cont = list[random.nextInt(list.length)];
            }
            items[i] = CraftItemStack.asBukkitCopy(cont);
        }

        // 0 1 2 3 4 5 6 7 8
        // 9 1 2 3 4 5 6 7 8
        // 18 1 2 3 22 5 6 7 8
        // 27 1 2 3 4 5 6 7 8
        // 36 1 2 3 4 5 6 7 8
        // 45
        new BukkitRunnable() {
            private int last = 0;
            private final ItemStack glass = CraftItemStack.asBukkitCopy(getNewItem(Items.GREEN_STAINED_GLASS_PANE));
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

    public static net.minecraft.world.item.ItemStack[] getItems(Person person) {
        return new net.minecraft.world.item.ItemStack[] {
                new net.minecraft.world.item.ItemStack(Items.PAPER, 12),
                new net.minecraft.world.item.ItemStack(Items.DIAMOND, 2),
                new net.minecraft.world.item.ItemStack(Items.EMERALD, 1),
                new net.minecraft.world.item.ItemStack(Items.IRON_INGOT, 12),
                new net.minecraft.world.item.ItemStack(Items.GOLD_INGOT, 5),
                new net.minecraft.world.item.ItemStack(Items.COPPER_INGOT, 9),
                PRISON_TOKEN.create(person, 4, 0.6),

                PRISON_HELMET.create(person, 0.2),
                PRISON_CHESTPLATE.create(person, 0.2),
                PRISON_LEGGINGS.create(person, 0.2),
                PRISON_BOOTS.create(person, 0.2),
                PRISON_STEEL_SWORD.create(person, 0.2),

                FLY_TOKEN.create(person, 0.033),
                SATURATION_TOKEN.create(person, 0.033),
//                CREEPER_TOKEN.create(person, 0.033),
//                new TeleportTokenItem().create(0.033),
                DURABILITY_TOKEN.create(person, 0.033),
                RANGE_TOKEN.create(person, 0.02)
        };
    }
}
