package net.annova.command.lobby;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaRPG.getInstance;

public class LobbyRPGCommand extends CommandBase {
    public LobbyRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "lobby",
                new String[]{"hub", "дщиин", "рги", "лобби", "хаб"},
                "RPG-Server control panel",
                new LobbyCode(),
                getInstance(),
                false
        );
    }
}
