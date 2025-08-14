package net.annova.object.entity.mobs.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.Nullable;

import static net.annova.object.item.base.ItemType.ENT_CORE;

public class MinesPhantom extends Zombie {
    public MinesPhantom(EntityType<? extends Zombie> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean setTarget(@Nullable LivingEntity target, @Nullable EntityTargetEvent.TargetReason reason) {
        if (reason == EntityTargetEvent.TargetReason.CLOSEST_PLAYER && target instanceof Player player) if (ENT_CORE.has(player)) return false;
        return super.setTarget(target, reason);
    }

    @Override
    public boolean isSunBurnTick() {
        return false;
    }
}
