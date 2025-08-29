package net.annova.command.cmdKill;

import net.annova.command.register.DefaultArg;
import net.annova.object.person.Person;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

public class KillCode {
    @DefaultArg
    public void def(Person person, Player player, String[] args) {
        ServerPlayer server = person.getServerPlayer();
        server.kill(server.serverLevel());
    }
}
