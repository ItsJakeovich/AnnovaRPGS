package net.annova.object.magic.projectile;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MelanoxRay extends ParticleProjectile {
    private final PotionEffect poison = new PotionEffect(PotionEffectType.POISON, 40, 3);
    private final PotionEffect slowness = new PotionEffect(PotionEffectType.SLOWNESS, 120, 0);
    @Override
    public void onHit(Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
            if (entity instanceof LivingEntity living) {
                living.addPotionEffect(poison);
                living.addPotionEffect(slowness);
            }
        }
    }

    @Override
    public double speed() {
        return 15;
    }

    @Override
    public Packet<?>[] particles(Location location) {
        return new Packet[] {
                new ClientboundLevelParticlesPacket(
                        new DustParticleOptions(0x90EE90, 1f),
                        true,
                        true,
                        location.getX(), location.getY(), location.getZ(),
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
