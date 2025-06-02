package net.annova.object.item.token;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import static net.annova.object.item.base.Item.SATURATION_TOKEN;

public class SaturationTokenItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
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
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
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
        if (!SATURATION_TOKEN.use(player)) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 72000, 0));
    }
}
