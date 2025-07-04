package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
import net.minecraft.world.item.Item;
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

import static net.annova.object.item.base.ItemType.BIOME_STONE;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.Keys.NK_BIOME;
import static org.bukkit.block.Biome.*;

public class BiomeStoneItem extends RPGItem implements Listener {
    @Override
    public Item type() {
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
        return RPGRarity.UNCOMMON;
    }

    private static final Map<Biome, Integer> biomeColors = new HashMap<>();
    static {
        biomeColors.put(OCEAN, 0x4682B4);
        biomeColors.put(PLAINS, 0x8DB360);
        biomeColors.put(DESERT, 0xFFD700);
        biomeColors.put(WINDSWEPT_HILLS, 0x606060);
        biomeColors.put(FOREST, 0x228B22);
        biomeColors.put(TAIGA, 0x2E8B57);
        biomeColors.put(SWAMP, 0x507D2A);
        biomeColors.put(MANGROVE_SWAMP, 0x4CBB17);
        biomeColors.put(RIVER, 0x76EEC6);
        biomeColors.put(NETHER_WASTES, 0xDC143C);
        biomeColors.put(THE_END, 0x708090);
        biomeColors.put(FROZEN_OCEAN, 0x99D9EA);
        biomeColors.put(FROZEN_RIVER, 0xA3D8F0);
        biomeColors.put(SNOWY_PLAINS, 0xFFFFFF);
        biomeColors.put(MUSHROOM_FIELDS, 0x964B00);
        biomeColors.put(BEACH, 0xFFF5BA);
        biomeColors.put(JUNGLE, 0x00A36C);
        biomeColors.put(SPARSE_JUNGLE, 0x32CD32);
        biomeColors.put(DEEP_OCEAN, 0x00008B);
        biomeColors.put(STONY_SHORE, 0xA9A9A9);
        biomeColors.put(SNOWY_BEACH, 0xADD8E6);
        biomeColors.put(BIRCH_FOREST, 0xD7FFD7);
        biomeColors.put(DARK_FOREST, 0xE97451);
        biomeColors.put(PALE_GARDEN, 0xA0A6A0);
        biomeColors.put(SNOWY_TAIGA, 0xD8F0FA);
        biomeColors.put(OLD_GROWTH_PINE_TAIGA, 0xA0522D);
        biomeColors.put(WINDSWEPT_FOREST, 0x808000);
        biomeColors.put(SAVANNA, 0xCD853F);
        biomeColors.put(SAVANNA_PLATEAU, 0xB22222);
        biomeColors.put(BADLANDS, 0xFF8C00);
        biomeColors.put(WOODED_BADLANDS, 0xFF4500);
        biomeColors.put(SMALL_END_ISLANDS, 0x483D8B);
        biomeColors.put(END_MIDLANDS, 0x4A708B);
        biomeColors.put(END_HIGHLANDS, 0x4682B4);
        biomeColors.put(END_BARRENS, 0xFF7F50);
        biomeColors.put(WARM_OCEAN, 0x98FB98);
        biomeColors.put(LUKEWARM_OCEAN, 0xADD8E6);
        biomeColors.put(COLD_OCEAN, 0x1C86EE);
        biomeColors.put(DEEP_LUKEWARM_OCEAN, 0x191970);
        biomeColors.put(DEEP_COLD_OCEAN, 0x000000);
        biomeColors.put(DEEP_FROZEN_OCEAN, 0x507DCE);
        biomeColors.put(THE_VOID, 0x808080);
        biomeColors.put(SUNFLOWER_PLAINS, 0xF7FF7A);
        biomeColors.put(WINDSWEPT_GRAVELLY_HILLS, 0x556B2F);
        biomeColors.put(FLOWER_FOREST, 0xFFB6C1);
        biomeColors.put(ICE_SPIKES, 0xB4D3E6);
        biomeColors.put(OLD_GROWTH_BIRCH_FOREST, 0xC7EFC7);
        biomeColors.put(OLD_GROWTH_SPRUCE_TAIGA, 0x90EE90);
        biomeColors.put(WINDSWEPT_SAVANNA, 0xE97451);
        biomeColors.put(ERODED_BADLANDS, 0x8B4513);
        biomeColors.put(BAMBOO_JUNGLE, 0x7CFC00);
        biomeColors.put(SOUL_SAND_VALLEY, 0x4B0082);
        biomeColors.put(CRIMSON_FOREST, 0x00CED1);
        biomeColors.put(WARPED_FOREST, 0x8B0000);
        biomeColors.put(BASALT_DELTAS, 0x2F4F4F);
        biomeColors.put(DRIPSTONE_CAVES, 0x191970);
        biomeColors.put(LUSH_CAVES, 0x8B8989);
        biomeColors.put(DEEP_DARK, 0xDC143C);
        biomeColors.put(MEADOW, 0xF5FFFA);
        biomeColors.put(GROVE, 0xADD8E6);
        biomeColors.put(SNOWY_SLOPES, 0xE0E6E9);
        biomeColors.put(FROZEN_PEAKS, 0xFF004F);
        biomeColors.put(JAGGED_PEAKS, 0xCACACA);
        biomeColors.put(STONY_PEAKS, 0x778899);
        biomeColors.put(CHERRY_GROVE, 0xFF69B4);
    }

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
