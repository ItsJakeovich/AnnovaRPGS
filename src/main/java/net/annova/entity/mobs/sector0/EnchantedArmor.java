package net.annova.entity.mobs.sector0;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class EnchantedArmor extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIFIED_PIGLIN;
    }

    @Override
    public String name() {
        return "EnchantedArmor";
    }

    @Override
    public int health() {
        return 0;
    }

    @Override
    public double damage() {
        return 0;
    }

    @Override
    public float speed() {
        return 0;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot() {
        return List.of();
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of();
    }
}
