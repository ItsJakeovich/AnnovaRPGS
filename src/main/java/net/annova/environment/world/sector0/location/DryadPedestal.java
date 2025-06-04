package net.annova.environment.world.sector0.location;

import net.annova.environment.world.Location;
import net.annova.environment.world.advancement.RPGAdvancement;
import org.bukkit.entity.Player;

import java.util.Set;

import static net.annova.environment.world.sector0.SuruWahare.world0;

public class DryadPedestal implements Location {
    private static final org.bukkit.Location theForest = new org.bukkit.Location(world0, -466, 71, 242);

    @Override
    public void movement(Player player) {

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
