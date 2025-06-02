package net.annova.object.entity.mobs.nether;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.entity.Player;
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
        return chance(0.01) ? "Армеец Марик" : "Армеец";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
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
    public List<ItemStack> loot(Player player) {
        return List.of();
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }
}
