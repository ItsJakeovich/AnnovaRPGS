package net.annova.event;

import net.annova.event.custom.PostPersonJoinCE;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.addItems;

public class LoginRPGEvent implements Listener {
    @EventHandler
    private void login(PostPersonJoinCE event) {
        Person pers = event.getPerson();
        RPGPerson person = getRPGPerson(pers);
        List<ItemStack> pending = person.getPendingItems();
        if (!pending.isEmpty()) {
            addItems(pers.getPlayer(), pending.toArray(new ItemStack[0]));
            person.clearPending();
        }
    }
}
