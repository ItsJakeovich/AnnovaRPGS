package net.annova.object.entity.mobs.sector0.meteorite;

import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomShards;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Set;

import static net.annova.main.AnnovaAPI.random;
import static net.annova.object.item.base.ItemType.HOT_STONE;

public class Shards extends RPGEntity implements CustomType {
    @Override
    public Class<?> clazz() {
        return CustomShards.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.BLAZE;
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
        return 100;
    }

    @Override
    public double damage() {
        return 6;
    }

    @Override
    public float speed() {
        return 0.315f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(
                HOT_STONE.create(person, 1, 0.04),
                new ItemStack(Items.BLAZE_ROD, random.nextInt(2)+1)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }
}
