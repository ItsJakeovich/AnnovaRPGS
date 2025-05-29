package net.annova.command.cmdMap;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaAPI.getInstance;

public class MapRPGCommand extends CommandBase {
    public MapRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "map",
                new String[]{"карта", "ьфз", "rfhnf"},
                "Send URL-link for map",
                MapCode.class.getDeclaredConstructor().newInstance(),
                getInstance(),
                false
        );
    }
}
