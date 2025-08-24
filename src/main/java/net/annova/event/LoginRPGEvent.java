package net.annova.event;

import net.annova.event.custom.PostPersonJoinCE;
import net.annova.object.person.Person;
import net.annova.object.person.RPGPerson;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.environment.world.GlobalWorld.LOBBY_TELEPORT;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.addItems;
import static net.minecraft.world.effect.MobEffects.INVISIBILITY;

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
        ServerPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
        player.teleport(LOBBY_TELEPORT);
        MobEffectInstance effect = player.getEffect(INVISIBILITY);
        if (effect != null && effect.isInfiniteDuration()) player.removeEffect(INVISIBILITY);
    }
}
