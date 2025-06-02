package net.annova.object.entity.mobs.prison;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.AbilityType;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.item.base.Item.PRISON_TOKEN;
import static net.annova.util.BukkitObjects.random;
import static net.annova.object.entity.core.AbilityType.SCHIVARE;
import static net.annova.object.entity.core.AbilityType.LEVELLER;

public class Zogga extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Zogga";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 80;
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
        return true;
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
                SCHIVARE.setValue("chance", 0.5),
                LEVELLER.setValue("chance", 0.5).setValue("duration", 100)
        );
    }
}
