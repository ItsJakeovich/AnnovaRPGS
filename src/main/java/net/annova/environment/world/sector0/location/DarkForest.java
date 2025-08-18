package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.object.LocationCategory;
import net.annova.object.person.Vec3iSelection;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusType;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.CraftParticle;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.Keys.*;
import static net.annova.object.entity.core.RPGEntityType.*;
import static net.annova.util.BukkitObjects.random;
import static net.minecraft.world.effect.MobEffects.DARKNESS;

public class DarkForest extends SectorLocation {
    private final Map<UUID, Long> forestMSGCooldown = new HashMap<>();
    private final String[] MSGKeys = {
            "s0-forest-msg-0",
            "s0-forest-msg-1",
            "s0-forest-msg-2",
            "s0-forest-msg-3",
            "s0-forest-msg-4",
            "s0-forest-msg-5",
            "s0-forest-msg-6",
            "s0-forest-msg-7",
            "s0-forest-msg-8",
            "s0-forest-msg-9",
    };
    private final Map<UUID, Long> soundCooldown = new HashMap<>();
    private final SoundEvent[] soundKeys = {
            FOREST_AMBIENT_1,
            FOREST_AMBIENT_2,
            FOREST_AMBIENT_3
    };
    private final MobEffectInstance darkness = new MobEffectInstance(DARKNESS, 300, 1);

    @Override
    public void movement(RPGPerson person, ServerPlayer player) {
        if (player.gameMode().equals(GameType.SURVIVAL)) {
            Vec3 position = player.position();
            player.connection.send(new ClientboundLevelParticlesPacket(CraftParticle.createParticleParam(
                    Particle.DRAGON_BREATH, null), false, false,
                    position.x, position.y+0.1, position.z,
                    15, 0, 15, 0, 100)
            );
            if (!person.getStatusManager().hasStatus(StatusType.ABSORB_NIGHT_BUFF)) player.addEffect(darkness);
        }

        UUID uuid = player.getUUID();
        long millis = System.currentTimeMillis();
        if (!soundCooldown.containsKey(uuid) || millis > soundCooldown.get(uuid)) {
            soundCooldown.put(uuid, millis + 300_000);
            player.playSound(soundKeys[random.nextInt(soundKeys.length)], 1, 1);
        }
        if (forestMSGCooldown.containsKey(uuid) &&  millis < forestMSGCooldown.get(uuid)) return;
        forestMSGCooldown.put(uuid, millis + 1800_000); // 30 min
        person.getPerson().send(getInstance(), MSGKeys[random.nextInt(MSGKeys.length)]);
    }

    private static final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_FOREST);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    private static final Vec3iSelection[] borders = new Vec3iSelection[] {
        new Vec3iSelection(-442, 0, -190, -128, 256, -511)
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