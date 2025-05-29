package net.annova.entity.mobs.prison;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGActivity;
import net.annova.entity.core.RPGEntity;
import net.annova.entity.traits.magic.projectile.HarryBall;
import net.annova.item.token.PrisonTokenItem;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftLivingEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.util.BukkitObjects.random;

public class Harry extends RPGEntity implements RPGActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.EVOKER;
    }

    @Override
    public String name() {
        return "Harry";
    }

    @Override
    public int health() {
        return 350;
    }

    @Override
    public double damage() {
        return 10;
    }

    @Override
    public float speed() {
        return 0.2f;
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
        net.minecraft.world.entity.LivingEntity nms = ((CraftLivingEntity) entity).getHandle();
        // если моб сфокусирован = выключает его передвижение.
        if (((Creature) entity).getTarget() == null) {
            nms.setSpeed(speed());
            return;
        } else {
            nms.setSpeed(0);
        }

        Location from = entity.getEyeLocation().clone().add(0, -0.5, 0);
        from.add(from.getDirection().multiply(2.5));

        Vector direction = ((Creature) entity).getTarget().getEyeLocation().toVector().subtract(from.toVector()).normalize();
        new HarryBall(entity).create(from.setDirection(direction));
    }

    @Override
    public long repeating() {
        return 50;
    }

    @Override
    public boolean syncActivity(LivingEntity entity) {
    }

    @Override
    public long syncRepeating() {
        return 0;
    }
}
