package net.annova.object.magic.projectile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_21_R4.CraftFluidCollisionMode;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.util.CraftLocation;
import org.bukkit.craftbukkit.v1_21_R4.util.CraftRayTraceResult;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.sendPackets;

public abstract class ParticleProjectile {
    public abstract void onHit(Vec3 position, ServerLevel level);
    public abstract double speed();
    public abstract Packet<?>[] particles(Vec3 position);
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
        this.tick(new Vec3(location.getX(), location.getY(), location.getZ()),
                location.getYaw(), location.getPitch(), location.getDirection().getY(),
                ((CraftWorld) location.getWorld()).getHandle()
        );
    }

    private final double airResistance = 0.5 * 1.225 * 0.47 * Math.PI * Math.pow(0.022, 2) / 20;
    private final double gravity = 0.025 * 9.81 / 20;
    public void tick(Vec3 startPosition, float yaw, float pitch, double startVertical, ServerLevel level) {
        new BukkitRunnable() {
            private int ticks = 200; // 10s
            private Vec3 position = startPosition;
            private double speed = ParticleProjectile.this.speed() / 20;
            private double vertical = startVertical;

            @Override
            public void run() {
                sendPackets(level, ParticleProjectile.this.particles(position));

                Vec3 direction = Vec3.directionFromRotation(yaw, pitch);
                direction = new Vec3(direction.x, vertical, direction.z).normalize();
                World world;
                RayTraceResult result = world.rayTrace(position, direction, speed, FluidCollisionMode.NEVER,
                        false, 1, null);

                if (result != null) {
                    Material type = result.getHitBlock() == null ? Material.AIR : result.getHitBlock().getType();
                    if ((!type.equals(Material.AIR) && !type.equals(Material.BARRIER) && !type.equals(Material.WATER))
                            || result.getHitEntity() != null) {
                        ParticleProjectile.this.onHit(position, level);
                        this.cancel();
                        return;
                    }
                }
                Vector q;

                position = position.add(direction.scale(speed));

                if (Mth.floor(position.x()) < level.getMinY()) {
                    this.cancel();
                    return;
                }

                this.speed = Math.max(speed - airResistance * speed*speed, 0);
                if (ParticleProjectile.this.isGravity()) vertical -= gravity;
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

    public void rayTrace(ServerLevel level, World world) {
        Vector dir = direction.clone().normalize().multiply(maxDistance);
        Vec3 startPos = CraftLocation.toVec3D(start);
        Vec3 endPos = startPos.add(dir.getX(), dir.getY(), dir.getZ());
        HitResult nmsHitResult = level.clip(new ClipContext(startPos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, CollisionContext.empty()));
        Vec3 startVec = null;
        double blockHitDistance = maxDistance;
        if (blockHit != null) {
            startVec = start.toVector();
            blockHitDistance = startVec.distance(blockHit.getHitPosition());
        }

        RayTraceResult entityHit = world.rayTraceEntities(start, direction, blockHitDistance, raySize, filter);
        if (blockHit == null) {
            return entityHit;
        } else if (entityHit == null) {
            return blockHit;
        } else {
            double entityHitDistanceSquared = startVec.distanceSquared(entityHit.getHitPosition());
            return entityHitDistanceSquared < blockHitDistance * blockHitDistance ? entityHit : blockHit;
        }
    }
}
