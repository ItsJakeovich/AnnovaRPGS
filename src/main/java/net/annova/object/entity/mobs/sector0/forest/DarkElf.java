package net.annova.object.entity.mobs.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.RPGActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Particle;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.Item.DARKNESS_CONCENTRATION;
import static net.annova.object.item.base.Item.DARK_ELF_BOW;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class DarkElf extends RPGEntity implements RPGActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.BOGGED;
    }

    @Override
    public String name() {
        return "DarkElf";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public int health() {
        return 400;
    }

    @Override
    public double damage() {
        return 12;
    }

    @Override
    public float speed() {
        return 0.345f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(
                DARKNESS_CONCENTRATION.create(player, random.nextInt(4)+1, 0.05),
                DARK_ELF_BOW.create(player, 0.01)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    private final PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0);
    @Override
    public boolean activity() {
        ((Creature) entity).addPotionEffect(invisibility);
        return true;
    }

    @Override
    public long repeating() {
        return 400;
    }

    private final PotionEffect[] effects = new PotionEffect[] {
            new PotionEffect(PotionEffectType.BLINDNESS, 80, 0),
            new PotionEffect(PotionEffectType.WEAKNESS, 100, 0),
            new PotionEffect(PotionEffectType.POISON, 100, 0)
    };
    @Override
    public boolean syncActivity(LivingEntity entity) {
        LivingEntity target = ((Creature) entity).getTarget();
        if (target != null && chance(0.1)) {
            target.addPotionEffect(effects[random.nextInt(effects.length)]);
            target.getWorld().spawnParticle(Particle.WITCH, target.getEyeLocation(), 8, 0.6, 1.2, 0.6);
        }
        return true;
    }

    @Override
    public long syncRepeating() {
        return 50;
    }
}
