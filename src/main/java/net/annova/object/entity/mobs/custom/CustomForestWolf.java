package net.annova.object.entity.mobs.custom;

import net.annova.object.person.status.StatusType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.chance;

public class CustomForestWolf extends Wolf {
    public CustomForestWolf(EntityType<? extends Wolf> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel worldserver, @NotNull Entity entity) {
        if (super.doHurtTarget(worldserver, entity)) {
            if (entity instanceof Player player && chance(0.02)) {
                getRPGPerson(player.getUUID()).addStatus(StatusType.BLEEDING);
                player.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1, 1);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean setTarget(@Nullable LivingEntity target, @Nullable EntityTargetEvent.TargetReason reason) {
        if (reason == EntityTargetEvent.TargetReason.CLOSEST_PLAYER && target instanceof Player) {
            if (getRPGPerson(target.getUUID()).getStatusManager().hasStatus(StatusType.PACK_LEADER_BUFF)) return false;
        }
        return super.setTarget(target, reason);
    }
}
