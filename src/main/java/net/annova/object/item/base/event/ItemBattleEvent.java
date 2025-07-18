package net.annova.object.item.base.event;

import net.annova.event.custom.BattleCE;
import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.attribute.ItemAction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.object.item.base.ItemModulit.MELANOX_POISON;
import static net.annova.object.item.base.ItemType.getItemType;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.NMSObjects.getArmorContents;
import static net.minecraft.world.effect.MobEffects.POISON;

public class ItemBattleEvent implements Listener {
    private static final MobEffectInstance POISON_4 = new MobEffectInstance(POISON, 80, 3);
    @EventHandler
    public void battleDamager(BattleCE event) {
        LivingEntity damager = event.getDamager();
        LivingEntity entity = event.getEntity();
        ItemStack item = damager.getItemInHand(InteractionHand.MAIN_HAND);
        ItemType itemType = getItemType(item);
        if (itemType != null && itemType.getItem() instanceof ItemAction action) {
            action.onAttack(damager, entity);
        }

        CustomData data = item.get(DataComponents.CUSTOM_DATA);
        if (data == null) return;
        CompoundTag tag = data.getUnsafe();
        if (MELANOX_POISON.hasModulit(tag)) {
            double chance = 1.0;
            for (ItemStack armor : getArmorContents(entity)) {
                if (armor.getItem() == Items.AIR) continue;
                chance-=0.2; // -20% за каждый предмет б
            }
            if (chance(chance)) entity.addEffect(POISON_4);
        }
    }
}
