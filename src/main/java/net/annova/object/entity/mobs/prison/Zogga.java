package net.annova.object.entity.mobs.prison;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.ValueType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.entity.core.attribute.ability.RPGAbility.Type.LEVELLER;
import static net.annova.object.entity.core.attribute.ability.RPGAbility.Type.SCHIVARE;
import static net.annova.object.item.base.ItemType.PRISON_TOKEN;
import static net.annova.util.BukkitObjects.random;

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
        return 120;
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
                createItem(Items.COOKED_BEEF, random.nextInt(4), 0.11),
                PRISON_TOKEN.create(player, random.nextInt(2), 0.11)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of(
                SCHIVARE.get().setValue(ValueType.CHANCE, 0.3),
                LEVELLER.get().setValue(ValueType.CHANCE, 0.5).setValue(ValueType.DURATION, 100)
        );
    }
}
