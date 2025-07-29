package net.annova.command.cmdCivilization;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaRPG.getInstance;

public class CivilizationRPGCommand extends CommandBase {
    public CivilizationRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "civilization",
                new String[]{"цивилизация", "сшмшдшяфешщт", "wbdbkbpfwbz", "цив", "wbd", "civ", "сшм"},
                "civilization control panel",
                new CivilizationCode(),
                getInstance(),
                false
        );
    }
}
