package net.annova.command.recipe;

import net.annova.command.register.CommandBase;

import java.lang.reflect.InvocationTargetException;

import static net.annova.main.AnnovaRPG.getInstance;

public class RecipeRPGCommand extends CommandBase {
    public RecipeRPGCommand() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(
                "recipe",
                new String[]{"рецепты", "кусшзу", "htwtgns"},
                "Recipe list",
                new RecipeCode(),
                getInstance(),
                false
        );
    }
}
