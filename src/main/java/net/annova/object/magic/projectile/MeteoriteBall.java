package net.annova.object.magic.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;

public class MeteoriteBall extends ParticleProjectile {
    @Override
    public void onHit(Vec3 position, ServerLevel level) {
        level.explode(null, position.x, position.y, position.z, 6, true, Level.ExplosionInteraction.NONE);
    }

    @Override
    public double speed() {
        return 10;
    }

    @Override
    public Packet<?>[] particles(Vec3 position) {
        return new Packet[] {
                new ClientboundLevelParticlesPacket(ParticleTypes.FLAME, true, true, position.x, position.y, position.z, 0.6f, 0.6f, 0.6f, 0, 12),
        };
    }

    @Override
    public boolean isGravity() {
        return true;
    }
}
