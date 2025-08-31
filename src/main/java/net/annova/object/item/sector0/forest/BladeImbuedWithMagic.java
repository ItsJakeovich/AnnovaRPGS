package net.annova.object.item.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.ItemAction;
import net.annova.object.item.base.attribute.RPGTool;
import net.annova.object.magic.physic.EntRoots;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.entity.core.RPGEntityType.ENT;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class BladeImbuedWithMagic extends RPGItem implements RPGTool, ItemAction {
    @Override
    public Item type() {
        return Items.IRON_SWORD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(417f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    @Override
    public double damage() {
        return 8.5;
    }

    @Override
    public double attackSpeed() {
        return 1.9;
    }

    @Override
    public void onAttack(LivingEntity entity, LivingEntity target) {
        if (random.nextBoolean()) { // корни
            if (chance(0.08)) EntRoots.addPhysicDebuff(target);
        } else { // дружественный энт
            if (chance(0.05)) ENT.getEntity().createWithTarget(entity, target, 1, 100);
        }
    }
}
