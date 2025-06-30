package net.annova.object.entity.mobs.prison;

import net.annova.object.CustomRunnable;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.item.base.ItemType.PRISON_TOKEN;
import static net.annova.util.BukkitObjects.random;

public class Velder extends RPGEntity implements EntityActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Velder";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 150;
    }

    @Override
    public double damage() {
        return 18;
    }

    @Override
    public float speed() {
        return 0.28f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(
                createItem(Material.COOKED_BEEF, random.nextInt(4), 0.11),
                PRISON_TOKEN.create(player, random.nextInt(2), 0.11)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        Creature creature = (Creature) entity.getBukkitEntity();
                        LivingEntity target = creature.getTarget();
                        if (target == null) return true;
                        Location from = creature.getEyeLocation(), to = target.getEyeLocation();
                        if (!from.getWorld().equals(to.getWorld())) return true;
                        if (from.distanceSquared(to) < 5*5) return true;

                        // Создает стрелу, если в 5 блоках от цели
                        Vector vector = to.toVector().subtract(from.toVector()).normalize();
                        Arrow arrow = entity.level().getWorld().spawnArrow(from, vector, 0.7f, 0.1f);
                        arrow.setDamage(Velder.this.damage());
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 50;
                    }
                }
        );
    }
}
