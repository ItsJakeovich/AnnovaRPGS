package net.annova.command.lobby;

import net.annova.command.register.DefaultArg;
import net.annova.object.person.Person;
import org.bukkit.entity.Player;

import static net.annova.environment.world.GlobalWorld.LOBBY_TELEPORT;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class LobbyCode {
    @DefaultArg
    public void lobbyMain(Person person, Player player, String[] args) {
        getRPGPerson(person).updateLastPosition();
        person.getServerPlayer().teleport(LOBBY_TELEPORT);
    }
}
