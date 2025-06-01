package net.annova.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.person.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

import static net.annova.item.base.Item.PSYONIC_STONE;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.TELEKINESIS;

public class PsyonicStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(328f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fСоздает слабое притягивающие магнитное поле"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!PSYONIC_STONE.equals(item)) return;
        RPGPerson person = getRPGPerson(player);
        StatusManager manager = person.getStatusManager();
        if (manager.getStatus(TELEKINESIS) != null) return;
        manager.addStatus(person, TELEKINESIS);
        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);
    }
}
