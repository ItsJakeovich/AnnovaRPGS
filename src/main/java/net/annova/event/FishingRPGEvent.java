package net.annova.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.EnumSet;

import static net.annova.environment.dungeon.Loot.createArrow;
import static net.annova.main.AnnovaAPI.random;

public class FishingRPGEvent implements Listener {
    private static final ItemStack[] REPLACE = new ItemStack[] {
            createArrow(1, new MobEffectInstance(MobEffects.INSTANT_HEALTH, 0, 2)),
            createArrow(1, new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 0, 2)),
            createArrow(1, new MobEffectInstance(MobEffects.POISON, 0, 2)),
            createArrow(1, new MobEffectInstance(MobEffects.JUMP_BOOST, 0, 2)),
            createArrow(1, new MobEffectInstance(MobEffects.SPEED, 0, 2)),
            createArrow(1, new MobEffectInstance(MobEffects.STRENGTH, 0, 2)),
            createArrow(1, new MobEffectInstance(MobEffects.LEVITATION, 0, 2)),
            createArrow(1, new MobEffectInstance(MobEffects.REGENERATION, 0, 2)),
            createArrow(1, new MobEffectInstance(MobEffects.SLOWNESS, 0, 2)),
            createArrow(1, new MobEffectInstance(MobEffects.BLINDNESS, 0, 2)),
            new ItemStack(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE),
            new ItemStack(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE),
    };
    private static final EnumSet<Material> TO_REPLACE = EnumSet.of(
            Material.FISHING_ROD,
            Material.BOW,
            Material.ENCHANTED_BOOK,
            Material.LEATHER_BOOTS
    );


    @EventHandler(priority = EventPriority.HIGH)
    public void fish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            event.setCancelled(true);
            return;
        }
        CraftItem item = (CraftItem) event.getCaught();
        if (item == null) return;

        if (TO_REPLACE.contains(item.getItemStack().getType()))
            item.getHandle().setItem(REPLACE[random.nextInt(REPLACE.length)]);
    }
}
