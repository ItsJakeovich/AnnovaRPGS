package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.item.base.Item.ICE_STONE;
import static net.annova.object.person.status.StatusType.ICENAGE;

public class IceStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(305f);
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
                "&3= &fВ радиусе 7 блоков &3замедляет &fигроков"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;

        ItemStack item = event.getItem();
        if (!ICE_STONE.equals(item)) return;
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);

        StatusManager manager = person.getStatusManager();
        if (manager.getStatus(ICENAGE) != null) return;
        manager.addStatus(player, ICENAGE);
        player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
    }
}
