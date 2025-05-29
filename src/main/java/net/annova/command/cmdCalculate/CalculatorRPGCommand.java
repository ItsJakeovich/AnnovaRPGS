package net.annova.command.cmdCalculate;

import net.annova.command.cmdRPG.RPGCode;
import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaAPI.getInstance;

public class CalculatorRPGCommand extends CommandBase {
    public CalculatorRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "calc",
                new String[]{"калькулятор", "сфдс", "rfkmrekznjh", "калька", "rfkmrf", "calculator", "сфдсгдфещк"},
                "Calculator",
                CalculatorCode.class.getDeclaredConstructor().newInstance(),
                getInstance(),
                false
        );
    }
}
