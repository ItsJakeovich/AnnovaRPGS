package net.annova.command.cmdDungeon;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaRPG.getInstance;

public class DungeonRPGCommand extends CommandBase{
    public DungeonRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "dungeon",
                new String[]{"данж", "вгтпущт", "lfy;", "dung"},
                "dungeon's control panel",
                DungeonCode.class.getDeclaredConstructor().newInstance(),
                getInstance(),
                true
        );
    }
}
