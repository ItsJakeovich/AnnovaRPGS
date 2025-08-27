package net.annova.object.entity.mobs.sector0.mines;

import net.annova.object.CustomRunnable;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.ability.ValueType;
import net.annova.object.entity.mobs.custom.MinesPhantom;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class Phantom extends RPGEntity implements CustomType, EntityActivity {
    @Override
    public Class<?> clazz() {
        return MinesPhantom.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Phantom";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.ELITE;
    }

    @Override
    public int health() {
        return 30;
    }

    @Override
    public double damage() {
        return 8;
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
        return List.of();
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of(new RPGAbility(RPGAbility.Type.SCHIVARE).setValue(ValueType.CHANCE, 0.75));
    }

    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        LivingEntity target = entity.getTarget();
                        if (target == null) return true;
                        if (target instanceof Player player) {
                            getRPGPerson(player.getUUID()).addStatus(StatusType.PHANTOM_FEAR, entity);
                        }
                        new PhantomMinion().createWithTarget(Phantom.this, target);
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 100;
                    }
                }
        );
    }
}
