package net.annova.object.entity.mobs.prison;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.magic.projectile.HarryBall;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.item.base.Item.PRISON_TOKEN;
import static net.annova.util.BukkitObjects.random;

public class Harry extends RPGEntity implements EntityActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.EVOKER;
    }

    @Override
    public String name() {
        return "Harry";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
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
    public boolean activity() {
        // если моб сфокусирован = выключает его передвижение.
        if (((Creature) entity).getTarget() == null) {
            entity.setSpeed(this.speed());
            return true;
        } else {
            entity.setSpeed(0);
        }

        Location from = ((Creature) entity).getEyeLocation().clone().add(0, -0.5, 0);
        from.add(from.getDirection().multiply(2.5));

        Vector direction = ((Creature) entity).getTarget().getEyeLocation().toVector().subtract(from.toVector()).normalize();
        new HarryBall((org.bukkit.entity.LivingEntity) entity.getBukkitEntity()).create(from.setDirection(direction));
        return true;
    }

    @Override
    public long repeating() {
        return 50;
    }

    @Override
    public boolean syncActivity(LivingEntity entity) {
        return true;
    }

    @Override
    public long syncRepeating() {
        return 0;
    }
}
