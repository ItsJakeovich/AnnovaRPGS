package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.object.entity.mobs.custom.CustomSmallSpider;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomSwampSpider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.SPIDER_CHITIN;

public class SmallSpider extends RPGEntity implements CustomType {
    @Override
    public Class<?> clazz() {
        return CustomSmallSpider.class;
    }
    @Override
    public EntityType<?> type() { // Вылезает из под воды - спавнить под водой.
        return EntityType.SILVERFISH;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 5;
    }

    @Override
    public double damage() {
        return 1;
    }

    @Override
    public float speed() {
        return 0.405f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(SPIDER_CHITIN.create(person, 0.15));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }
}
