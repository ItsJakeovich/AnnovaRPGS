package net.annova.command.cmdAvatar;

import net.annova.command.register.CommandArg;
import net.annova.lang.AISString;
import net.annova.object.person.Person;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.Status;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.ValueType;
import org.bukkit.entity.Player;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class AvatarCode {
    @CommandArg(name = "equipment", aliases = {"equip", "экипировка", "экип", "уйгшзьуте", "'rbgbhjdrf", "уйгшз"})
    public void equipment(Player player) {
        getRPGPerson(player).getEquipment().open();
    }

    @CommandArg(name = "info", aliases = {"information", "инфо", "информация", "штащ", "byaj"})
    public void information(Player player) {
        RPGPerson person = getRPGPerson(player);
        Person api = person.getPerson();
        AISString message = api.getAISString(getInstance(), "avatar-information")
                .replaceTag("{name}", player.getName())
                .replaceTag("{location}", person.getStringSectorLocation())
                // Modifiers
                .replaceTag("{damage}", person.getStringFinalValue(ValueType.DAMAGE))
                .replaceTag("{damage-multiplier}", person.getStringFinalPercent(ValueType.DAMAGE_MULTIPLIER))
                .replaceTag("{defence}", person.getStringFinalValue(ValueType.DEFENCE))
                .replaceTag("{defence-multiplier}", person.getStringFinalPercent(ValueType.DEFENCE_MULTIPLIER, true))
                .replaceTag("{fall-multiplier}", person.getStringFinalPercent(ValueType.FALL_MULTIPLIER, true))
                .replaceTag("{speed}", person.getStringFinalPercent(ValueType.SPEED))
                .replaceTag("{health}", person.getStringFinalValue(ValueType.HEALTH))
                .replaceTag("{regen}", person.getStringFinalValue(ValueType.REGENERATION))
                .replaceTag("{block}", person.getStringFinalValue(ValueType.BLOCK_RANGE))
                .replaceTag("{entity}", person.getStringFinalValue(ValueType.ENTITY_RANGE))
        ;
        api.send(message.getDefinedSplitted(api));
    }

//    @CommandArg(name = "status")
//    public void status(Person person, Player player, String[] args) {
//        StatusManager manager = getRPGPerson(player).getStatusManager();
//        player.sendMessage("statuses:");
//        for (Status status : manager.getStatuses()) {
//            player.sendMessage(status.getType().name());
//        }
//    }
}
