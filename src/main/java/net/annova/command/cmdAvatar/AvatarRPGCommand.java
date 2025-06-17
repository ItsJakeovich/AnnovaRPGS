package net.annova.command.cmdAvatar;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaAPI.getInstance;

public class AvatarRPGCommand extends CommandBase {
    public AvatarRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "avatar",
                new String[]{"аватар", "ava", "фмф", "fdf"},
                "Avatar control panel",
                AvatarCode.class.getDeclaredConstructor().newInstance(),
                getInstance(),
                false
        );
    }
}
