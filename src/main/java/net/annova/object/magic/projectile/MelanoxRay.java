package net.annova.object.magic.projectile;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MelanoxRay extends ParticleProjectile {
    private final MobEffectInstance POISON = new MobEffectInstance(MobEffects.POISON, 40, 3);
    private final MobEffectInstance SLOWNESS = new MobEffectInstance(MobEffects.SLOWNESS, 120, 0);
    @Override
    public void onHit(Vec3 position, ServerLevel level) {
        for (Entity entity : level.getEntities((net.minecraft.world.entity.Entity) null, new AABB(position, position).inflate(1, 1, 1))) {
            if (entity instanceof LivingEntity living) {
                living.addEffect(POISON);
                living.addEffect(SLOWNESS);
            }
        }
    }

    @Override
    public double speed() {
        return 15;
    }

    @Override
    public Packet<?>[] particles(Vec3 position) {
        return new Packet[] {
                new ClientboundLevelParticlesPacket(
                        new DustParticleOptions(0x90EE90, 1f),
                        true,
                        true,
                        position.x, position.y, position.z,
                        0.0f, 0.0f, 0.0f,
                        0.0f,
                        1
                )
        };
    }

    @Override
    public boolean isGravity() {
        return false;
    }
}
