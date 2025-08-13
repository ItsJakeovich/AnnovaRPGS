package net.annova.environment.civilization.event;

import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.build.TownBuild;
import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.event.custom.ConfirmChatActionCE;
import net.annova.event.custom.PersonConfirmCE;
import net.annova.lang.AISReplacement;
import net.annova.lang.AISString;
import net.annova.object.*;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
                int chunksX = (structure.getMaxX()+15) >> 4, chunksZ = (structure.getMaxZ()+15) >> 4;
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
                } // Проверку на основание нужно делать отдельно, так как размер здания может быть меньше чанка.

                double maxSupportThreshold = 0;
                double realSupportThreshold = 0;

                int startY = position.getY()-1, minY = level.getMinY();
                int maxHeigth = startY-minY;
                double modifier = 1.0/maxHeigth;

                int xStep = (int) Math.max(structure.getMaxX() * 0.1, 1),
                        zStep = (int) Math.max(structure.getMaxZ()*0.1, 1);
                int maxX = position.getX()+structure.getMaxX(), maxZ = position.getZ()+structure.getMaxZ();
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(0, 0, 0);
                for (int x = position.getX(); x < maxX; x+=xStep) {
                    for (int z = position.getZ(); z < maxZ; z +=zStep) {
                        pos.set(x, startY, z);
                        for (int cY = maxHeigth; cY >= 0; cY--, pos.setY(pos.getY()-1)) {
                            double support = cY * modifier;
                            maxSupportThreshold += support;
                            if (level.getBlockState(pos).isAir()) continue;
                            realSupportThreshold += support;
                        }
                    }
                }
                double supportPercent = realSupportThreshold / maxSupportThreshold;
                if (supportPercent < 0.75) {
                    person.send(person.getAISString(getInstance(), "build-cancel-support")
                            .replaceTag("{real}", String.valueOf(realSupportThreshold))
                            .replaceTag("{max}", String.valueOf(maxSupportThreshold))
                            .replaceTag("{percent}", (int)(supportPercent*100) + "%")
                    );
                    cancelPreviewBuild(person);
                    return;
                }

                for (TownBuild build : town.getAllStructures()) {
                    SimpleConstruction construction = build.getConstruction();
                    if (construction.getStructure().intersect(position, construction)) {
                        person.send(getInstance(), "build-cancel-intersects");
                        cancelPreviewBuild(person);
                        return;
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
