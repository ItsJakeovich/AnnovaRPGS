package net.annova.object.entity.mobs.prison;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.AbilityType;
import net.annova.object.entity.core.RPGActivity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.entity.core.RPGSpawn;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.item.base.Item.PRISON_TOKEN;
import static net.annova.util.BukkitObjects.random;

public class Major extends RPGEntity implements RPGSpawn, RPGActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.HUSK;
    }

    @Override
    public String name() {
        return "Major";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 1500;
    }

    @Override
    public double damage() {
        return 24;
    }

    @Override
    public float speed() {
        return 0.24f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(
                createItem(Material.COOKED_BEEF, random.nextInt(4), 0.11),
                PRISON_TOKEN.create(player, random.nextInt(18)+3)
        );
    }

    @Override
    public Set<AbilityType> abilities() {
        return Set.of(
                AbilityType.REFLECT.setValue("chance", 0.2)
        );
    }

    @Override
    public void onSpawn(net.minecraft.world.entity.LivingEntity entity, Location location) {
        for (int i = 6; i > 0; i--) {
            RPGEntityType.SECURITY.getEntity().create(location);
        }
    }

    @Override
    public boolean activity() {
        for (Entity ent : entity.getBukkitEntity().getNearbyEntities(10, 5, 10)) {
            if (!(ent instanceof Player player)) continue;
            player.setFreezeTicks(400);
        }
        return true;
    }

    @Override
    public long repeating() {
        return 200;
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
