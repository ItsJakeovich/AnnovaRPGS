package net.annova.environment.world.sector0.location;

import net.annova.environment.world.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;

public class InfectedForest implements Location {
    private static final org.bukkit.Location theForest = new org.bukkit.Location(world0, -280, 69, -351);

    @Override
    public void movement(Player player) {
        if (player.getLocation().distanceSquared(theForest) < 15625) {
            if (player.getPotionEffect(PotionEffectType.DARKNESS) != null) {
                getPerson(player).send(getInstance(), "loc-inF-on");
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 120, 0));
            test test = new test();
            player.spawnParticle(Particle.DRAGON_BREATH, player.getLocation(), 30, 3.5, 2.5, 3.5);
        }
    }
}