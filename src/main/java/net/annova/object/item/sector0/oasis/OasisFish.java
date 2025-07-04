package net.annova.object.item.sector0.oasis;

import net.annova.event.custom.DropItemFoundBlockCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.annova.object.item.base.ItemType.OASIS_FISH;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.random;

public class OasisFish extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(17f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
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
    public void dropItem(DropItemFoundBlockCE event) {
        if (!OASIS_FISH.equals(event.getItem())) return;
        if (!event.getFoundedBlock().getType().equals(Material.WATER)) return;
        Player player = event.getPlayer();

        long now = System.currentTimeMillis();
        if (cooldown.containsKey(player)) if (now < cooldown.get(player)) return;

        getPerson(player).send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
        cooldown.put(player, now + 14_400_000); // 4 hours
        player.addPotionEffects(effects);
    }
}
