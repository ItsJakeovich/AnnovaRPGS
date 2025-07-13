package net.annova.object.entity.mobs;

import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.ValueType;
import net.annova.object.entity.mobs.custom.CustomSnake;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.entity.core.attribute.ability.RPGAbility.Type.POISON;
import static net.annova.util.BukkitObjects.random;

public class Snake extends RPGEntity implements CustomType {
    @Override
    public Class<?> clazz() {
        return CustomSnake.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.SILVERFISH;
    }

    @Override
    public String name() {
        return "Snake";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 20;
    }

    @Override
    public double damage() {
        return 8;
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
        return List.of(
            createItem(Items.RABBIT_HIDE, Component.literal("Змеиная кожа"), 1, random.nextInt(2), 0.2)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of(POISON.get().setValue(ValueType.DURATION, 100));
    }

    // Spawn in UpdateSpawnTimer
}
