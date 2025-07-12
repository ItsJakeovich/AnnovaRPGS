package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.object.item.base.ItemType.AZURE_FISH;

public class AzureFish extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(15f);
    }

    @Override
    public int stackSize() {
        return 16;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!AZURE_FISH.equals(event.getItem())) return;

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 18000, 0));
    }
}
