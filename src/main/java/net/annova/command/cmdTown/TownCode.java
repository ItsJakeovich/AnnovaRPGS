package net.annova.command.cmdTown;

import net.annova.command.register.CommandArg;
import net.annova.environment.civilization.Town;
import net.annova.lang.AISString;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.Vec3i;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.StringJoiner;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.GlobalRPG.getTown;

public class TownCode {
    @CommandArg(name = "info", aliases = {"i", "и", "штащ", "инфо", "byaj"})
    public void town(Player player, String[] args) {
        RPGPerson rpg = getRPGPerson(player);
        Person person = rpg.getPerson();
        Town town;
        if (args.length == 1) {
            town = rpg.getTown();
            if (town == null) {
                person.send(getInstance(), "civ-info-cancel-town");
                return;
            }
        } else {
            town = getTown(args[1]);
            if (town == null) {
                person.send(getInstance(), "twn-info-not-found");
                return;
            }
        }

        AISString info = person.getAISString(getInstance(), "twn-info")
                .replaceTag("{name}", town.getName())
                .replaceTag("{civ}", town.getCivilization().getName())
                ;

        StringJoiner members = new StringJoiner("{sc}, {mc}");
        members.add("{ac}" + town.getLeader().getPerson().getName());
        if (town.getMembers().contains(rpg) || town.getCivilization().getLeaders().contains(rpg)) {
            Set<RPGPerson> toList = town.getMembers();
            toList.remove(town.getLeader());
            for (RPGPerson member : toList) {
                members.add(member.getPerson().getName());
            }
            Vec3i pos = town.getCenterChunk().getPosition().getMiddleBlockPosition(63);
            info.append(person.getAISString(getInstance(), "twn-info-advanced")
                    .replaceTag("{pos}", pos.getX() + "{sc}, {mc}" + pos.getY() + "{sc}, {mc}" + pos.getZ())
                    .replaceTag("{growth}", String.valueOf((int)town.getGrowth()))
                    .replaceTag("{hammers}", String.valueOf((int)town.getHammers()))
                    .replaceTag("{culture}", String.valueOf((int)town.getCulture()))
                    .replaceTag("{science}", String.valueOf((int)town.getScience()))
            );
        }

        info.replaceTag("{members}", members + "{sc}.");
        person.send(info);
    }
}
