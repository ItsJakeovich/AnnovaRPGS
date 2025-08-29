package net.annova.command.cmdTeam;

import net.annova.command.register.CommandArg;
import net.annova.object.person.Person;
import net.annova.object.person.RPGPerson;
import net.annova.object.SimpleObject;
import net.annova.object.person.Team;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class TeamCode {
    @CommandArg(name = "create", aliases = {"создать", "скуфеу", "cjplfnm"}, description = "Create a team")
    public void create(Player player, String[] args) {
        RPGPerson pers = getRPGPerson(player);
        Person person = pers.getPerson();
        Team team = pers.getTeam();
        if (team != null) {
            person.send(getInstance(), "team-create-already", "{team}", team.getName());
            return;
        }

        if (args.length < 2) {
            person.send(getInstance(), "team-create-name");
            return;
        }
        Team newTeam = new Team(args[1], pers);
        pers.setTeam(newTeam);
        person.send(getInstance(), "team-create-successfully", "{team}", args[1]);
    }

    @CommandArg(name = "leave", aliases = {"покинуть", "дуфму", "gjrbyenm"}, description = "Leave a team")
    public void leave(Player player) {
        RPGPerson pers = getRPGPerson(player);
        Person person = pers.getPerson();
        Team team = pers.getTeam();

        if (team == null) {
            person.send(getInstance(), "team-absent");
            return;
        }
        if (team.getLeader() == pers) {
            person.send(getInstance(), "team-leave-owner");
            return;
        }
        pers.setTeam(null);
        team.removeMember(pers);

        person.send(getInstance(), "team-leave-successfully", "{team}", team.getName());
    }

    @CommandArg(name = "disband", aliases = {"расформировать", "вшыифтв", "hfcajhvbhjdfnm"}, description = "Disband a team")
    public void disband(Player player) {
        RPGPerson pers = getRPGPerson(player);
        Person person = pers.getPerson();
        Team team = pers.getTeam();

        if (team == null) {
            person.send(getInstance(), "team-absent");
            return;
        }
        if (team.getLeader() != pers) {
            person.send(getInstance(), "team-not-owner");
            return;
        }

        Set<RPGPerson> toRemove = new HashSet<>();
        for (RPGPerson member : team.getMembers()) {
            member.setTeam(null);
            toRemove.add(member);
        }
        team.removeMembers(toRemove);
        person.send(getInstance(), "team-disband-successfully", "{team}", team.getName());
    }

    @CommandArg(name = "invite", arguments = {"{players}"}, aliases = {"пригласить", "штмшеу", "ghbukfcbnm"}, description = "Invite a player to a team")
    public void invite(Player player, String[] args) {
        RPGPerson pers = getRPGPerson(player);
        Person person = pers.getPerson();
        Team team = pers.getTeam();

        if (team == null) {
            person.send(getInstance(), "team-absent");
            return;
        }
        if (args.length < 2) {
            person.send(getInstance(), "team-invite-name");
            return;
        }
        if (team.getLeader() != pers) {
            person.send(getInstance(), "team-not-owner");
            return;
        }
        RPGPerson invite = getRPGPerson(args[1]);
        if (invite == null) {
            person.send(getInstance(), "player-not-found", "{name}", args[1]);
            return;
        }

        invite.getPerson().getConfirm().setConfirmType("team-request").addData(new SimpleObject("team", team));

        person.send(getInstance(), "team-invite-invited", "{name}", invite.getPerson().getName());
        invite.getPerson().send(getInstance(), "team-invite-request", "{team}", team.getName());
    }

    @CommandArg(name = "kick", arguments = {"{players}"}, aliases = {"выгнать", "лшсл", "dsuyfnm"}, description = "kick a player out of the team")
    public void kick(Player player, String[] args) {
        RPGPerson pers = getRPGPerson(player);
        Person person = pers.getPerson();
        Team team = pers.getTeam();

        if (team == null) {
            person.send(getInstance(), "team-absent");
            return;
        }
        if (args.length < 2) {
            person.send(getInstance(), "team-kick-name");
            return;
        }
        if (team.getLeader() != pers) {
            person.send(getInstance(), "team-not-owner");
            return;
        }
        RPGPerson kick = getRPGPerson(args[1]);
        if (kick == null) {
            person.send(getInstance(), "player-not-found", "{name}", args[1]);
            return;
        }
        if (!team.getMembers().contains(kick)) {
            person.send(getInstance(), "team-kick-wrong");
            return;
        }
        kick.setTeam(null);
        kick.getPerson().send(getInstance(), "team-kick-kicked", "{team}", team.getName());

        team.removeMember(kick);

        String name = kick.getPerson().getName();
        for (RPGPerson member : team.getMembers()) {
            member.getPerson().send(getInstance(), "team-kick-successfully", "{name}", name);
        }
    }

    /*@CommandArg(name = "gathering", aliases = {"сбор", "пферукштп", "c,jh"}, description = "Gathering players in one place")
    public void gathering(Player player) {
        RPGPerson pers = getRPGPerson(player);
        Person person = pers.getPerson();
        Team team = pers.getTeam();

        if (pers.getDungeon() == null) {
            person.send(getInstance(), "team-gathering-absent");
            return;
        }
        if (team == null) {
            person.send(getInstance(), "team-absent");
            return;
        }

        for (RPGPerson membr : team.getMembers()) {
            if (membr.equals(pers)) continue;
            Person member = membr.getPerson();
            member.getConfirm().setConfirmType("team-gathering").addData(new SimpleObject("dungeon", pers.getDungeon()));
            member.send(getInstance(), "team-gathering-request", "{name}", person.getName());
        }
        person.send(getInstance(), "team-gathering-successfully");
    }*/
}
