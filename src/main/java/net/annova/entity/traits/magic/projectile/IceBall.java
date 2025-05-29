package net.annova.entity.traits.magic.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.*;
import org.bukkit.block.Block;

import static net.annova.util.BukkitObjects.chance;
import static org.bukkit.Material.*;

public class IceBall extends ParticleProjectile {
    @Override
    public void onHit(Location location) {
        World world = location.getWorld();
        for (int y = -24; y < 24; y++) {
            for (int z = -24; z < 24; z++) {
                for (int x = -24; x < 24; x++) {
                    if (chance(0.2)) {
                        Location blockLoc = location.clone().add(x, y, z);
                        Block block = blockLoc.getBlock();
                        if (block.getType() == AIR) continue;
                        block.setType(PACKED_ICE);
                        if (y > 12) {
                            blockLoc.add(0, 10, 0);
                            world.spawnParticle(Particle.DUST_COLOR_TRANSITION, blockLoc, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 4.0F));
                            world.playSound(blockLoc, Sound.BLOCK_SNOW_BREAK, 5, 5);
                        }
                    }
                }
            }
        }
    }

    @Override
    public double speed() {
        return 30;
    }

    @Override
    public Packet<?>[] particles(Location location) {
        return new Packet[] {
                new ClientboundLevelParticlesPacket(ParticleTypes.SNOWFLAKE, true, true, location.getX(), location.getY(), location.getZ(), 0.25f, 0.25f, 0.25f, 0, 16),
                new ClientboundLevelParticlesPacket(ParticleTypes.CRIT, true, true, location.getX(), location.getY(), location.getZ(), 0.25f, 0.25f, 0.25f, 0, 16),
        };
    }
}
