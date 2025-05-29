package net.annova.command.cmdTeam;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaRPG.getInstance;

public class TeamRPGCommand extends CommandBase {
    public TeamRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "team",
                new String[]{"команда", "еуфь", "rjvfylf"},
                "Team interaction",
                TeamCode.class.getDeclaredConstructor().newInstance(),
                getInstance(),
                false
        );
    }
}
