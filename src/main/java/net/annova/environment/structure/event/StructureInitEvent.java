package net.annova.environment.structure.event;

import net.annova.event.custom.StructureCreateCE;
import net.annova.object.Person;
import net.annova.object.SimpleBlock;
import net.annova.object.SimpleConstruction;
import net.annova.object.SimpleStructure;
import net.annova.environment.structure.Structure;
import net.minecraft.network.chat.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

import static net.annova.main.GlobalRPG.*;

public class StructureInitEvent implements Listener {
    @EventHandler // template create event
    public void importSign(StructureCreateCE event) {
        SimpleStructure structure = event.getStructure();
        String[] information = structure.getName().split("-");
        if (!information[0].equals("structure")) return;
        List<SimpleBlock> sBlocks = structure.getBlocks(Person.CardinalDirection.NORTH);
        int basePoint = 0;
        for (int point = 0; point < sBlocks.size(); point++) {
            SimpleBlock block = sBlocks.get(point);
            if (block.getFrontSign() == null && block.getBackSign() == null) continue;

            List<Component> lines = new ArrayList<>(List.of(block.getFrontSign()));
            lines.addAll(List.of(block.getBackSign()));

            List<String[]> definedLines = new ArrayList<>();
            for (Component line : lines) {
                definedLines.add(line.toString().split("-"));
            }

            for (String[] line : definedLines) {
                switch (line[0]) {
                    case "/base" -> basePoint = point;
                }
            }
        }

        String name = information[1];
        SimpleConstruction construction = new SimpleConstruction(name, structure, Person.CardinalDirection.NORTH, null, null);
        construction.setCompleted(true);
        Structure mapStructure = new Structure(construction, basePoint);
        mapStructure.setBase(switch (name.toLowerCase()) {
            //case "altar" -> new Altar();
            //case "firealtar" -> new FireAltar();
            default -> null;
        });

        addStructure(mapStructure);
    }
}