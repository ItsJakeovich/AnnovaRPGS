package net.annova.object.entity.mobs;

import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomFrog;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

public class Frog extends RPGEntity implements CustomType {
    @Override
    public Class<?> clazz() {
        return CustomFrog.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.FROG;
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
        return 10;
    }

    @Override
    public double damage() {
        return 1;
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
    public List<ItemStack> loot(Person person) {
        return List.of();
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }
}
