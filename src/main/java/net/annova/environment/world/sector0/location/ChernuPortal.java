package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.Vec3iSelection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import static net.annova.environment.world.GlobalWorld.CHERNU;
import static net.minecraft.world.level.GameType.SURVIVAL;

public class ChernuPortal extends SectorLocation {
    @Override
    public void movement(RPGPerson person, ServerPlayer player) {
        double distance = this.portalCenter.distanceToSqr(player.position());
        if (player.gameMode() == SURVIVAL && !person.getPerson().getName().equals(CHERNU)) {
            double damage;
            int fireSeconds;
            if (distance < 576) {
                damage = 16;
                fireSeconds = 30;
            } else {
                double koefficient = 576 / distance;
                damage = 6 * koefficient;
                fireSeconds = (int) (30 * koefficient);
            }
            player.igniteForSeconds(fireSeconds);
            person.damage(damage);
        }
    }

    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return EnumSet.noneOf(RPGEntityType.class);
    }

    private final Vec3 portalCenter = new Vec3(-61.5, 64, 433);
    private final Vec3iSelection[] borders = new Vec3iSelection[] {
            new Vec3iSelection(-29, 31, 410, -82, 201, 467)
    };
    @Override
    public Vec3iSelection[] borders() {
        return borders;
    }

    @Override
    public boolean isProtected() {
        return false;
    }
}
