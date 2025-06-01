package net.annova.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.event.custom.EntityDamageCE;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.item.base.Item.DARK_STONE;
import static net.annova.item.base.Item.DEFENCE_STONE;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.PASSIVE_SHIELD;
import static net.annova.util.BukkitObjects.addItems;
import static net.annova.main.AnnovaRPG.getInstance;

public class DefenceStoneItem extends RPGItem implements Listener {
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
                "&3= &fНакладывает пассивно &3эффект сопративления",
                "&3= &fНакладывает пассивно &7эффект замедления"
        );
    }

    @EventHandler
    public void drop(EntityDamageCE event) {
        if (event.getBaseDamage() < 3) return;
        if (event.getEntity() instanceof Player player) {
            addItems(player, DARK_STONE.create(player, 0.0001));
        }
    }

    public static final PotionEffect DEFENCE_STONE_RESISTANCE = new PotionEffect(PotionEffectType.RESISTANCE, 120, 1, true, false, true);
    public static final PotionEffect DEFENCE_STONE_SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, 120, 0, true, false, true);

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (!DEFENCE_STONE.equals(event.getItem())) return;
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);
        StatusManager manager = person.getStatusManager();
        if (manager.getStatus(PASSIVE_SHIELD) != null) return;
        manager.addStatus(person, PASSIVE_SHIELD);
    }
}
