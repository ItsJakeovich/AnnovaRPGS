package net.annova.command.cmdKill;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaRPG.getInstance;

public class KillRPGCommand extends CommandBase {
    public KillRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "kill",
                new String[]{"умереть"},
                "suicide command",
                new KillCode(),
                getInstance(),
                false
        );
    }
}
