package net.annova.object.item.sector0.oasis;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.FIRE_FISH;
import static net.annova.object.person.status.StatusType.FIRE_FISH_DAMAGE_BUFF;

public class FireFish extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(2f);
    }

    @Override
    public int stackSize() {
        return 16;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    PotionEffect poison = new PotionEffect(PotionEffectType.POISON, 200, 0);
    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!FIRE_FISH.equals(event.getItem())) return;

        Player player = event.getPlayer();
        getRPGPerson(player).getStatusManager().addStatus(FIRE_FISH_DAMAGE_BUFF);
        player.addPotionEffect(poison);
    }
}
