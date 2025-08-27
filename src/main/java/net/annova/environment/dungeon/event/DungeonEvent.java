package net.annova.environment.dungeon.event;

import net.annova.environment.dungeon.Dungeon;
import net.annova.object.person.RPGPerson;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;

import static net.annova.environment.dungeon.GlobalDungeon.getDungeonWorld;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class DungeonEvent implements Listener {
    @EventHandler
    public void login(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        RPGPerson pers = getRPGPerson(player);
        if (pers == null) return;
        if (!player.getWorld().equals(getDungeonWorld())) return;
        if (pers.getDungeon() != null) return;

        Location location = player.getRespawnLocation();
        if (location == null) location = Bukkit.getWorld("world").getSpawnLocation();
        pers.teleport(location);
        player.setGameMode(GameMode.SURVIVAL);
    }
//    @EventHandler
//    private void teleport(PlayerTeleportEvent event) {
//        Player player = event.getPlayer();
//        RPGPerson person = getRPGPerson(player);
//        if (person.isTeleportAccess()) {
//            person.setTeleportAccess(false);
//            return;
//        }
//        if (!player.getWorld().equals(getDungeonWorld())) return;
//        Dungeon dungeon = person.getDungeon();
//        if (dungeon == null) return;
//        if (dungeon.isClose()) return;
//        event.setCancelled(true);
//    }
    @EventHandler
    public void touch(PlayerInteractEvent event) {
        RPGPerson person = getRPGPerson(event.getPlayer());
        Dungeon dungeon = person.getDungeon();
        if (dungeon != null && dungeon.getBase().getTemplate().getId() == 0) {
            if (event.hasItem()) {
                if (event.getItem().getType().equals(Material.ENDER_PEARL)) event.setCancelled(true);
                if (event.getItem().getType().equals(Material.CHORUS_FRUIT)) event.setCancelled(true);
            }
            if (event.getClickedBlock() != null) {
                if (event.getClickedBlock().getType().name().endsWith("TRAPDOOR")) event.setCancelled(true);
            }

            if (!dungeon.isStart()) {
                // Открытие подземелья
                EquipmentSlot slot = event.getHand();
                if (slot == EquipmentSlot.HAND && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Player player = event.getPlayer();
                    Block block = event.getClickedBlock();
                    if (block == null || !block.getType().name().endsWith("WALL")) return;

                    person.setReady(!person.isReady());

                    dungeon.startIfTrue();
                }
            }
        }
    }

//    @EventHandler
//    public void explode(EntityExplodeEvent event) {
//        if (!event.getLocation().getWorld().equals(getDungeonWorld())) return;
//        event.blockList().clear();
//    }
}
