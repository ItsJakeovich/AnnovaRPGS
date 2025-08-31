package net.annova.object.person.status.event;

import net.annova.event.custom.BattleCE;
import net.annova.object.entity.mobs.sector0.dryad.DarkWolf;
import net.annova.object.entity.mobs.sector0.dryad.ForestWolf;
import net.annova.object.magic.physic.EntRoots;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
import net.annova.object.person.status.ValueType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.environment.world.GlobalWorld.isNight;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.CREATING_NIGHT_AMULET;
import static net.annova.object.person.equipment.ArmorSet.CHITIN;
import static net.annova.object.person.status.StatusType.PARALYSIS;
import static net.annova.object.person.status.StatusType.ROOTS_LASH_BUFF;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.NMSObjects.getArmorContents;

public class DamageEvent implements Listener {
    @EventHandler
    public void status(BattleCE event) {
        if (event.getDamager() instanceof Player player) {
            RPGPerson person = getRPGPerson(player);
            double value = person.getFinalValue(ValueType.DAMAGE);
            if (value != 0) {
                event.increaseDamage(value * player.getAttackStrengthScale(0.0F));
            }
            value = person.getFinalValue(ValueType.DAMAGE_MULTIPLIER);
            if (value != 0) {
                event.increaseDamage(event.getBaseDamage() * value);
            }

            LivingEntity target = event.getEntity();
            StatusManager manager = person.getStatusManager();
            if (CREATING_NIGHT_AMULET.hasEquipment(player)) {
                target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 300, 0));
            }
            if (manager.hasStatus(StatusType.ENT_PATRON_BUFF)) {
                if (chance(0.04)) EntRoots.addPhysicDebuff(target);
            }
            if (manager.hasStatus(StatusType.PACK_LEADER_BUFF)) {
                double modifier = 1;
                for (ItemStack item : getArmorContents(target)) {
                    if (!item.isEmpty()) modifier -= 0.25;
                }
                event.increaseDamage(event.getBaseDamage()*modifier);
                if (isNight(player.level()) && chance(0.18)) {
                    new DarkWolf().createWithTarget(player, target, 1, 140);
                } else if (chance(0.17)) {
                    new ForestWolf().createWithTarget(player, target, 1, 100);
                }
            }
            if (manager.hasStatus(StatusType.PHANTOM_FEAR)) {
                event.increaseDamage(event.getBaseDamage()*0.1);
            }
            if (manager.hasStatus(ROOTS_LASH_BUFF)) {
                if (chance(0.02)) EntRoots.addPhysicDebuff(target);
            }

            if (target instanceof Player targ) {
                if (CHITIN == person.getArmorSet() && chance(0.05)) {
                    getRPGPerson(targ).addStatus(PARALYSIS);
                }
            }
        }
    }
}
