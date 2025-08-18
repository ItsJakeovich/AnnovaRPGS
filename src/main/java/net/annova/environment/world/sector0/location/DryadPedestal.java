package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.event.UpdateTask;
import net.annova.environment.world.event.WorldUpdateTimer;
import net.annova.object.person.Vec3iSelection;
import net.annova.object.person.RPGPerson;
import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntityType;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.Keys.POSTAMENT_DRIADASONG;
import static net.annova.object.entity.core.RPGEntityType.*;
import static net.annova.util.BukkitObjects.random;

public class DryadPedestal extends SectorLocation {
    public DryadPedestal() {
        WorldUpdateTimer.addTask(new UpdateTask() {
            @Override
            public void update() {
                for (ServerPlayer player : world0.players()) {
                    RPGPerson person = getRPGPerson(player);
                    if (person.getSectorLocation() instanceof DarkForest) {
                        player.playSound(POSTAMENT_DRIADASONG, 1, 1);
                    }
                }
            }

            @Override
            public int period() {
                return 60;
            }
        });
    }

    private final String[] messageKeys = {
            "s0-dryad-msg-0",
            "s0-dryad-msg-1",
            "s0-dryad-msg-2",
            "s0-dryad-msg-3",
            "s0-dryad-msg-4",
            "s0-dryad-msg-5",
            "s0-dryad-msg-6",
    };
    private final Map<UUID, Long> messageCooldown = new HashMap<>();

    @Override
    public void movement(RPGPerson person, ServerPlayer player) {
        long millis = System.currentTimeMillis();
        UUID id = player.getUUID();
        if (messageCooldown.containsKey(id) && millis < messageCooldown.get(id)) return;
        messageCooldown.put(id, millis + 1800_000); // 30 min

        person.getPerson().send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
    }

    private final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_DRYAD);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    private final Vec3iSelection[] borders = new Vec3iSelection[] {
            new Vec3iSelection(-362, 0, 123, -585, 256, 394)
    };
    @Override
    public Vec3iSelection[] borders() {
        return borders;
    }

    @Override
    public boolean isProtected() {
        return true;
    }
}
