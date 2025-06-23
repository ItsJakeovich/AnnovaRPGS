package net.annova.command.cmdRPG;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaRPG.getInstance;

public class RPGCommand extends CommandBase {
    public RPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "rpg",
                new String[]{"рпг", "кзп", "hgu"},
                "RPG-Server control panel",
                RPGCode.class.getDeclaredConstructor().newInstance(),
                getInstance(),
                true
        );
    }
}
