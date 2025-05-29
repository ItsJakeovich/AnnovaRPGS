package net.annova.item.crystal;

import net.annova.entity.traits.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.Keys.NK_BIOME;
import static org.bukkit.block.Biome.*;

public class BiomeStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return STR."\{ChatColor.GREEN}Частичный кристалл";
    }

    @Override
    public List<Float> modelData() {
        return List.of(0f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&7Когда то был частью чего-то большего..."
        );
    }

    private final Map<Biome, Integer> biomeColors = new HashMap<>() {{
        put(OCEAN, 0x4682B4);
        put(PLAINS, 0x8DB360);
        put(DESERT, 0xFFD700);
        put(WINDSWEPT_HILLS, 0x606060);
        put(FOREST, 0x228B22);
        put(TAIGA, 0x2E8B57);
        put(SWAMP, 0x507D2A);
        put(MANGROVE_SWAMP, 0x4CBB17);
        put(RIVER, 0x76EEC6);
        put(NETHER_WASTES, 0xDC143C);
        put(THE_END, 0x708090);
        put(FROZEN_OCEAN, 0x99D9EA);
        put(FROZEN_RIVER, 0xA3D8F0);
        put(SNOWY_PLAINS, 0xFFFFFF);
        put(MUSHROOM_FIELDS, 0x964B00);
        put(BEACH, 0xFFF5BA);
        put(JUNGLE, 0x00A36C);
        put(SPARSE_JUNGLE, 0x32CD32);
        put(DEEP_OCEAN, 0x00008B);
        put(STONY_SHORE, 0xA9A9A9);
        put(SNOWY_BEACH, 0xADD8E6);
        put(BIRCH_FOREST, 0xD7FFD7);
        put(DARK_FOREST, 0xE97451);
        put(PALE_GARDEN, 0xA0A6A0);
        put(SNOWY_TAIGA, 0xD8F0FA);
        put(OLD_GROWTH_PINE_TAIGA, 0xA0522D);
        put(WINDSWEPT_FOREST, 0x808000);
        put(SAVANNA, 0xCD853F);
        put(SAVANNA_PLATEAU, 0xB22222);
        put(BADLANDS, 0xFF8C00);
        put(WOODED_BADLANDS, 0xFF4500);
        put(SMALL_END_ISLANDS, 0x483D8B);
        put(END_MIDLANDS, 0x4A708B);
        put(END_HIGHLANDS, 0x4682B4);
        put(END_BARRENS, 0xFF7F50);
        put(WARM_OCEAN, 0x98FB98);
        put(LUKEWARM_OCEAN, 0xADD8E6);
        put(COLD_OCEAN, 0x1C86EE);
        put(DEEP_LUKEWARM_OCEAN, 0x191970);
        put(DEEP_COLD_OCEAN, 0x000000);
        put(DEEP_FROZEN_OCEAN, 0x507DCE);
        put(THE_VOID, 0x808080);
        put(SUNFLOWER_PLAINS, 0xF7FF7A);
        put(WINDSWEPT_GRAVELLY_HILLS, 0x556B2F);
        put(FLOWER_FOREST, 0xFFB6C1);
        put(ICE_SPIKES, 0xB4D3E6);
        put(OLD_GROWTH_BIRCH_FOREST, 0xC7EFC7);
        put(OLD_GROWTH_SPRUCE_TAIGA, 0x90EE90);
        put(WINDSWEPT_SAVANNA, 0xE97451);
        put(ERODED_BADLANDS, 0x8B4513);
        put(BAMBOO_JUNGLE, 0x7CFC00);
        put(SOUL_SAND_VALLEY, 0x4B0082);
        put(CRIMSON_FOREST, 0x00CED1);
        put(WARPED_FOREST, 0x8B0000);
        put(BASALT_DELTAS, 0x2F4F4F);
        put(DRIPSTONE_CAVES, 0x191970);
        put(LUSH_CAVES, 0x8B8989);
        put(DEEP_DARK, 0xDC143C);
        put(MEADOW, 0xF5FFFA);
        put(GROVE, 0xADD8E6);
        put(SNOWY_SLOPES, 0xE0E6E9);
        put(FROZEN_PEAKS, 0xFF004F);
        put(JAGGED_PEAKS, 0xCACACA);
        put(STONY_PEAKS, 0x778899);
        put(CHERRY_GROVE, 0xFF69B4);
    }};

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;

        ItemStack item = event.getItem();
        if (!equalItem(item)) return;
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);

        if (person.getStatus(Status.Type.BIOME_STONE) != null) return;
        person.addStatus(Status.Type.BIOME_STONE);
        player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);

        new BukkitRunnable() {
            final Location loc = player.getLocation();
            final World world = loc.getWorld();
            final Particle.DustOptions option = new Particle.DustOptions(Color.fromRGB(
                    item.getItemMeta().getPersistentDataContainer().get(NK_BIOME, PersistentDataType.INTEGER)),
                    3
            );

            @Override
            public void run() {
                if (!hasItem(player)) {
                    person.removeStatus(Status.Type.BIOME_STONE);
                    cancel();
                }
                world.spawnParticle(Particle.DUST, loc, 10, 0.5, 0.5, 0.5, option);
            }
        }.runTaskTimer(getInstance(), 0L, 5L);
    }
}
