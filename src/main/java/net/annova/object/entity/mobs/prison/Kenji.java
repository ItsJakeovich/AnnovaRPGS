package net.annova.object.entity.mobs.prison;

import net.annova.object.RPGRarity;
import net.annova.object.entity.ability.AbilityType;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.item.base.Item.PRISON_TOKEN;
import static net.annova.object.entity.ability.AbilityType.AVOIDANCE;
import static net.annova.object.entity.ability.AbilityType.SLOWDOWN;
import static net.annova.util.BukkitObjects.random;

public class Kenji extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Kenji";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 300;
    }

    @Override
    public double damage() {
        return 26;
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
                createItem(Material.COOKED_BEEF, random.nextInt(4), 0.11),
                PRISON_TOKEN.create(player, random.nextInt(2), 0.11)
        );
    }

    @Override
    public Set<AbilityType> abilities() {
        return Set.of(
                SLOWDOWN.setValue("level", 1), // это второй уровень
                AVOIDANCE.setValue("radius", 6).setValue("duration", 160)
        );
    }
}
