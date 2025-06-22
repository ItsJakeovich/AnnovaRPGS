package net.annova.object.person.status.event;

import net.annova.event.custom.BattleCE;
import net.annova.object.person.status.ValueType;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class DamageEvent implements Listener {
    @EventHandler
    public void status(BattleCE event) {
        if (event.getDamager() instanceof Player player) {
            ServerPlayer server = ((CraftPlayer)player).getHandle();
            player.sendMessage(" ");
            player.sendMessage(String.valueOf(player.getAttackCooldown()));
            player.sendMessage(String.valueOf(server.getAttackStrengthScale(0.0F)));
            double value = getRPGPerson(player).getFinalValue(ValueType.DAMAGE);
            player.sendMessage(String.valueOf(value));
            if (value == 0) return;
            event.increaseDamage(value * player.getAttackCooldown());
        }
    }
}
