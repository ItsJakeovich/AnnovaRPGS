package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.event.custom.EntityDamageCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.object.item.base.Item.DARK_STONE;
import static net.annova.object.item.base.Item.DEFENCE_STONE;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.PASSIVE_SHIELD;
import static net.annova.util.BukkitObjects.addItems;

public class DefenceStoneItem extends RPGItem {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(324f);
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
                "&3| &fНакладывает пассивно эффект &3сопративления",
                "&3| &fНакладывает пассивно эффект  &7замедления"
        );
    }

    public static final PotionEffect DEFENCE_STONE_RESISTANCE = new PotionEffect(PotionEffectType.RESISTANCE, 120, 1, true, false, true);
    public static final PotionEffect DEFENCE_STONE_SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, 120, 0, true, false, true);
}
