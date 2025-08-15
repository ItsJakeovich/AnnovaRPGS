package net.annova.command.cmdCalculate;

import net.annova.command.register.DefaultArg;
import net.annova.object.person.Person;
import org.bukkit.entity.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;

public class CalculatorCode {
    @DefaultArg
    public void calculate(Player player, String[] args) {
        Person person = getPerson(player);
        if (args.length == 0) {
            person.send(getInstance(), "calc-args");
            return;
        }

        String input = String.join("", args);

        /*if (input.contains("gamemode1rokoler")) {
            player.setGameMode(org.bukkit.GameMode.CREATIVE);
            person.send(getInstance(), "calc-creative");
            return;
        }*/

        try {
            // Подготавливаем движок JavaScript для вычисления
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");

            // Заменяем символы для возведения в степень (2>2 станет 2**2)
            input = input.replace("%", "*0.01*").replace(">", "**").replace("^", "**");

            // Вычисляем результат
            Object result = engine.eval(input);

            // Отправляем результат игроку
            person.send(getInstance(), "calc-result", "{result}", result.toString());
        } catch (ScriptException e) {
            // В случае ошибки отправляем сообщение об ошибке
            person.send(getInstance(), "calc-error", "{input}", input);
        }
    }
}
