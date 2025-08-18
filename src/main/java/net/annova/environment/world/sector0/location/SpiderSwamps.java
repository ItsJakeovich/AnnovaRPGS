package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.event.UpdateTask;
import net.annova.environment.world.event.WorldUpdateTimer;
import net.annova.object.LocationCategory;
import net.annova.object.person.Vec3iSelection;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.entity.core.RPGEntityType.MELANOX;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class SpiderSwamps extends SectorLocation {
    private final String[] messageKeys = {
            "s0-swamps-msg-0",
            "s0-swamps-msg-1",
            "s0-swamps-msg-2",
            "s0-swamps-msg-3",
            "s0-swamps-msg-4",
            "s0-swamps-msg-5",
            "s0-swamps-msg-6",
            "s0-swamps-msg-7",
    };
    private final Map<UUID, Long> messageCooldown = new HashMap<>();

    @Override
    public void movement(RPGPerson person, ServerPlayer player) {
        long millis = System.currentTimeMillis();
        UUID uuid = player.getUUID();
        if (messageCooldown.containsKey(uuid) &&  millis < messageCooldown.get(uuid)) return;
        messageCooldown.put(uuid, millis + 1800_000); // 30 min

        person.getPerson().send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
        if (player.equipment.get(EquipmentSlot.HEAD) == ItemStack.EMPTY) person.damage(2.0f);
    }

    public SpiderSwamps() {
        WorldUpdateTimer.addTask(new UpdateTask() {
            @Override
            public void update() {
                if (SpiderSwamps.this.getBoss() == null && chance(MELANOX.getChance())) {
                    RPGEntity entity = MELANOX.getEntity();
                    entity.create(new Vec3(-702.5, 9, 239.5), world0);
                    SpiderSwamps.this.setBoss(entity);
                }
            }

            @Override
            public int period() {
                return 300; // 30 секунд
            }
        });
    }

    private static final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_SWAMPS, MELANOX);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    private static final Vec3iSelection[] borders = new Vec3iSelection[] {
            new Vec3iSelection(-785, 80, 126, -743, 48, 191),
            new Vec3iSelection(-771, 62, 192, -684, 2, 254)
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
