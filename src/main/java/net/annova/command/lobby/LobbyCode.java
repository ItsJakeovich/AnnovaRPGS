package net.annova.command.lobby;

import net.annova.command.register.DefaultArg;
import net.annova.object.person.Person;
import org.bukkit.entity.Player;

import static net.annova.environment.world.GlobalWorld.LOBBY_TELEPORT;

public class LobbyCode {
    @DefaultArg
    public void lobbyMain(Person person, Player player, String[] args) {
        person.getServerPlayer().teleport(LOBBY_TELEPORT);
    }
}
