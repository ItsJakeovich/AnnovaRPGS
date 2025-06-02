package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.REGENERATION;
import static net.annova.object.item.base.Item.REGENERATION_STONE;

public class RegenerationStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(325f);
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
                "&3= &fПассивно регенерирует &c\uD83D\uDDA4"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!REGENERATION_STONE.equals(player.getInventory().getItemInMainHand())) return;
        RPGPerson person = getRPGPerson(player);
        StatusManager manager = person.getStatusManager();
        if (manager.getStatus(REGENERATION) != null) return;
        manager.addStatus(REGENERATION);

        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);
    }
}
