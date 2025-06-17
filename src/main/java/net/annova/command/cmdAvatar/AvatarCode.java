package net.annova.command.cmdAvatar;

import net.annova.command.register.CommandArg;
import net.annova.object.person.RPGPerson;
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
}
