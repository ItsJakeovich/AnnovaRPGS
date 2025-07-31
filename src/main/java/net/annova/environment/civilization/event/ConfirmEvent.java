package net.annova.environment.civilization.event;

import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.event.custom.ConfirmChatActionCE;
import net.annova.event.custom.PersonConfirmCE;
import net.annova.object.Confirm;
import net.annova.object.Person;
import net.annova.object.SimpleObject;
import net.annova.object.person.RPGPerson;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.lang.MessageObjects.isCyrillicOrLatin;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class ConfirmEvent implements Listener {
    @EventHandler
    public void chat(ConfirmChatActionCE event) {
        Person person = event.getPerson();
        String command = event.getCommand();
        switch (person.getConfirm().getConfirmType()) {
            case "set-camp" -> {
                if (command.length() < 4 || command.length() > 20 || !isCyrillicOrLatin(command)) {
                    person.send(getInstance(), "camp-set-cancel-name");
                    return;
                }
                person.send(getInstance(), "camp-set-name", "{name}", command);
                person.getConfirm().addData(new SimpleObject("name", event.getCommand()));
            }
        }
    }

    @EventHandler
    public void confirm(PersonConfirmCE event) {
        Person person = event.getPerson();

        Confirm confirm = person.getConfirm();
        switch (confirm.getConfirmType()) {
            case "set-camp" -> {
                String name = (String) confirm.getData().getOrDefault("name", "");
                if (name.isEmpty()) {
                    person.send(getInstance(), "camp-set-cancel-blank");
                    return;
                }

                RPGPerson rpg = getRPGPerson(person);
                ServerPlayer player = rpg.getServerPlayer();

                AreaChunk chunk = new AreaChunk(player.serverLevel(), player.chunkPosition());
                if (chunk.isWater()) {
                    person.send(getInstance(), "camp-set-cancel-water");
                    person.resetConfirm();
                    return;
                }

                person.send(getInstance(), "camp-set", "{name}", name);
                Town town = new Town(name, chunk, rpg);
                rpg.setTown(town);
            }
            default -> {
                return;
            }
        }
        person.resetConfirm();
    }
}
