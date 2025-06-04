package net.annova.object.entity.mobs.sector0.caves;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.RPGSpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.Item.MAGIC_SCRAP;

public class Miner extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return MAGIC_ARMOR;
    }

    @Override
    public String name() {
        return "Miner";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 150;
    }

    @Override
    public double damage() {
        return 8;
    }

    @Override
    public float speed() {
        return 0.25f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(MAGIC_SCRAP.create(0.01));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }
}
