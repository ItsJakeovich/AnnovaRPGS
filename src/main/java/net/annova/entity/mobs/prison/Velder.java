package net.annova.entity.mobs.prison;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGActivity;
import net.annova.entity.core.RPGEntity;
import net.annova.item.token.PrisonTokenItem;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.util.BukkitObjects.random;

public class Velder extends RPGEntity implements RPGActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Velder";
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
    public List<ItemStack> loot() {
        return List.of(
                createItem(Material.COOKED_BEEF, random.nextInt(4), 0.11),
                new PrisonTokenItem().create(random.nextInt(2), 0.11)
        );
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of();
    }

    @Override
    public boolean activity(LivingEntity entity) {
        LivingEntity target = ((Creature) entity).getTarget();
        if (target == null) return;
        Location from = entity.getEyeLocation(), to = target.getEyeLocation();
        if (!from.getWorld().equals(to.getWorld())) return;
        if (from.distanceSquared(to) < 5*5) return;

        // Создает стрелу, если в 5 блоках от цели
        Vector vector = to.toVector().subtract(from.toVector()).normalize();
        Arrow arrow = entity.getWorld().spawnArrow(from, vector, 0.7f, 0.1f);
        arrow.setDamage(damage());
    }

    @Override
    public long repeating() {
        return 50L;
    }

    @Override
    public boolean syncActivity(LivingEntity entity) {
    }

    @Override
    public long syncRepeating() {
        return 0;
    }
}
