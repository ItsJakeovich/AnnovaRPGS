package net.annova.environment.world.sector0.location;

import net.annova.environment.world.Location;
import net.annova.environment.world.advancement.RPGAdvancement;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

import static net.annova.environment.world.sector0.SuruWahare.world0;

public class Meteorite implements Location {
    private static final org.bukkit.Location theMeteorite = new org.bukkit.Location(world0, 730, 51, -191);

    @Override
    public void movement(Player player) {
        if (player.getLocation().distanceSquared(theMeteorite) < 4096) {
            player.setFireTicks(120);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 120, 0));

            player.spawnParticle(Particle.DRAGON_BREATH, player.getLocation(), 30, 3.5, 2.5, 3.5);
        }
    }

    @Override
    public Set<RPGAdvancement> advancements() {
        return Set.of();
    }

    @Override
    public org.bukkit.Location[] points() {
        return new org.bukkit.Location[0];
    }

    @Override
    public int squaredRadius() {
        return 0;
    }
}