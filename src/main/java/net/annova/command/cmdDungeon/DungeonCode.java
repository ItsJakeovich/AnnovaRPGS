package net.annova.command.cmdDungeon;

import net.annova.command.register.CommandArg;
import net.annova.environment.dungeon.Dungeon;
import net.annova.object.person.Person;
import net.minecraft.core.Vec3i;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.GlobalRPG.getDungeon;
import static net.annova.main.GlobalRPG.getDungeons;
import static net.annova.object.item.base.ItemType.DUNGEON_KEY;
import static net.annova.util.BukkitObjects.addItems;
import static net.annova.main.AnnovaRPG.getInstance;

public class DungeonCode {
    @CommandArg(name = "key")
    public void key(Player player) {
        addItems(player, CraftItemStack.asBukkitCopy(DUNGEON_KEY.create(player)));
    }

    @CommandArg(name = "list")
    public void list(Player player) {
        Person person = getPerson(player);
        person.send(getInstance(), "dungeon-list-list");
        for (Dungeon dungeon : getDungeons()) {
            Vec3i loc = dungeon.getPosition();
            person.send("{pc}● " + dungeon.getKey() + " | " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
        }
    }
    @CommandArg(name = "delete")
    public void delete(Player player, String[] args) {
        Person person = getPerson(player);
        if (args.length < 2) {
            person.send(getInstance(), "dungeon-delete-args");
            return;
        }
        String key = args[1];
        Dungeon dungeon = getDungeon(Integer.parseInt(key));
        if (dungeon == null) {
            person.send(getInstance(), "dungeon-delete-not-found", "{key}", key);
            return;
        }
        dungeon.endDungeon(true);
        person.send(getInstance(), "dungeon-deleted");
    }
}
