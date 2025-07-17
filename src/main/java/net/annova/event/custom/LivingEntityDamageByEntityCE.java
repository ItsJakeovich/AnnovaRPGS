package net.annova.event.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LivingEntityDamageByEntityCE extends MobDamageCE {

    private final Entity damager;

    public LivingEntityDamageByEntityCE(LivingEntity entity, EntityDamageByEntityEvent event) {
        super(entity, event);
        this.damager = ((CraftEntity) event.getDamager()).getHandle();
    }

    public Entity getDamager() {
        return damager;
    }
}
