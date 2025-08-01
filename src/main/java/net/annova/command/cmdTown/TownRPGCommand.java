package net.annova.command.cmdTown;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaRPG.getInstance;

public class TownRPGCommand extends CommandBase {
    public TownRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "town",
                new String[]{"t", "г", "город", "ujhjl", "ещцт"},
                "town control panel",
                new TownCode(),
                getInstance(),
                false
        );
    }
}
