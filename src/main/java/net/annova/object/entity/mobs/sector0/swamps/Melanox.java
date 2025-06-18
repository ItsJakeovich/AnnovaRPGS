package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.object.CustomRunnable;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.entity.core.RPGEntityType.SWAMP_SPIDER;
import static net.annova.object.item.base.ItemType.SPIDER_CHITIN;
import static net.annova.util.BukkitObjects.chance;

public class Melanox extends RPGEntity implements EntityActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.SPIDER;
    }

    @Override
    public String name() {
        return "Melanox";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public int health() {
        return 600;
    }

    @Override
    public double damage() {
        return 15;
    }

    @Override
    public float speed() {
        return 0.4f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(SPIDER_CHITIN.create(player, 0.01));
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
                        for (int i = 0; i < 4; i++) {
                            SWAMP_SPIDER.getEntity().create(entity);
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 200;
                    }
                },
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        LivingEntity target = ((Mob) entity).getTarget();
                        if (target == null) return true;

                        Vec3 vec3 = target.position();
                        if (chance(0.3)) entity.teleportTo(vec3.x, vec3.y, vec3.z);
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 60;
                    }
                }
        );
    }
}
