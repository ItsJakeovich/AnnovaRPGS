package net.annova.object.magic.projectile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.*;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.sendPackets;

public abstract class ParticleProjectile {
    public abstract void onHit(Location location);
    public abstract double speed();
    public abstract Packet<?>[] particles(Location location);
    public abstract boolean isGravity();

    public void create(LivingEntity entity, Entity target) {
        this.create(((Creature) entity).getEyeLocation(), target.getLocation());
    }
    public void create(Location from, Location to) {
        Vector direction = to.add(0,1, 0).toVector().subtract(from.toVector()).normalize();
        from.setDirection(direction);
        from.add(direction.clone().multiply(2.5));
        this.create(from);
    }
    public void create(Location location) {
        this.tick(location);
    }

    private final double airResistance = 0.5 * 1.225 * 0.47 * Math.PI * Math.pow(0.022, 2) / 20;
    private final double gravity = 0.025 * 9.81 / 20;
    public void tick(Location startLocation) {
        new BukkitRunnable() {
            private int ticks = 400; // 20s
            private final Location location = startLocation.clone();
            private final World world = location.getWorld();
            private double speed = ParticleProjectile.this.speed() / 20;
            private double vertical = location.getDirection().getY();

            @Override
            public void run() {
                sendPackets(world, ParticleProjectile.this.particles(location));

                Vector direction = location.getDirection().clone().setY(vertical).normalize();
                RayTraceResult result = world.rayTrace(location, direction, speed, FluidCollisionMode.NEVER,
                        false, 1, null);

                if (result != null) {
                    Material type = result.getHitBlock() == null ? Material.AIR : result.getHitBlock().getType();
                    if ((!type.equals(Material.AIR) && !type.equals(Material.BARRIER) && !type.equals(Material.WATER))
                            || result.getHitEntity() != null) {
                        ParticleProjectile.this.onHit(location);
                        this.cancel();
                        return;
                    }
                }

                location.add(direction.multiply(speed));

                if (location.getBlockY() < world.getMinHeight()) {
                    this.cancel();
                    return;
                }

                this.speed = Math.max(speed - airResistance * speed*speed, 0) / 20;
                if (ParticleProjectile.this.isGravity()) this.vertical -= gravity;
                else {
                    if (ticks == 0) {
                        this.cancel();
                        return;
                    }
                    this.ticks--;
                }
            }
        }.runTaskTimer(getInstance(), 0L, 1);
    }
}
