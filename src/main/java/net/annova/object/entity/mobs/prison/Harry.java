package net.annova.object.entity.mobs.prison;

import net.annova.object.CustomRunnable;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.magic.projectile.HarryBall;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.item.base.ItemType.MAGIC_ORE;
import static net.annova.object.item.base.ItemType.PRISON_TOKEN;
import static net.annova.util.BukkitObjects.random;

public class Harry extends RPGEntity implements EntityActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.EVOKER;
    }

    @Override
    public String name() {
        return " ";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 200;
    }

    @Override
    public double damage() {
        return 7;
    }

    @Override
    public float speed() {
        return 0.2f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(
                createItem(Items.COOKED_BEEF, random.nextInt(4), 0.11),
                PRISON_TOKEN.create(person, random.nextInt(2), 0.11),
                MAGIC_ORE.create(person, random.nextInt(2)+1, 0.1)
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
                        Mob mob = entity;
                        LivingEntity target = mob.getTarget();
                        if (target == null) {
                            entity.setSpeed(Harry.this.speed());
                            return true;
                        } else {
                            entity.setSpeed(0);
                        }

                        new HarryBall(mob).create(mob.getEyePosition().add(0, -0.5, 0),
                                target.getEyePosition(),
                                mob.level().getMinecraftWorld()
                        );
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 50;
                    }
                }
        );
    }
}
