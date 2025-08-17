package net.annova.object.entity.mobs.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CustomSmallSpider extends Silverfish {
    public CustomSmallSpider(EntityType<? extends Silverfish> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel worldserver, DamageSource damagesource, float f) {
        if (damagesource.is(DamageTypes.DROWN)) return false;
        return super.hurtServer(worldserver, damagesource, f);
    }
}
