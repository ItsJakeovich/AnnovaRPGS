package net.annova.object.magic.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Location;

public class FireBall extends ParticleProjectile {
    @Override
    public void onHit(Location location) {
        location.createExplosion(5);
    }

    @Override
    public double speed() {
        return 25;
    }

    @Override
    public Packet<?>[] particles(Location location) {
        return new Packet[] {
                new ClientboundLevelParticlesPacket(ParticleTypes.FLAME, true, true, location.getX(), location.getY(), location.getZ(), 0.25f, 0.25f, 0.25f, 0, 8),
                new ClientboundLevelParticlesPacket(ParticleTypes.CLOUD, true, true, location.getX(), location.getY(), location.getZ(), 0.1f, 0.1f, 0.1f, 0, 2)
        };
    }

    @Override
    public boolean isGravity() {
        return true;
    }
}
