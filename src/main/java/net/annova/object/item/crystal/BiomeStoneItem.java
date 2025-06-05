package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
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

import static net.annova.object.item.base.Item.BIOME_STONE;
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
    public List<Float> modelData() {
        return List.of(0f);
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
                "&7Когда то был частью чего-то большего..."
        );
    }

    private final Map<Biome, Integer> biomeColors = new HashMap<>() {{
        this.put(OCEAN, 0x4682B4);
        this.put(PLAINS, 0x8DB360);
        this.put(DESERT, 0xFFD700);
        this.put(WINDSWEPT_HILLS, 0x606060);
        this.put(FOREST, 0x228B22);
        this.put(TAIGA, 0x2E8B57);
        this.put(SWAMP, 0x507D2A);
        this.put(MANGROVE_SWAMP, 0x4CBB17);
        this.put(RIVER, 0x76EEC6);
        this.put(NETHER_WASTES, 0xDC143C);
        this.put(THE_END, 0x708090);
        this.put(FROZEN_OCEAN, 0x99D9EA);
        this.put(FROZEN_RIVER, 0xA3D8F0);
        this.put(SNOWY_PLAINS, 0xFFFFFF);
        this.put(MUSHROOM_FIELDS, 0x964B00);
        this.put(BEACH, 0xFFF5BA);
        this.put(JUNGLE, 0x00A36C);
        this.put(SPARSE_JUNGLE, 0x32CD32);
        this.put(DEEP_OCEAN, 0x00008B);
        this.put(STONY_SHORE, 0xA9A9A9);
        this.put(SNOWY_BEACH, 0xADD8E6);
        this.put(BIRCH_FOREST, 0xD7FFD7);
        this.put(DARK_FOREST, 0xE97451);
        this.put(PALE_GARDEN, 0xA0A6A0);
        this.put(SNOWY_TAIGA, 0xD8F0FA);
        this.put(OLD_GROWTH_PINE_TAIGA, 0xA0522D);
        this.put(WINDSWEPT_FOREST, 0x808000);
        this.put(SAVANNA, 0xCD853F);
        this.put(SAVANNA_PLATEAU, 0xB22222);
        this.put(BADLANDS, 0xFF8C00);
        this.put(WOODED_BADLANDS, 0xFF4500);
        this.put(SMALL_END_ISLANDS, 0x483D8B);
        this.put(END_MIDLANDS, 0x4A708B);
        this.put(END_HIGHLANDS, 0x4682B4);
        this.put(END_BARRENS, 0xFF7F50);
        this.put(WARM_OCEAN, 0x98FB98);
        this.put(LUKEWARM_OCEAN, 0xADD8E6);
        this.put(COLD_OCEAN, 0x1C86EE);
        this.put(DEEP_LUKEWARM_OCEAN, 0x191970);
        this.put(DEEP_COLD_OCEAN, 0x000000);
        this.put(DEEP_FROZEN_OCEAN, 0x507DCE);
        this.put(THE_VOID, 0x808080);
        this.put(SUNFLOWER_PLAINS, 0xF7FF7A);
        this.put(WINDSWEPT_GRAVELLY_HILLS, 0x556B2F);
        this.put(FLOWER_FOREST, 0xFFB6C1);
        this.put(ICE_SPIKES, 0xB4D3E6);
        this.put(OLD_GROWTH_BIRCH_FOREST, 0xC7EFC7);
        this.put(OLD_GROWTH_SPRUCE_TAIGA, 0x90EE90);
        this.put(WINDSWEPT_SAVANNA, 0xE97451);
        this.put(ERODED_BADLANDS, 0x8B4513);
        this.put(BAMBOO_JUNGLE, 0x7CFC00);
        this.put(SOUL_SAND_VALLEY, 0x4B0082);
        this.put(CRIMSON_FOREST, 0x00CED1);
        this.put(WARPED_FOREST, 0x8B0000);
        this.put(BASALT_DELTAS, 0x2F4F4F);
        this.put(DRIPSTONE_CAVES, 0x191970);
        this.put(LUSH_CAVES, 0x8B8989);
        this.put(DEEP_DARK, 0xDC143C);
        this.put(MEADOW, 0xF5FFFA);
        this.put(GROVE, 0xADD8E6);
        this.put(SNOWY_SLOPES, 0xE0E6E9);
        this.put(FROZEN_PEAKS, 0xFF004F);
        this.put(JAGGED_PEAKS, 0xCACACA);
        this.put(STONY_PEAKS, 0x778899);
        this.put(CHERRY_GROVE, 0xFF69B4);
    }};

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;

        ItemStack item = event.getItem();
        if (!BIOME_STONE.equals(item)) return;
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);

        StatusManager manager = person.getStatusManager();
        if (manager.hasStatus(StatusType.BIOME_STONE)) return;
        manager.addStatus(StatusType.BIOME_STONE);
        player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);

        new BukkitRunnable() {
            final Location loc = player.getLocation();
            final World world = loc.getWorld();
            final Particle.DustOptions option = new Particle.DustTransition(Color.GREEN, Color.fromRGB(
                    item.getItemMeta().getPersistentDataContainer().get(NK_BIOME, PersistentDataType.INTEGER)),
                    3
            );

            @Override
            public void run() {
                if (!BIOME_STONE.has(player)) {
                    manager.removeStatus(StatusType.BIOME_STONE);
                    this.cancel();
                }
                world.spawnParticle(Particle.DUST, loc, 10, 0.5, 0.5, 0.5, option);
            }
        }.runTaskTimer(getInstance(), 0L, 5L);
    }
}
