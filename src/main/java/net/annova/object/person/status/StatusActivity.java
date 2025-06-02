package net.annova.object.person.status;

import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;

public interface StatusActivity {
    boolean activity(RPGPerson person, Player player, Object object);
    long repeating();
}
