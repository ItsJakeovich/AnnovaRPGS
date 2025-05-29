package net.annova.command.cmdInventory;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaAPI.getInstance;

public class InventoryRPGCommand extends CommandBase {
    public InventoryRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "inventory",
                new String[]{"invsee", "инвентарь", "просмотреть", "штмутещкн", "штмыуу", "bydtynfhm", "ghjcvjnhtnm"},
                "Open player inventory",
                InventoryCode.class.getDeclaredConstructor().newInstance(),
                getInstance(),
                true
        );
    }
}
