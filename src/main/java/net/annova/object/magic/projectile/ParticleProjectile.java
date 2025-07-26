package net.annova.object.magic.projectile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.sendPackets;

public abstract class ParticleProjectile {
    public abstract void onHit(Vec3 position, ServerLevel level);
    public abstract double speed();
    public abstract Packet<?>[] particles(Vec3 position);
    public abstract boolean isGravity();

    public void create(LivingEntity entity, Entity target) {
        this.create(entity.getEyePosition(), target.getEyePosition(), entity.level().getMinecraftWorld());
    }
    public void create(LivingEntity entity, Vec3 to) {
        this.create(entity.getEyePosition(), to, entity.level().getMinecraftWorld());
    }
    public void create(Vec3 from, Vec3 to, ServerLevel level) {
        Vec3 direction = to.add(0, 1, 0).subtract(from).normalize();

        float pitch, yaw = 0;

        double x = direction.x, z = direction.z;
        if (x == 0 && z == 0) pitch = direction.y > 0 ? -90 : 90;
        else {
            yaw = (float)Math.toDegrees((Math.atan2(-x, z) + (Math.PI * 2D)) % (Math.PI * 2D));
            pitch = (float)Math.toDegrees(Math.atan(-direction.y / Math.sqrt(x*x + z*z)));
        }

        this.tick(from.add(direction.scale(2.5)), yaw, pitch, direction.y, level);
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

                if (isCollision(level, position, direction, speed)) {
                    ParticleProjectile.this.onHit(position, level);
                    this.cancel();
                    return;
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

    public static boolean isCollision(ServerLevel level, Vec3 position, Vec3 direction, double maxDistance) {
        Vec3 dir = direction.scale(maxDistance);
        Vec3 endPos = position.add(dir.x, dir.y, dir.z);
        HitResult nmsHitResult = level.clip(new ClipContext(position, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, CollisionContext.empty()));
        if (nmsHitResult.getType() != HitResult.Type.MISS) return true;

        Collection<net.minecraft.world.entity.Entity> entities = level.getEntities((net.minecraft.world.entity.Entity) null,
                new AABB(position, position).expandTowards(dir).inflate(0.25)
        );
        return !entities.isEmpty(); // true если попало.
    }
}
