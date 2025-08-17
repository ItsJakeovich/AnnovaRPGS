package net.annova.object.item.sector0.oasis;

import net.annova.object.RPGRarity;
import net.annova.object.entity.mobs.sector0.oasis.Crab;
import net.annova.event.custom.BattleCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.RPGTool;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.object.item.base.ItemType.TIDE_BLADE;
import static net.annova.main.GlobalRPG.getRPGEntity;

public class TideBlade extends RPGItem implements RPGTool, Listener {
    @Override
    public Item type() {
        return Items.IRON_SWORD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(412f);
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
    public boolean unusable() {
        return false;
    }

    @EventHandler
    public void attack(BattleCE event) {
        LivingEntity damager = event.getDamager();
        if (!TIDE_BLADE.equals(damager.getMainHandItem())) return;

        LivingEntity entity = event.getEntity();
        if (getRPGEntity(entity) instanceof Crab) {
            event.setDamage(event.getDamage() + 3*event.getStrengthMultiplier());
        }

        ServerLevel level = entity.level().getMinecraftWorld();
        if (!level.isRaining() && !level.isThundering()
                && !(entity.isInWater() || damager.isInWater())) return;

        event.setDamage(event.getDamage() + 1.5*event.getStrengthMultiplier());
    }

    @Override
    public double damage() {
        return 6.5;
    }

    @Override
    public double attackSpeed() {
        return 1.6;
    }
}
