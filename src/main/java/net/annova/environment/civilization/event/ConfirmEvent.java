package net.annova.environment.civilization.event;

import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.build.TownBuild;
import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.event.custom.ConfirmChatActionCE;
import net.annova.event.custom.LeftClickCE;
import net.annova.event.custom.PersonConfirmCE;
import net.annova.lang.AISReplacement;
import net.annova.object.*;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;
import java.util.stream.IntStream;

import static net.annova.lang.MessageObjects.isCyrillicOrLatin;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getAreaChunk;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.CAMP;
import static net.annova.object.item.base.RPGItem.subtractItem;
import static net.annova.util.GlobalTasks.cancelPreviewBuild;
import static net.annova.util.GlobalTasks.previewBuild;

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

                boolean outOfCulture = false;
                boolean isInWater = false;
                Town town = rpg.getSelectedTown();
                Set<AreaChunk> areas = town.getChunks();
                // просто переменные

                int chunkX = position.getX() >> 4, chunkZ = position.getZ() >> 4;
                int chunksX = structure.getMaxX() >> 4, chunksZ = structure.getMaxZ() >> 4;
                loop: for (int x = 0; x < chunksX; x++) {
                    for (int z = 0; z < chunksZ; z++) {
                        AreaChunk area = getAreaChunk(new ChunkPos(chunkX+x, chunkZ+z));
                        if (!areas.contains(area)) {
                            outOfCulture = true;
                            break loop;
                        }
                        if (area.isWater()) {
                            isInWater = true;
                            break loop;
                        }
                    }
                } // проверка на культуру и воду
                if (outOfCulture) {
                    person.send(getInstance(), "build-cancel-culture");
                    cancelPreviewBuild(person);
                } else if (isInWater) {
                    person.send(getInstance(), "build-cancel-water");
                    cancelPreviewBuild(person);
                } else {
                    town.addConstruction(new SimpleConstruction(
                            preview.structure(),
                            preview.direction(),
                            preview.position(),
                            person.getServerPlayer().serverLevel()
                    ));
                    cancelPreviewBuild(person);
                }
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
