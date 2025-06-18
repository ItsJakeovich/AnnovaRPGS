package net.annova.object.entity.mobs.sector0.mines;

import net.annova.object.CustomRunnable;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.MAGIC_SCRAP;
import static net.annova.util.BukkitObjects.random;

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
    public List<ItemStack> loot(Player player) {
        return List.of(MAGIC_SCRAP.create(0.01));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    private final PotionEffect[] effects = new PotionEffect[] {
            new PotionEffect(PotionEffectType.BLINDNESS, 100, 1),
            new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 20, 3),
            new PotionEffect(PotionEffectType.SLOWNESS, 60, 4),
            new PotionEffect(PotionEffectType.NAUSEA, 200, 1),
            new PotionEffect(PotionEffectType.POISON, 60, 3),
    };
    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        LivingEntity target = ((Creature) entity).getTarget();
                        if (target != null) {
                            target.addPotionEffect(effects[random.nextInt(effects.length)]);
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
