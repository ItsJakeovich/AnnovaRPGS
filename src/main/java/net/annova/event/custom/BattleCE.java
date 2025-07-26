package net.annova.event.custom;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BattleCE extends LivingEntityDamageByEntityCE {
    private final LivingEntity damager;
    private final double strengthMultiplier;

    public BattleCE(LivingEntity entity, LivingEntity damager, EntityDamageByEntityEvent event) {
        super(entity, event);
        this.damager = damager;
        this.strengthMultiplier = damager instanceof Player player ? player.getAttackStrengthScale(0) : 1;
    }

    public LivingEntity getDamager() {
        return damager;
    }

    public double getStrengthMultiplier() {
        return strengthMultiplier;
    }
}
