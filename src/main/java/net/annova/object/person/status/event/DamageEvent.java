package net.annova.object.person.status.event;

import net.annova.event.custom.BattleCE;
import net.annova.object.magic.physic.EntRoots;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
import net.annova.object.person.status.ValueType;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ArmorSet.CHITIN;
import static net.annova.object.person.status.StatusType.PARALYSIS;
import static net.annova.object.person.status.StatusType.ROOTS_LASH_BUFF;
import static net.annova.util.BukkitObjects.chance;

public class DamageEvent implements Listener {

    @EventHandler
    public void status(BattleCE event) {
        if (event.getDamager() instanceof Player player) {
            RPGPerson person = getRPGPerson(player);
            double value = person.getFinalValue(ValueType.DAMAGE);
            if (value != 0) {
                event.increaseDamage(value * ((CraftPlayer) player).getHandle().getAttackStrengthScale(0.0F));
            }
            value = person.getFinalValue(ValueType.DAMAGE_MULTIPLIER);
            if (value != 0) {
                event.increaseDamage(event.getBaseDamage() * value);
            }

            LivingEntity target = event.getEntity();
            StatusManager manager = person.getStatusManager();
            if (manager.hasStatus(StatusType.ENT_PATRON_BUFF)) {
                if (chance(0.04)) EntRoots.addPhysicDebuff(target);
            }
            if (manager.hasStatus(ROOTS_LASH_BUFF)) {
                if (chance(0.02)) EntRoots.addPhysicDebuff(target);
            }

            if (CHITIN == person.getArmorSet() && chance(0.05)) {
                manager.addStatus(PARALYSIS);
            }
        }
    }
}
