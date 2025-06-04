package net.annova.object.item.sector0.oasis;

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

public class VolcanicStreamsMalma extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(9f);
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
        return List.of("&3= &fЭта рыба родом из раскалённых источников у подножия гор&3. &fЕё кожа пропитана магмой и способна передать сопротивление огню тому&3, &fкто осмелится её съесть");
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!AZURE_FISH.equals(event.getItem())) return;

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0));
    }
}
