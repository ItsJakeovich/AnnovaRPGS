package net.annova.command.cmdAvatar;

import net.annova.command.register.CommandArg;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.ValueType;
import net.annova.util.AISString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class AvatarCode {
    @CommandArg(name = "switch-ui")
    public void switchBar(Player player) {
        RPGPerson person = getRPGPerson(player);
        boolean status = !person.isShowBar();
        person.setShowBar(status);
        if (status) person.getPerson().getScoreboard().initScoreBoard();
        else player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        person.getPerson().send(getInstance(), "avatar-switchui", "{status}", String.valueOf(status));
    }

    @CommandArg(name = "equipment", aliases = {"equip", "экипировка", "экип", "уйгшзьуте", "'rbgbhjdrf", "уйгшз"})
    public void equipment(Player player) {
        getRPGPerson(player).getEquipment().open(player);
    }

    @CommandArg(name = "info", aliases = {"information", "инфо", "информация", "штащ", "byaj"})
    public void information(Player player) {
        RPGPerson person = getRPGPerson(player);
        Person apiPerson = person.getPerson();
        AISString message = apiPerson.getAISString(getInstance(), "avatar-information")
                .replaceFirst("{name}", player.getName())
                .replaceFirst("{location}", person.getStringSectorLocation())
                // Modifiers
                .replaceFirst("{damage}", person.getStringFinalValue(ValueType.DAMAGE))
                .replaceFirst("{damage-multiplier}", person.getStringFinalPercent(ValueType.DAMAGE_MULTIPLIER))
                .replaceFirst("{defence-multiplier}", person.getStringFinalPercent(ValueType.DEFENCE_MULTIPLIER))
                .replaceFirst("{fall-multiplier}", person.getStringFinalPercent(ValueType.FALL_MULTIPLIER))
                .replaceFirst("{health}", person.getStringFinalValue(ValueType.HEALTH))
                .replaceFirst("{regen}", person.getStringFinalValue(ValueType.REGENERATION))
                .replaceFirst("{block}", person.getStringFinalValue(ValueType.BLOCK_RANGE))
                .replaceFirst("{entity}", person.getStringFinalValue(ValueType.ENTITY_RANGE))
        ;
        apiPerson.send(apiPerson.colorString(message.string));
    }
}
