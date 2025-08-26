package net.annova.command.civ.cmdTown;

import net.annova.command.register.CommandArg;
import net.annova.environment.civilization.StatType;
import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.lang.AISString;
import net.annova.object.person.Confirm;
import net.annova.object.person.Person;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.StringJoiner;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.*;

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
    public void stats(Person person, Player player, String[] args) {
        RPGPerson rpg = getRPGPerson(player);
        Town town = rpg.getTown();
        if (getRPGPerson(player) == null) {
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

    @CommandArg(name = "invite", aliases = {"пригласить", "ghbukfcbnm", "штмшеу"})
    public void invite(Person person, Player player, String[] args) {
        if (args.length < 2) {
            person.send(getInstance(), "town-invitekick-cancel-args");
            return;
        }
        RPGPerson invited = getRPGPerson(args[1]);
        if (invited == null) {
            person.send(getInstance(), "player-not-found", "{name}", args[1]);
            return;
        }
        if (invited.getTown() != null) {
            person.send(getInstance(), "town-invite-cancel-busy");
            return;
        }
        RPGPerson rpg = getRPGPerson(player);
        Town town = rpg.getSelectedTown();
        if (town == null) {
            person.send(getInstance(), "civ-info-cancel-town");
            return;
        }
        if (!town.getLeaders().contains(rpg)) {
            person.send(getInstance(), "town-info-cancel-leader");
            return;
        }
        // Отсылаем приглашение
        Confirm confirm = invited.getPerson().getConfirm();
        confirm.setConfirmType("invite-town");
        confirm.getData().put("town", town);
        Person invite = invited.getPerson();
        AISString mail = invite.getAISString(getInstance(), "town-invite-mail");
        mail.replaceTag("{inviter}", person.getName());
        mail.replaceTag("{town}", town.getName());
        invite.send(mail);

        person.send(getInstance(), "town-invite-sended", "{nick}", invited.getPerson().getName());
    }
    @CommandArg(name = "kick" , aliases = {"исключить", "bcrk.xbnm", "лшсл"})
    public void kick(Person person, Player player, String[] args) {
        if (args.length < 2) {
            person.send(getInstance(), "town-invitekick-cancel-args");
            return;
        }
        RPGPerson rpg = getRPGPerson(player);
        Town town = rpg.getSelectedTown();
        if (town == null) {
            person.send(getInstance(), "civ-info-cancel-town");
            return;
        }
        if (!town.getLeaders().contains(rpg)) {
            person.send(getInstance(), "town-info-cancel-leader");
            return;
        }
        RPGPerson kicked = getRPGPerson(args[1]);
        if (kicked == null) {
            person.send(getInstance(), "player-not-found", "{name}", args[1]);
            return;
        }
        if (kicked == rpg) {
            person.send(getInstance(), "town-kick-cancel-yourself");
            return;
        }
        if (kicked.getTown() != town) {
            person.send(getInstance(), "town-kick-cancel-none");
            return;
        }

        Confirm confirm = rpg.getPerson().getConfirm();
        confirm.setConfirmType("kick-town");
        Map<String, Object> data = confirm.getData();
        data.put("person", kicked);
        data.put("town", town);
        person.send(getInstance(), "town-kick-confirm", "{nick}", kicked.getPerson().getName());
    }

    @CommandArg(name = "claim", aliases = {"приват", "ghbdfn", "сдфшь"})
    public void claim(Person person, Player player, String[] args) {
        RPGPerson rpg = getRPGPerson(player);
        Town town = rpg.getTown();
        if (town == null) {
            person.send(getInstance(), "civ-info-cancel-town");
            return;
        }
        if (town.isTodayClaim()) {
            person.send(getInstance(), "town-claim-cancel-today");
            return;
        }
        ServerLevel level = town.getLevel();
        ServerPlayer server = person.getServerPlayer();
        if (level != server.serverLevel()) {
            person.send(getInstance(), "town-claim-cancel-world");
            return;
        }
        ChunkPos pos = server.chunkPosition();
        if (getAreaChunk(level, pos) != null) {
            person.send(getInstance(), "town-claim-cancel-busy");
            return;
        }
        if (town.getChunks().size() >= 9) {
            person.send(getInstance(), "town-claim-cancel-limit");
            return;
        }

        boolean isAlone = true;
        for (int i = 0; i < 4; i++) {
            ChunkPos toCheck = switch (i) {
                case 0 -> new ChunkPos(pos.x+1, pos.z);
                case 1 -> new ChunkPos(pos.x-1, pos.z);
                case 2 -> new ChunkPos(pos.x, pos.z+1);
                case 3 -> new ChunkPos(pos.x, pos.z-1);
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
            AreaChunk chunk = getAreaChunk(level, toCheck);
            if (chunk == null) continue;
            if (chunk.getTown() != town) continue;
            isAlone = false; break;
        }
        if (isAlone) {
            person.send(getInstance(), "town-claim-cancel-alone");
            return;
        }
        person.send(getInstance(), "town-claim-success", "{coord}", "{mc}"+pos.x+"{sc},{mc}"+pos.z);
        town.addChunk(new AreaChunk(level, pos));
        town.setTodayClaim(true);
    }
}
