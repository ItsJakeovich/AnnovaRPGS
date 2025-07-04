package net.annova.object.item.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.ItemAction;
import net.annova.object.item.base.attribute.RPGTool;
import net.annova.object.magic.physic.EntRoots;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.entity.core.RPGEntityType.ENT;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class BladeImbuedWithMagic extends RPGItem implements RPGTool, ItemAction {
    @Override
    public Item type() {
        return Material.IRON_SWORD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(417f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
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
            if (chance(0.08)) {
                EntRoots.addPhysicDebuff(target);
            }
        } else { // дружественный энт
            if (chance(0.05)) {
                net.minecraft.world.entity.LivingEntity ent = ENT.getEntity().create(entity.getLocation());
                Mob mobEnt = (Mob) ent;
                net.minecraft.world.entity.LivingEntity craftTarget = ((CraftLivingEntity) target).getHandle();
                mobEnt.setTarget(craftTarget);
                new BukkitRunnable() {
                    int lifeTime = 10; // 1 = 0.5s | 10 = 5s

                    @Override
                    public void run() {
                        if (ent.isAlive() && ent.valid && lifeTime != 0) {
                            if (mobEnt.getTarget() != craftTarget) {
                                ent.remove(Entity.RemovalReason.DISCARDED);
                                this.cancel();
                            }
                            this.lifeTime--;
                        } else {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(getInstance(), 0L, 10L);
            }
        }
    }
}
