package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.RPGActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.entity.core.RPGEntityType.SWAMP_SPIDER;
import static net.annova.object.item.base.Item.SPIDER_CHITIN;
import static net.annova.util.BukkitObjects.chance;

public class Melanox extends RPGEntity implements RPGActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.SPIDER;
    }

    @Override
    public String name() {
        return "Melanox";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public int health() {
        return 600;
    }

    @Override
    public double damage() {
        return 15;
    }

    @Override
    public float speed() {
        return 0.4f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(SPIDER_CHITIN.create(player, 0.01));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public boolean activity() {
        for (int i = 0; i < 4; i++) {
            SWAMP_SPIDER.getEntity().create(entity);
        }
        return true;
    }

    @Override
    public long repeating() {
        return 200;
    }

    @Override
    public boolean syncActivity(LivingEntity entity) {
        LivingEntity target = ((Creature) entity).getTarget();
        if (target == null) return true;

        if (chance(0.3)) entity.teleport(target);
        return true;
    }

    @Override
    public long syncRepeating() {
        return 60;
    }
}
