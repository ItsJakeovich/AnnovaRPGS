package net.annova.object.magic.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Location;

public class MeteoriteBall extends ParticleProjectile {
    @Override
    public void onHit(Location location) {
        location.createExplosion(6, true, false);
    }

    @Override
    public double speed() {
        return 10;
    }

    @Override
    public Packet<?>[] particles(Location location) {
        return new Packet[] {
                new ClientboundLevelParticlesPacket(ParticleTypes.FLAME, true, true,location.getX(), location.getY(), location.getZ(), 0.6f, 0.6f, 0.6f, 0, 12),
        };
    }

    @Override
    public boolean isGravity() {
        return true;
    }
}
