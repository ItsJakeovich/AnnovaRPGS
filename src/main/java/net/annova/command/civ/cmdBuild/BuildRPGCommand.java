package net.annova.command.civ.cmdBuild;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaRPG.getInstance;

public class BuildRPGCommand extends CommandBase {
    public BuildRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "build",
                new String[]{"b", "с", "строить", "игшдв", "cnhjbnm"},
                "civilization control panel",
                new BuildCode(),
                getInstance(),
                true
        );
    }
}
