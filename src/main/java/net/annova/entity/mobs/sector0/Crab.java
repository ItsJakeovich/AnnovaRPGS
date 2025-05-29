package net.annova.entity.mobs.sector0;

import net.annova.entity.core.RPGEntity;
import net.annova.entity.traits.ability.Ability;
import net.minecraft.world.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.item.base.Item.CRAB_CLAW;
import static net.annova.util.BukkitObjects.random;

public class Crab extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return EntityType.CAVE_SPIDER;
    }

    @Override
    public String name() {
        return "Crab";
    }

    @Override
    public int health() {
        return 250;
    }

    @Override
    public double damage() {
        return 12;
    }

    @Override
    public float speed() {
        return 0.36f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot() {
        return List.of(CRAB_CLAW.create(random.nextInt(3), 0.125));
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of();
    }
}
