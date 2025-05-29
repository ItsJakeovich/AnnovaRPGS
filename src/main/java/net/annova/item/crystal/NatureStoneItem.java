package net.annova.item.crystal;

import net.annova.entity.traits.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.chance;

public class NatureStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.GREEN + "Кристалл природы";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{309};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fУскоряет рост растений в радиусе"
        );
    }

    @EventHandler
    private void drop(BlockBreakEvent event) {
        Block block = event.getBlock();

        double chance;
        if (block.getType().name().endsWith("_LEAVES")) chance = 0.005;
        else if (block instanceof Ageable) chance = 0.003;
        else return;

        if (!chance(chance)) return;
        block.getWorld().dropItemNaturally(block.getLocation(), this.create());
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!equalItem(item)) return;
        RPGPerson person = getRPGPerson(player);
        if (person.getStatus(Status.Type.AURA_LIFE) != null) return;
        person.addStatus(Status.Type.AURA_LIFE);
        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);

        new BukkitRunnable() {
            public void run() {
                if (!hasItem(player)) {
                    person.removeStatus(Status.Type.AURA_LIFE);
                    cancel();
                }
                for (int y = -5; y <= 5; y++) {
                    for (int z = -3; z <= 4; z++) {
                        for (int x = -5; x <= 5; x++) {
                            Block block = player.getLocation().add(x, y, z).getBlock();
                            Material type = block.getType();

                            // Проверяем тип блока и обновляем его состояние
                            if (type == Material.WHEAT || type == Material.CARROTS || type == Material.POTATOES || type == Material.BEETROOTS) {
                                if (block.getBlockData() instanceof Ageable ageable) {
                                    if (ageable.getAge() == ageable.getMaximumAge()) continue;
                                    ageable.setAge(ageable.getAge()+1);
                                    block.setBlockData(ageable);
                                }
                            } else if (type == Material.GRASS_BLOCK) {
                                if (!chance(0.05)) continue;
                                block.getWorld().generateTree(block.getLocation(), TreeType.TREE); // Пример: вырастить дерево
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(getInstance(), 0L, 840L);
    }
}
