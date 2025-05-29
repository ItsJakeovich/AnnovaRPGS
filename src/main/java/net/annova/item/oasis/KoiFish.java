package net.annova.item.oasis;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.item.base.Item.KOI_FISH;

public class KoiFish extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of();
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fСимвол удачи и силы&3, &fэтот карп будто вобрал в себя энергию всех потоков Оазиса");
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!KOI_FISH.equals(event.getItem())) return;

        Player player = event.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 1));
        player.setFoodLevel(20);
    }
}
