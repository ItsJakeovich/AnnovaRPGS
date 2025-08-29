package net.annova.event.summon;

import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.event.custom.MouseClickCE;
import net.annova.object.person.RPGPerson;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static net.annova.environment.world.GlobalWorld.worldSectors;
import static net.annova.main.GlobalRPG.getAreaChunk;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class ClickRPGEvent implements Listener {
    @EventHandler
    public void click(PlayerInteractEvent event) {
        // Проверка на город
        ServerPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
        RPGPerson person = getRPGPerson(player);
        ServerLevel level = player.serverLevel();
        Block block = event.getClickedBlock();
        if (block != null && worldSectors.containsKey(level)) {
            AreaChunk chunk = getAreaChunk(level, new ChunkPos(((CraftBlock) block).getPosition()));
            if (chunk != null && chunk.getTown() != person.getTown()) {
                event.setCancelled(true);
            }
        }
        ItemStack item = event.getItem();
        if (item != null && (person.getSectorLocation() != null || person.getDungeon() != null)) {
            switch (item.getType()) {
                case BUCKET:
                case WATER_BUCKET:
                case AXOLOTL_BUCKET:
                case COD_BUCKET:
                case LAVA_BUCKET:
                case MILK_BUCKET:
                case POWDER_SNOW_BUCKET:
                case PUFFERFISH_BUCKET:
                case SALMON_BUCKET:
                case TADPOLE_BUCKET:
                case TROPICAL_FISH_BUCKET:
                case ACACIA_BOAT:
                case BIRCH_BOAT:
                case CHERRY_BOAT:
                case JUNGLE_BOAT:
                case MANGROVE_BOAT:
                case OAK_BOAT:
                case SPRUCE_BOAT:
                {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // Запуск события MouseClickCE
        EquipmentSlot slot = event.getHand();
        if (slot == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        MouseClickCE.ClickType type = null;
        Action action = event.getAction();
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) type = MouseClickCE.ClickType.RIGHT_CLICK;
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) type = MouseClickCE.ClickType.LEFT_CLICK;

        if (type == null) return;
        MouseClickCE ev = new MouseClickCE(event.getPlayer(), event, type);
        Bukkit.getPluginManager().callEvent(ev);
    }
}
