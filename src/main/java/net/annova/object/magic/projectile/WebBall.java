package net.annova.object.magic.projectile;

import net.annova.object.person.status.StatusType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class WebBall extends ParticleProjectile {
    @Override
    public void onHit(Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
            if (entity instanceof Player player) {
                getRPGPerson(player).getStatusManager().addStatus(StatusType.PARALYSIS);
            }
        }
    }

    @Override
    public double speed() {
        return 20;
    }

    @Override
    public Packet<?>[] particles(Location location) {
        return new Packet[] {
                new ClientboundLevelParticlesPacket(ParticleTypes.ITEM_COBWEB, true, true, location.getX(), location.getY(), location.getZ(), 0.25f, 0.25f, 0.25f, 0, 8),
        };
    }
}
