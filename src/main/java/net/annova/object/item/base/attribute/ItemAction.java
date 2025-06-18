package net.annova.object.item.base.attribute;

import org.bukkit.entity.LivingEntity;

public interface ItemAction {
    void onAttack(LivingEntity entity, LivingEntity target);
}
