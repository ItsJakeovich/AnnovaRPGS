package net.annova.environment.dungeon.type.snow;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.environment.dungeon.type.DungeonBase;
import net.annova.object.SimpleStructure;
import org.bukkit.inventory.Inventory;

import static net.annova.main.GlobalRPG.getDTemplate;

public class Snow implements DungeonBase {
    private final DungeonTemplate template;
    private Dungeon dungeon;

    public Snow() {
        template = getDTemplate(1).clone();
    }

    @Override
    public DungeonTemplate getTemplate() {
        return template;
    }

    @Override
    public void start(Dungeon dungeon) {
        this.dungeon = dungeon;

        SimpleStructure structure = template.getStructure(0);
    }

    @Override
    public Inventory merchant() {
        return null;
    }
}
