package net.annova.object.entity.mobs.sector0.mines;

import net.annova.object.CustomRunnable;
import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.MAGIC_SCRAP;
import static net.annova.util.BukkitObjects.random;
import static net.minecraft.world.effect.MobEffects.*;

public class Mage extends RPGEntity implements EntityActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Mage";
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
        return 4;
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
        return List.of(MAGIC_SCRAP.create(0.01));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    private final MobEffectInstance[] effects = new MobEffectInstance[] {
            new MobEffectInstance(BLINDNESS, 100, 1),
            new MobEffectInstance(INSTANT_DAMAGE, 20, 3),
            new MobEffectInstance(SLOWNESS, 60, 4),
            new MobEffectInstance(NAUSEA, 200, 1),
            new MobEffectInstance(POISON, 60, 3),
    };
    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        LivingEntity target = ((Mob) entity).getTarget();
                        if (target != null) {
                            target.addEffect(effects[random.nextInt(effects.length)]);
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 200;
                    }
                }
        );
    }
}
