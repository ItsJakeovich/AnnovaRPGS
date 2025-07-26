package net.annova.object.item;

import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.ItemAction;
import net.annova.object.item.base.attribute.RPGTool;
import net.annova.object.RPGRarity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class PrisonSteelSwordItem extends RPGItem implements ItemAction, RPGTool {
    @Override
    public Item type() {
        return Items.IRON_SWORD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(804f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public double damage() {
        return 12.5;
    }

    @Override
    public double attackSpeed() {
        return 1.6;
    }

    private static final MobEffectInstance BLINDNESS = new MobEffectInstance(MobEffects.BLINDNESS, 300, 0);
    @Override
    public void onAttack(LivingEntity entity, LivingEntity target) {
        target.addEffect(BLINDNESS);
    }
}
