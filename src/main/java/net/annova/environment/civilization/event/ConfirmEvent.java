package net.annova.environment.civilization.event;

import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.build.TownBuild;
import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.event.custom.ConfirmChatActionCE;
import net.annova.event.custom.PersonConfirmCE;
import net.annova.object.*;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.Set;

import static net.annova.lang.MessageObjects.isCyrillicOrLatin;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getAreaChunk;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.CAMP;
import static net.annova.util.GlobalTasks.cancelPreviewBuild;

public class ConfirmEvent implements Listener {
    @EventHandler
    public void chat(ConfirmChatActionCE event) {
        Person person = event.getPerson();
        String command = event.getCommand();
        switch (person.getConfirm().getConfirmType()) {
            case "set-camp" -> {
                if (command.length() < 4 || command.length() > 20 || !isCyrillicOrLatin(command)) {
                    person.send(getInstance(), "camp-set-cancel-name");
                    return;
                }
                person.send(getInstance(), "camp-set-name", "{name}", command);
                person.getConfirm().addData(new SimpleObject("name", event.getCommand()));
            }
        }
    }

    @EventHandler
    public void confirm(PersonConfirmCE event) {
        Person person = event.getPerson();

        Confirm confirm = person.getConfirm();
        System.out.println(confirm.getConfirmType());
        switch (confirm.getConfirmType()) {
            case "build" -> {
                System.out.println("build1");
                RPGPerson rpg = getRPGPerson(person);
                ServerPlayer serverPlayer = person.getServerPlayer();

                Preview preview = person.getPreview();
                Vec3i position = preview.position();
                ServerLevel level = preview.level();
                SimpleStructure structure = preview.structure();

                Town town = rpg.getSelectedTown();
                Set<AreaChunk> areas = town.getChunks();
                int chunkX = position.getX() >> 4, chunkZ = position.getZ() >> 4;
                int chunksX = structure.getMaxX() >> 4, chunksZ = structure.getMaxZ() >> 4;
                for (int x = 0; x < chunksX; x++) {
                    for (int z = 0; z < chunksZ; z++) {
                        AreaChunk area = getAreaChunk(new ChunkPos(chunkX + x, chunkZ + z));
                        if (!areas.contains(area)) {
                            person.send(getInstance(), "build-cancel-culture");
                            cancelPreviewBuild(person);
                            return;
                        }
                        if (area.isWater()) {
                            person.send(getInstance(), "build-cancel-water");
                            cancelPreviewBuild(person);
                            return;
                        }
                    }
                }

                Collection<TownBuild> townBuilds = town.getAllStructures();
                for (Vec3i point : new Vec3i[]{
                        position,
                        position.offset(structure.getMaxX(), 0, 0),
                        position.offset(0, structure.getMaxY(), 0),
                        position.offset(0, 0, structure.getMaxZ()),
                        position.offset(structure.getMaxX(), structure.getMaxY(), 0),
                        position.offset(0, structure.getMaxY(), structure.getMaxZ()),
                        position.offset(structure.getMaxX(), 0, structure.getMaxZ()),
                        position.offset(structure.getMaxX(), structure.getMaxY(), structure.getMaxZ())
                }) {
                    int pointX = point.getX(), pointY = point.getY(), pointZ = point.getZ();
                    for (TownBuild build : townBuilds) {
                        SimpleStructure struct = build.getConstruction().getStructure();
                        Vec3i min = build.getConstruction().getBuildPoint(), max = min.offset(struct.getMaxX(), struct.getMaxY(), struct.getMaxZ());

                        if (pointX >= min.getX() && pointX <= max.getX()
                                && pointY >= min.getY() && pointY <= max.getY()
                                && pointZ >= min.getZ() && pointZ <= max.getZ()) {
                            Конфликтует с другим строением -> сообщение + return;
                        }
                    }
                }

                Vec3i positionMax = position.offset(structure.getMaxX(), structure.getMaxY(), structure.getMaxZ());
                for (TownBuild builds : town.getAllStructures()) {
                    SimpleConstruction construct = builds.getConstruction();
                    SimpleStructure struct = construct.getStructure();
                    Vec3i min = construct.getBuildPoint(), max = min.offset(struct.getMaxX(), struct.getMaxY(), struct.getMaxZ());

                    if ((position.getX() >= min.getX() || positionMax.getX() <= max.getX()) &&
                            (position.getY() >= min.getY() || positionMax.getY() <= max.getY()) &&
                            (position.getZ() >= min.getZ() || positionMax.getZ() <= max.getZ())) {

                    }
                }

                town.addConstruction(new SimpleConstruction(
                        preview.structure(),
                        preview.direction(),
                        preview.position(),
                        person.getServerPlayer().serverLevel()
                ));
                cancelPreviewBuild(person);
            }
            case "set-camp" -> {
                String name = (String) confirm.getData().getOrDefault("name", "");
                if (name.isEmpty()) {
                    person.send(getInstance(), "camp-set-cancel-blank");
                    return;
                }

                RPGPerson rpg = getRPGPerson(person);
                ServerPlayer player = rpg.getServerPlayer();

                AreaChunk chunk = new AreaChunk(player.serverLevel(), player.chunkPosition());
                if (chunk.isWater()) {
                    person.send(getInstance(), "camp-set-cancel-water");
                    person.resetConfirm();
                    return;
                }

                if (!CAMP.removeItem(person.getServerPlayer())) {
                    person.send(getInstance(), "camp-set-not-item");
                    return;
                }

                person.send(getInstance(), "camp-set", "{name}", name);
                Town town = new Town(name, chunk, rpg);
                rpg.setTown(town);
            }
            default -> {
                return;
            }
        }
        person.resetConfirm();
    }
}
