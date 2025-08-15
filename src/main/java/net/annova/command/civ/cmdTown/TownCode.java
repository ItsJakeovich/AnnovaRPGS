package net.annova.command.civ.cmdTown;

import net.annova.command.register.CommandArg;
import net.annova.environment.civilization.StatType;
import net.annova.environment.civilization.Town;
import net.annova.lang.AISString;
import net.annova.object.person.Person;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.Vec3i;
import org.bukkit.entity.Player;

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
        } else { town = getTown(args[1]);
            if (town == null) {
                person.send(getInstance(), "twn-info-not-found", "{town}", args[1]);
                return;
            }
        }

        AISString info = person.getAISString(getInstance(), "twn-info")
                .replaceTag("{name}", town.getName())
                .replaceTag("{civ}", town.getCivilization().getName())
                ;

        boolean advanced = false;
        if (town.getMembers().contains(rpg) || town.getCivilization().getLeaders().contains(rpg)) {
            Vec3i pos = town.getCenterChunk().getPosition().getMiddleBlockPosition(63);
            info.append(person.getAISString(getInstance(), "twn-info-advanced")
                    .replaceTag("{pos}", pos.getX() + "{sc}, {mc}" + pos.getY() + "{sc}, {mc}" + pos.getZ())
                    .replaceTag("{growth}", String.valueOf((int)town.getValue(StatType.GROWTH)))
                    .replaceTag("{hammers}", String.valueOf((int)town.getValue(StatType.HAMMERS)))
                    .replaceTag("{culture}", String.valueOf((int)town.getValue(StatType.CULTURE)))
                    .replaceTag("{science}", String.valueOf((int)town.getValue(StatType.CULTURE)))
            );
            advanced = true;
        }

        StringJoiner members = new StringJoiner("{sc}, ");
        for (RPGPerson member : town.getMembers()) {
            String color = "{mc}";
            if (town.getLeaders().contains(member)) color = "{ac}";
            else if (!advanced) continue;
            members.add(color + member.getPerson().getName());
        }

        info.replaceTag("{members}", members + "{sc}.");
        person.send(info);
    }

    @CommandArg(name = "stats", aliases = {"статы", "ыефеы", "s", "с"})
    public void stats(Player player) {
        RPGPerson rpg = getRPGPerson(player);
        Person person = rpg.getPerson();
        Town town = rpg.getTown();
        if (town == null) {
            person.send(getInstance(), "civ-info-cancel-town");
            return;
        }
        person.send(person.getAISString(getInstance(), "twn-stats")
                .replaceTag("{town}", town.getName())
                .replaceTag("{growthtotal}", String.valueOf(town.getValue(StatType.GROWTH)))
                .replaceTag("{growthbase}", String.valueOf(town.getStatSource(StatType.GROWTH, Town.StatSources.BASE)))
                .replaceTag("{growthchunk}", String.valueOf(town.getStatSource(StatType.GROWTH, Town.StatSources.CHUNK)))

                .replaceTag("{hammertotal}", String.valueOf(town.getValue(StatType.HAMMERS)))
                .replaceTag("{hammerbase}", String.valueOf(town.getStatSource(StatType.HAMMERS, Town.StatSources.BASE)))
                .replaceTag("{hammerchunk}", String.valueOf(town.getStatSource(StatType.HAMMERS, Town.StatSources.CHUNK)))
        );
    }
}
