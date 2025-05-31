package net.annova.item.crystal;

import net.annova.object.person.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.AURA_LIFE;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.item.base.Item.NATURE_STONE;

public class NatureStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(309f);
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
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!NATURE_STONE.equals(item)) return;
        RPGPerson person = getRPGPerson(player);
        StatusManager manager = person.getStatusManager();
        if (manager.getStatus(AURA_LIFE) != null) return;
        manager.addStatus(AURA_LIFE);
        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);

        new BukkitRunnable() {
            public void run() {
                if (!NATURE_STONE.has(player)) {
                    manager.removeStatus(AURA_LIFE);
                    this.cancel();
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
