package net.annova.environment.world.sector0.location;

import net.annova.environment.world.Location;
import net.annova.environment.world.RPGAdvancement;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.random;

public class DarkForest implements Location {
    private final String[] messageKeys = {
            "s0-forest-msg-0",
            "s0-forest-msg-1",
            "s0-forest-msg-2",
            "s0-forest-msg-3",
            "s0-forest-msg-4",
            "s0-forest-msg-5",
            "s0-forest-msg-6",
            "s0-forest-msg-7",
            "s0-forest-msg-8",
            "s0-forest-msg-9",
    };
    private final Map<Player, Long> messageCooldown = new HashMap<>();
    private final PotionEffect darkness = new PotionEffect(PotionEffectType.DARKNESS, 300, 1);
    @Override
    public void movement(Player player) {
        if (this.isOutside(player.getLocation())) return;

        player.addPotionEffect(darkness);
        player.spawnParticle(Particle.DRAGON_BREATH, player.getLocation(), 30, 3.5, 2.5, 3.5);

        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(player) &&  millis < messageCooldown.get(player)) return;
        messageCooldown.put(player, millis + 1800_000); // 30 min
        getPerson(player).send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
    }

    @Override
    public Set<RPGAdvancement> advancements() {
        return Set.of();
    }

    @Override
    public org.bukkit.Location[] points() {
        return new org.bukkit.Location[] {
                new org.bukkit.Location(world0, -280, 69, -351)
        };
    }

    @Override
    public int squaredRadius() {
        return 15625;
    }
}