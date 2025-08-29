package net.annova.environment.dungeon.type;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.lang.AISString;
import net.annova.object.person.Person;
import org.bukkit.inventory.Inventory;

public interface DungeonBase {
    DungeonTemplate getTemplate();
    void start(Dungeon dungeon);
    AISString message(Person person);
    Inventory merchant();
}
