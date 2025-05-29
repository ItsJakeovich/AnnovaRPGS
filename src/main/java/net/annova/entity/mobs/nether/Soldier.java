package net.annova.entity.mobs.nether;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.util.BukkitObjects.chance;

public class Soldier extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIFIED_PIGLIN;
    }

    @Override
    public String name() {
        return chance(0.01) ? ChatColor.RED + "Армеец Марик" : ChatColor.GRAY + "Армеец";
    }

    @Override
    public int health() {
        return 600;
    }

    @Override
    public double damage() {
        return 16;
    }

    @Override
    public float speed() {
        return 0.3f;
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
