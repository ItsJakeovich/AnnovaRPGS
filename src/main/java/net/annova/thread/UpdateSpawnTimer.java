package net.annova.thread;

import net.annova.environment.world.SectorLocation;
import net.annova.object.LocationCategory;
import net.annova.object.person.Vec3iSelection;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.annova.thread.event.MinuteTickEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.environment.world.GlobalWorld.getWorldSector;
import static net.annova.environment.world.GlobalWorld.getWorldSectors;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.util.BukkitObjects.random;

public class UpdateSpawnTimer implements Listener {
    @EventHandler
    public void run(MinuteTickEvent event) {
        for (ServerLevel level : getWorldSectors().keySet()) {
            for (SectorLocation location : getWorldSector(level).zones()) {
                Vec3iSelection[] borders = location.borders();
                for (RPGEntityType entity : location.entitiesToSpawn()) {
                    if (entity.isBoss() && location.getBoss() != null) continue;
                    RPGEntity living = entity.tryNaturalSpawn(borders[random.nextInt(borders.length)].getRandomPoint(), level, 10, entity.isWaterSpawn());

                    if (entity.isBoss() && living != null) location.setBoss(living);
                }
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            RPGPerson person = getRPGPerson(player);
            if (person.getSectorLocation() != null) continue;
            ServerPlayer serverPlayer = person.getServerPlayer();
            ServerLevel level = serverPlayer.serverLevel();
            if (!getWorldSectors().containsKey(level)) continue;

            RPGEntityType[] entities = getEntitiesByCategory(LocationCategory.EVERYWHERE).toArray(new RPGEntityType[0]);

            // Entity spawn
            Vec3 position = serverPlayer.position();
            if (position.y() > 200) continue;
            for (int i = random.nextInt(3)+1; i != 0; i--) { // от 1 до 3
                RPGEntityType entity = entities[random.nextInt(entities.length)];
                if (entity.getCondition().canSpawn(position, level)) entity.tryNaturalSpawn(position, level, 16, entity.isWaterSpawn());
            }
        }
    }
}
