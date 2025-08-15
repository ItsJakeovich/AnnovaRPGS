package net.annova.environment.dungeon.event;

import net.annova.environment.dungeon.Dungeon;
import net.annova.event.custom.NonCommandBlockRestrictionCE;
import net.annova.object.person.Person;
import net.annova.object.person.RPGPerson;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getDungeonWorld;
import static net.annova.main.GlobalRPG.getRPGPerson;

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
        person.send(getInstance(), "dungeon-key-confirm");
    }

    @EventHandler
    public void interactGate(PlayerInteractEvent event) {
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);
        Dungeon dungeon = person.getDungeon();
        if (dungeon == null) return;
        Block block = event.getClickedBlock();
        if (block == null || !block.getType().name().endsWith("WALL")) return;

        person.setReady(!person.isReady());
        player.sendMessage(String.valueOf(person.isReady()));

        dungeon.startIfTrue();
    }

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

//    @EventHandler
//    public void spawn(EntitySpawnEvent event) {
//        if (!event.getLocation().getWorld().equals(getDungeonWorld())) return;
//
//        Entity entity = event.getEntity();
//        if (entity instanceof ArmorStand) return;
//        if (!(entity instanceof LivingEntity)) return;
//        if (entity.getPersistentDataContainer().has(NK_ID)) return;
//
//        event.setCancelled(true);
//    }
    @EventHandler
    public void destroy(NonCommandBlockRestrictionCE event) {
        ServerPlayer player = event.getPlayer();
        if (!player.serverLevel().equals(getDungeonWorld())) return;
        if (player.server.getPlayerList().isOp(player.gameProfile)) return;
        event.setCancelled(true);
    }
    @EventHandler
    public void explode(EntityExplodeEvent event) {
        if (!event.getLocation().getWorld().equals(getDungeonWorld())) return;
        event.blockList().clear();
    }
}
