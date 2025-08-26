package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.event.UpdateTask;
import net.annova.environment.world.event.WorldUpdateTimer;
import net.annova.object.LocationCategory;
import net.annova.object.person.Vec3iSelection;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.entity.core.RPGEntityType.*;
import static net.annova.object.person.equipment.ArmorSet.CHITIN;
import static net.annova.util.BukkitObjects.random;
import static net.minecraft.world.level.GameType.SURVIVAL;

public class Meteorite extends SectorLocation {
    public Meteorite() {
        WorldUpdateTimer.addTask(new UpdateTask() {
            private static final DustColorTransitionOptions options = new DustColorTransitionOptions(65535, 8421504, 3.0f);

            @Override
            public void update() {
                world0.sendParticles(ParticleTypes.FLAME, meteoriteCenter.x, meteoriteCenter.y, meteoriteCenter.z, 400, 28, 20, 28, 0);
            }

            @Override
            public int period() {
                return 20;
            }
        });
    }

    private final String[] messageKeys = {
            "s0-meteorite-msg-0",
            "s0-meteorite-msg-1",
            "s0-meteorite-msg-2",
            "s0-meteorite-msg-3",
            "s0-meteorite-msg-4",
            "s0-meteorite-msg-5",
            "s0-meteorite-msg-6",
            "s0-meteorite-msg-7",
            "s0-meteorite-msg-8",
    };
    private final Map<UUID, Long> messageCooldown = new HashMap<>();

    @Override
    public void movement(RPGPerson person, ServerPlayer player) {
        /*boolean isDamageable = true;
        if (CHITIN == person.getArmorSet()) isDamageable = false;
        if (isDamageable) {
            double distance = this.meteoriteCenter.distanceToSqr(player.position());
            if (player.gameMode() == SURVIVAL) {
                double damage;
                int fireSeconds;
                if (distance < 576) {
                    damage = 24;
                    fireSeconds = 30;
                } else {
                    double koefficient = 576 / distance;
                    damage = 8 * koefficient;
                    fireSeconds = (int) (30 * koefficient);
                }
                player.igniteForSeconds(fireSeconds);
                person.damage(damage);
            }
        }*/

        UUID uuid = player.getUUID();
        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(uuid) && millis < messageCooldown.get(uuid)) return;
        messageCooldown.put(uuid, millis + 1800_000); // 30 min

        person.getPerson().send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
    }

    private final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_METEORITE, METEORITE_GOLEM);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    private final Vec3 meteoriteCenter = new Vec3(726.5, 40.5, -190.5);
    private final Vec3iSelection[] borders = new Vec3iSelection[] {
            new Vec3iSelection(670, 30, -132, 767, 1000, -228)
    };
    @Override
    public Vec3iSelection[] borders() {
        return borders;
    }

    @Override
    public boolean isProtected() {
        return false;
    }
}