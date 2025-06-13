package net.annova.object.entity.mobs.sector0.dryad;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.RPGSpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.entity.core.RPGEntityType.FOREST_WOLF;
import static net.annova.object.item.base.Item.WOLF_SKIN;
import static net.annova.util.BukkitObjects.random;

public class ForestWolf extends RPGEntity implements RPGSpawn {
    @Override
    public EntityType<?> type() {
        return EntityType.WOLF;
    }

    @Override
    public String name() {
        return "ForestWolf";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 100;
    }

    @Override
    public double damage() {
        return 5;
    }

    @Override
    public float speed() {
        return 0.35f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(
                WOLF_SKIN.create(player, random.nextInt(2)+1, 0.05)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public void onSpawn(Location location) {
        // от 0 до 3 собратьев
        for (int i = random.nextInt(4); i != 0; i--) {
            FOREST_WOLF.getEntity().create(location);
        }
    }
}
