package net.annova.object.entity.mobs.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class MinesPhantom extends Zombie {
    public MinesPhantom(EntityType<? extends Zombie> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }
}
