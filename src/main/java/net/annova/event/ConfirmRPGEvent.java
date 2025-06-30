package net.annova.event;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.type.Prison;
import net.annova.event.custom.PersonConfirmCE;
import net.annova.object.item.base.ItemType;
import net.annova.object.*;
import net.annova.environment.structure.Structure;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.addItems;

public class ConfirmRPGEvent implements Listener {
    @EventHandler
    public void confirm(PersonConfirmCE event) {
        Person person = event.getPerson();

        Confirm confirm = person.getConfirm();
        switch (confirm.getConfirmType()) {
            case "team-request" -> {
                RPGPerson pers = getRPGPerson(person.getUid());
                if (pers.getTeam() != null) {
                    person.send(getInstance(), "team-invite-request-already", "{team}", pers.getTeam().getName());
                    break;
                }

                Team team = (Team) person.getConfirm().getData().get("team");
                pers.setTeam(team);
                team.addMember(pers);

                String name = person.getName();
                for (RPGPerson member : team.getMembers()) {
                    member.getPerson().send(getInstance(), "team-invite-successfully", "{name}", name);
                }
            }
            case "team-gathering" -> {
                RPGPerson pers = getRPGPerson(person.getUid());
                if (pers.getDungeon() != null) {
                    person.send(getInstance(), "team-gathering-already");
                    return;
                }
                Dungeon dungeon = (Dungeon) person.getConfirm().getData().get("dungeon");
                pers.setDungeon(dungeon);
                pers.setReady(false);
                dungeon.teleportPerson(pers);
            }
            case "dungeon" -> {
                Player player = person.getPlayer();
                ItemType itemType = ItemType.DUNGEON_KEY;
                if (!player.getInventory().removeItem(itemType.create(player)).isEmpty()) {
                    person.send(getInstance(), "dungeon-missing-key");
                    break;
                }

                Dungeon dungeon = new Dungeon(new Prison(), person.getLocation().clone());

                RPGPerson pers = getRPGPerson(person.getUid());
                dungeon.addPlayers(pers.getTeam().getMembers());
                pers.setReady(false);
                Team team = pers.getTeam();
                if (team != null) {
                    for (RPGPerson membr : team.getMembers()) {
                        if (membr.equals(pers)) continue;
                        Person member = membr.getPerson();
                        member.getConfirm().setConfirmType("team-gathering").addData(new SimpleObject("dungeon", pers.getDungeon()));
                        member.send(getInstance(), "team-gathering-request", "{name}", person.getName());
                    }
                    person.send(getInstance(), "team-gathering-successfully");
                }
                pers.setDungeon(dungeon);

//                DungeonTemplate dungeonTemplate = dungeon.getBase().getTemplate();

                dungeon.createDungeon();

                dungeon.teleportPerson(pers);
            }
            case "structure" -> {
                Structure structure = (Structure) confirm.getData().get("structure");
                structure.getBase().create(structure, person.getLocation());
            }
            default -> {
                return;
            }
        }
        person.resetConfirm();
    }
}
