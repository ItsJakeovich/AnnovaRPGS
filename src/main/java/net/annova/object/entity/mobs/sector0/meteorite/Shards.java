package net.annova.object.entity.mobs.sector0.meteorite;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomShards;
import net.minecraft.world.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.METEORITE_FRAGMENT;

public class Shards extends RPGEntity implements CustomType {
    @Override
    public Class<?> clazz() {
        return CustomShards.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.WITHER_SKELETON;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 60;
    }

    @Override
    public double damage() {
        return 6;
    }

    @Override
    public float speed() {
        return 0.345f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(METEORITE_FRAGMENT.create(player, 1, 0.01));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }
}
