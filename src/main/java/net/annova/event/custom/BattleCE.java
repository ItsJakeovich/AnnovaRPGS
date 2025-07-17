package net.annova.event.custom;


import net.minecraft.world.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BattleCE extends LivingEntityDamageByEntityCE {
    private final LivingEntity damager;

    public BattleCE(LivingEntity entity, LivingEntity damager, EntityDamageByEntityEvent event) {
        super(entity, event);
        this.damager = damager;
    }

    public LivingEntity getDamager() {
        return damager;
    }
}
