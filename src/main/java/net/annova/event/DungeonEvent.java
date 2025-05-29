package net.annova.event;

import net.annova.environment.dungeon.Dungeon;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getDungeonWorld;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.Keys.NK_ID;

public class DungeonEvent implements Listener {
    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        if (!event.hasItem()) return;
        ItemStack item = event.getItem();
        if (!item.getType().equals(Material.NAUTILUS_SHELL)) return;
        if (!item.getItemMeta().hasCustomModelData()) return;
        if (item.getItemMeta().getCustomModelData() != 3) return;
        Player player = event.getPlayer();
        Person person = getPerson(player);
        if (!person.isAuthorizated()) return;
        if (getRPGPerson(player).getDungeon() != null) {
            person.send(getInstance(),"dungeon-key-already");
            return;
        }
        person.getConfirm().setConfirmType("dungeon");
        person.send(getInstance(), "dungeon-key");
    }

    @EventHandler
    public void login(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        RPGPerson pers = getRPGPerson(player);
        if (!player.getWorld().equals(getDungeonWorld())) return;
        if (pers.getDungeon() != null) return;

        Location location = player.getRespawnLocation();
        if (location == null) location = Bukkit.getWorld("world").getSpawnLocation();
        pers.teleport(location);
        player.setGameMode(GameMode.SURVIVAL);
    }
    @EventHandler
    private void teleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);
        if (person.isTeleportAccess()) {
            person.setTeleportAccess(false);
            return;
        }
        if (!player.getWorld().equals(getDungeonWorld())) return;
        Dungeon dungeon = person.getDungeon();
        if (dungeon == null) return;
        if (dungeon.isClose()) return;
        event.setCancelled(true);
    }
    @EventHandler
    public void touch(PlayerInteractEvent event) {
        RPGPerson person = getRPGPerson(event.getPlayer());
        Dungeon dungeon = person.getDungeon();
        if (dungeon == null) return;
        if (dungeon.getBase().getTemplate().getId() != 0) return;
        if (event.hasItem()) {
            if (event.getItem().getType().equals(Material.ENDER_PEARL)) event.setCancelled(true);
            if (event.getItem().getType().equals(Material.CHORUS_FRUIT)) event.setCancelled(true);
        }
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType().name().endsWith("TRAPDOOR")) event.setCancelled(true);
        }
    }

    @EventHandler
    public void spawn(EntitySpawnEvent event) {
        if (!event.getLocation().getWorld().equals(getDungeonWorld())) return;

        Entity entity = event.getEntity();
        if (entity instanceof ArmorStand) return;
        if (!(entity instanceof LivingEntity)) return;
        if (entity.getPersistentDataContainer().has(NK_ID)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void place(BlockPlaceEvent event) {
        if (!event.getBlock().getLocation().getWorld().equals(getDungeonWorld())) return;
        if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }
    @EventHandler
    public void destroy(BlockBreakEvent event) {
        if (!event.getBlock().getLocation().getWorld().equals(getDungeonWorld())) return;
        if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }
    @EventHandler
    public void explode(EntityExplodeEvent event) {
        if (!event.getLocation().getWorld().equals(getDungeonWorld())) return;
        event.blockList().clear();
    }
}
