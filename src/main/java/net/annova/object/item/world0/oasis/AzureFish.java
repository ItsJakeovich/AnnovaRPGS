package net.annova.object.item.world0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.object.item.base.Item.AZURE_FISH;

public class AzureFish extends RPGItem implements Listener {
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
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fОкрашенная небом и океаном одновременн&3, &fэта рыба оставляет после себя ощущение свежести и лёгкости&3. &fЕё вкус будто насыщает не только тело&3, &fно и разум");
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!AZURE_FISH.equals(event.getItem())) return;

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 18000, 0));
    }
}
