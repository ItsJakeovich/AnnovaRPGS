package net.annova.object.magic.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static net.annova.util.BukkitObjects.chance;
import static net.minecraft.sounds.SoundEvents.SNOW_BREAK;
import static net.minecraft.world.level.block.Blocks.PACKED_ICE;

public class IceBall extends ParticleProjectile {
    private static final DustColorTransitionOptions options = new DustColorTransitionOptions(255, 65535, 4.0f);
    @Override
    public void onHit(Vec3 position, ServerLevel level) {
        for (int y = -24; y < 24; y++) {
            for (int z = -24; z < 24; z++) {
                for (int x = -24; x < 24; x++) {
                    if (chance(0.2)) {
                        BlockPos pos = BlockPos.containing(position.add(x, y, z));
                        BlockState state = level.getBlockState(pos);
                        if (state.isAir()) continue;
                        level.setBlock(pos, PACKED_ICE.defaultBlockState(), 1);
                        if (y > 12) {
                            level.sendParticles(options, pos.getX(), pos.getY()+10, pos.getZ(), 1, 0, 0, 0, 0);
                            level.playLocalSound(pos.above(10), SNOW_BREAK, SoundSource.BLOCKS, 1, 1, true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public double speed() {
        return 18;
    }

    @Override
    public Packet<?>[] particles(Vec3 position) {
        return new Packet[] {
                new ClientboundLevelParticlesPacket(ParticleTypes.SNOWFLAKE, true, true, position.x, position.y, position.z, 0.25f, 0.25f, 0.25f, 0, 16),
                new ClientboundLevelParticlesPacket(ParticleTypes.CRIT, true, true, position.x, position.y, position.z, 0.25f, 0.25f, 0.25f, 0, 16),
        };
    }

    @Override
    public boolean isGravity() {
        return true;
    }
}
