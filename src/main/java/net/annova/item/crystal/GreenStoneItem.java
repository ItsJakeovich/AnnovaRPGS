package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.item.base.Item.GREEN_STONE;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.GREEN_STONE_HEALTH_BUFF;

public class GreenStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(301f);
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
                "&3= &fДобавляет 8 дополнительного &cздоровья &fигроку"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!GREEN_STONE.equals(item)) return;
        RPGPerson person = getRPGPerson(player);
        person.getStatusManager().addStatus(player, GREEN_STONE_HEALTH_BUFF);

        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 0, true, false, true));
    }
}
