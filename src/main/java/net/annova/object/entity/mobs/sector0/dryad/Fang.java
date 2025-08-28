package net.annova.object.entity.mobs.sector0.dryad;

import net.annova.object.CustomRunnable;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.CustomFang;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.main.AnnovaAPI.random;
import static net.annova.object.item.base.ItemType.DARK_WOLF_FANG;
import static net.annova.object.item.base.ItemType.WOLF_SKIN;

public class Fang extends RPGEntity implements CustomType, EntityActivity {
    @Override
    public Class<?> clazz() {
        return CustomFang.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.WOLF;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public int health() {
        return 115;
    }

    @Override
    public double damage() {
        return 8;
    }

    @Override
    public float speed() {
        return 0.375f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(
                WOLF_SKIN.create(person, random.nextInt(3)+4),
                DARK_WOLF_FANG.create(person, random.nextInt(2)+1, 0.11)
        );
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
                        LivingEntity target = entity.getTarget();
                        if (target == null) return true;
                        if (random.nextBoolean()) {
                            new DarkWolf().createWithTarget(Fang.this, target);
                        } else {
                            for (int i = 0; i < 2; i++) {
                                new ForestWolf().createWithTarget(Fang.this, target);
                            }
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 1200;
                    }
                }
        );
    }
}
