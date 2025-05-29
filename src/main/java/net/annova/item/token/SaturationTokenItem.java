package net.annova.item.token;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class SaturationTokenItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public String name() {
        return ChatColor.GREEN + "Жетон насыщения";
    }

    @Override
    public List<Float> modelData() {
        return List.of(7F);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fДаёт на 1 час насыщение"
        );
    }

    @EventHandler
    private void use(PlayerInteractEvent event) {
        if (event.getAction().name().charAt(0) != 'R') return;
        Player player = event.getPlayer();
        if (!equalItem(player.getInventory().getItemInMainHand())) return;

        this.removeItem(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 72000, 0));
    }
}
