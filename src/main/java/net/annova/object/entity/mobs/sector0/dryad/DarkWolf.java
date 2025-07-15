package net.annova.object.entity.mobs.sector0.dryad;

import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomDarkWolf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.DARK_WOLF_FANG;
import static net.annova.object.item.base.ItemType.WOLF_SKIN;
import static net.annova.util.BukkitObjects.random;

public class DarkWolf extends RPGEntity implements CustomType {
    @Override
    public Class<?> clazz() {
        return CustomDarkWolf.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.WOLF;
    }

    @Override
    public String name() {
        return "DarkWolf";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 50;
    }

    @Override
    public double damage() {
        return 4;
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
    public List<ItemStack> loot(Person person) {
        return List.of(
                WOLF_SKIN.create(person, random.nextInt(4)+1, 0.1),
                DARK_WOLF_FANG.create(person, 0.04)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }
}
