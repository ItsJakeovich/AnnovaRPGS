package net.annova.event;

import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.event.custom.StructureCreateCE;
import net.annova.object.*;
import net.annova.object.animation.Animation;
import net.annova.object.animation.Frame;
import net.annova.object.person.Person;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

import static net.annova.main.GlobalRPG.addDTemplate;
import static net.annova.util.AMaterial.getMaterial;

public class DungeonInitEvent implements Listener {
    @EventHandler
    public void importSign(StructureCreateCE event) {
        SimpleStructure structure = event.getStructure();
        if (!structure.getName().split("-")[0].equals("dungeon")) return;
        DungeonTemplate dungeonTemplate = new DungeonTemplate();
        List<SimpleBlock> sBlocks = structure.getBlocks(Person.CardinalDirection.NORTH);
        Map<Integer, BlockState> blocks = new HashMap<>();
        Map<Integer, List<String[]>> summons = new HashMap<>();

        int locIndex = 0;
        for (int point = 0; point < sBlocks.size(); point++) {
            SimpleBlock block = sBlocks.get(point);
            if (block.getFrontSign() == null && block.getBackSign() == null) continue;

            List<String[]> definedLines = new ArrayList<>();
            for (Component line : block.getFrontSign()) {
                definedLines.add(line.getString().split("-"));
            }
            for (Component line : block.getBackSign()) {
                definedLines.add(line.getString().split("-"));
            }
            Material material = Material.AIR;
            int id = 0;
            String type = "";
            String[] summon = new String[0];

            for (String[] line : definedLines) {
                switch (line[0]) {
                    case "/spawn" -> {
                        dungeonTemplate.setSpawn(point);
                        dungeonTemplate.setId(Integer.parseInt(line[2]));
                    }
                    case "/loc" -> locIndex = Integer.parseInt(line[1]);
                    case "/b" -> blocks.put(point, getMaterial(line[1]));
                    case "/a" -> {
                        type = line[1];
                        id = Integer.parseInt(line[2]);
                        dungeonTemplate.addAnimation(new Animation(id, type));
                    }
                    case "/type" -> {
                        if (line[1].equals("closed")) {
                            dungeonTemplate.getAnimation(type, id).setClosed(true);
                        }
                    }
                    case "/frame" -> {
                        Frame frame = new Frame(Integer.parseInt(line[1]));
                        Animation animation = dungeonTemplate.getAnimation(type, id);
                        animation.addFrame(frame).addBlock(point, blocks.remove(point));
                    }
                    case "/s" -> summon = new String[] {
                            line[1],
                            line.length > 2 ? line[2] : "1",
                            String.valueOf(point)
                    };
                    case "/n" -> {
                        int key = Integer.parseInt(line[1]);
                        summons.putIfAbsent(key, new ArrayList<>());
                        int quantity = Integer.parseInt(summon[1]);

                        for (int i = 0; i < quantity; i++) summons.get(key).add(summon);
                    }
                    case "/loot" -> dungeonTemplate.addLootChest(point, Integer.parseInt(line[1]));
                    case "/merchant" -> dungeonTemplate.setMerchantPoint(point);
                }
            }
        }
        int id = dungeonTemplate.getId();

        for (int key : summons.keySet()) {
            for (String[] summon : summons.get(key)) {
                RPGEntityType type = RPGEntityType.valueOf(summon[0].toUpperCase());
                RPGEntity entity = type.getEntity();

                entity.setPoint(Integer.parseInt(summon[2]));
                dungeonTemplate.addEntity(key, entity);
            }
        }

        dungeonTemplate.setStructure(locIndex, structure);
        addDTemplate(dungeonTemplate);
    }
}
