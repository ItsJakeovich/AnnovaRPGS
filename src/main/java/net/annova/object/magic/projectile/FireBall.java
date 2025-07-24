package net.annova.object.magic.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class FireBall extends ParticleProjectile {
    @Override
    public void onHit(Vec3 position, ServerLevel level) {
        position.createExplosion(5);
    }

    @Override
    public double speed() {
        return 25;
    }

    @Override
    public Packet<?>[] particles(Vec3 position) {
        return new Packet[] {
                new ClientboundLevelParticlesPacket(ParticleTypes.FLAME, true, true, position.x, position.y, position.z, 0.25f, 0.25f, 0.25f, 0, 8),
                new ClientboundLevelParticlesPacket(ParticleTypes.CLOUD, true, true, position.x, position.y, position.z, 0.1f, 0.1f, 0.1f, 0, 2)
        };
    }

    @Override
    public boolean isGravity() {
        return true;
    }
}
