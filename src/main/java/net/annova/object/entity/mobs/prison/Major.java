package net.annova.object.entity.mobs.prison;

import net.annova.object.CustomRunnable;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.entity.core.attribute.EntitySpawn;
import net.annova.object.entity.core.attribute.ability.ValueType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.entity.core.attribute.ability.RPGAbility.Type.REFLECT;
import static net.annova.object.item.base.ItemType.PRISON_TOKEN;
import static net.annova.util.BukkitObjects.random;

public class Major extends RPGEntity implements EntitySpawn, EntityActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.HUSK;
    }

    @Override
    public String name() {
        return "Major";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 1500;
    }

    @Override
    public double damage() {
        return 24;
    }

    @Override
    public float speed() {
        return 0.24f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(
                createItem(Items.COOKED_BEEF, random.nextInt(4), 0.11),
                PRISON_TOKEN.create(person, random.nextInt(18)+3)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of(
                REFLECT.get().setValue(ValueType.CHANCE, 0.2)
        );
    }

    @Override
    public void onSpawn(ServerLevel level, Vec3 position) {
        for (int i = 6; i > 0; i--) {
            RPGEntityType.SECURITY.getEntity().create(position, level);
        }
    }

    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        for (Entity ent : entity.getBukkitEntity().getNearbyEntities(10, 5, 10)) {
                            if (!(ent instanceof Player player)) continue;
                            player.setFreezeTicks(400);
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
