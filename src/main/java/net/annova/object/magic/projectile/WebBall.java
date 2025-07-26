package net.annova.object.magic.projectile;

import net.annova.object.person.status.StatusType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class WebBall extends ParticleProjectile {
    @Override
    public void onHit(Vec3 position, ServerLevel level) {
        for (Player player : level.getNearbyPlayers(TargetingConditions.DEFAULT, null, new AABB(position, position).inflate(1, 1, 1))) {
            getRPGPerson(player).getStatusManager().addStatus(StatusType.PARALYSIS, +2_000);
        }
    }

    @Override
    public double speed() {
        return 20;
    }

    @Override
    public Packet<?>[] particles(Vec3 position) {
        return new Packet[] {
                new ClientboundLevelParticlesPacket(ParticleTypes.ITEM_COBWEB, true, true, position.x, position.y, position.z, 0.25f, 0.25f, 0.25f, 0, 8),
        };
    }

    @Override
    public boolean isGravity() {
        return true;
    }
}
