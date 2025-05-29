package net.annova.item.oasis;

import net.annova.item.base.RPGItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.annova.event.CapitatorEvent.Capitator;
import static net.annova.item.base.Item.OASIS_FISH;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.random;

public class OasisFish extends RPGItem implements Listener {
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
        return List.of("&3= &fРыба&3, &fсловно забытая капля жизни в мире жара и пыли&3. &fЕё окраска &3— &fмягкая бирюза с отблесками песчаного золота &3— &fнапоминает о прохладных водах&3, спрятанных в сердце безжизненной пустыни&f. Она не даёт эффектов&3, &fно её появление всегда ощущается как облегчение&3.");
    }

    private final String[] messageKeys = {
            "s0-oasis-oasis-0",
            "s0-oasis-oasis-1",
            "s0-oasis-oasis-2",
            "s0-oasis-oasis-3",
            "s0-oasis-oasis-4",
    };
    private final Set<PotionEffect> effects = Set.of(
            new PotionEffect(PotionEffectType.LUCK, 72000, 9),
            new PotionEffect(PotionEffectType.RESISTANCE, 72000, 0),
            new PotionEffect(PotionEffectType.SATURATION, 72000, 0)
    );
    private final Map<Player, Long> cooldown = new HashMap<>();
    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        if (!OASIS_FISH.equals(item.getItemStack())) return;
        final Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        if (cooldown.containsKey(player)) if (now < cooldown.get(player)) return;
        this.cooldown.put(player, now + 14_400_000); // 4 hours

        new BukkitRunnable() {
            private Location location = item.getLocation();
            public void run() {
                Location newLocation = item.getLocation();
                if (!item.isValid() || location.equals(newLocation) || newLocation.add(newLocation.getDirection().normalize()).getBlock().getType().equals(Material.WATER)) {
                    getPerson(player).send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
                    player.addPotionEffects(effects);
                    this.cancel();
                    item.remove();
                    return;
                }
                this.location = newLocation;
            }
        }.runTaskTimer(getInstance(), 0L, 10L);
    }
}
