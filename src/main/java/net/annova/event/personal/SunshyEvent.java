package net.annova.event.personal;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.environment.dungeon.Loot.random;
import static net.annova.util.BukkitObjects.addItems;

public class SunshyEvent implements Listener {
    private static int count = 0;
    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals("Sunshyyy")) return;
        if (count > 200) return;
        addItems(player, createItem(getRandomMaterial()));
        count++;
    }

    private static final Set<Material> mats = Set.of(
            Material.BEDROCK,
            Material.BARRIER,
            Material.COMMAND_BLOCK,
            Material.COMMAND_BLOCK_MINECART,
            Material.REPEATING_COMMAND_BLOCK,
            Material.SPAWNER,
            Material.STRUCTURE_BLOCK,
            Material.JIGSAW,
            Material.LIGHT,
            Material.DEBUG_STICK,
            Material.KNOWLEDGE_BOOK,
            Material.CHAIN_COMMAND_BLOCK
    );
    private Material getRandomMaterial() {
        List<Material> materials = Arrays.stream(Material.values())
                .filter(material -> material.isItem() && !material.name().endsWith("EGG") && !mats.contains(material))
                .toList();
        return materials.get(random.nextInt(materials.size()));
    }

}
