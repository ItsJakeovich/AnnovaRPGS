package net.annova.environment.dungeon.type;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import org.bukkit.inventory.Inventory;

public interface DungeonBase {
    DungeonTemplate getTemplate();
    void start(Dungeon dungeon);
    Inventory merchant();
}
