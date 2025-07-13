package net.annova.object.entity.mobs.sector0.dryad;

import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

public class Fang extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return null;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public RPGRarity rarity() {
        return null;
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
    public List<ItemStack> loot(Person person) {
        return List.of();
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }
}
