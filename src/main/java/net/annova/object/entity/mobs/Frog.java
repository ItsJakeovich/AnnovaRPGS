package net.annova.object.entity.mobs;

import net.annova.object.CustomRunnable;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomFrog;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Set;

public class Frog extends RPGEntity implements CustomType, EntityActivity {
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

    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        entity.drop(new ItemStack(Items.SLIME_BALL), true, true);
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 3000;
                    }
                }
        );
    }
}
