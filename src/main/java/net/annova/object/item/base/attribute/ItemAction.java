package net.annova.object.item.base.attribute;

import net.minecraft.world.entity.LivingEntity;

public interface ItemAction {
    void onAttack(LivingEntity entity, LivingEntity target);
}
