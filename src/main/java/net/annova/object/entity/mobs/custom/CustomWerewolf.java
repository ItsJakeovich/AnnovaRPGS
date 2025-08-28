package net.annova.object.entity.mobs.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class CustomWerewolf extends Zombie {
    public CustomWerewolf(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isSunBurnTick() {
        return false;
    }
}
