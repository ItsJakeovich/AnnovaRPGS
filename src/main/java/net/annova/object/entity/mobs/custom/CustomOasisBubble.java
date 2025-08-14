package net.annova.object.entity.mobs.custom;

import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.UNKNOWN_SOURCE_WATER;
import static net.annova.object.person.advancement.RPGAdvancement.WATER;

public class CustomOasisBubble extends ArmorStand {
    public CustomOasisBubble(EntityType<? extends ArmorStand> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vec, @NotNull InteractionHand hand) {
        Inventory inventory = player.getInventory();
        ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!item.is(Items.GLASS_BOTTLE)) return InteractionResult.SUCCESS;

        RPGPerson person = getRPGPerson(player);
        RPGItem.subtractItem((ServerPlayer) player, item);
        player.addItem(UNKNOWN_SOURCE_WATER.create(person));
        player.playSound(SoundEvents.BOTTLE_FILL, 1, 1);
        person.completeAdvancement(WATER);
        return InteractionResult.SUCCESS;
    }
}
