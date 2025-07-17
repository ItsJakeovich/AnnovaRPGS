package net.annova.object.entity.mobs.sector0.dryad;

import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntitySpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomForestWolf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.core.RPGEntityType.FOREST_WOLF;
import static net.annova.object.item.base.ItemType.WOLF_SKIN;
import static net.annova.util.BukkitObjects.random;

public class ForestWolf extends RPGEntity implements CustomType, EntitySpawn {
    @Override
    public Class<?> clazz() {
        return CustomForestWolf.class; // нужно будет дописать что он агрессивен при наличии мяса
    }

    @Override
    public EntityType<?> type() {
        return EntityType.WOLF;
    }

    @Override
    public String name() {
        return "ForestWolf";
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
        return 2;
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
                WOLF_SKIN.create(person, random.nextInt(2)+1, 0.15)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public void onSpawn(ServerLevel level, Vec3 position) {
        // от 0 до 3 собратьев
        for (int i = random.nextInt(4); i != 0; i--) {
            FOREST_WOLF.getEntity().create(location, true);
        }
    }
}
