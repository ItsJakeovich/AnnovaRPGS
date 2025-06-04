package net.annova.object.entity.mobs.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.RPGActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static net.annova.util.BukkitObjects.chance;

public class OlderEnt extends RPGEntity implements RPGActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
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
        return 500;
    }

    @Override
    public double damage() {
        return 14;
    }

    @Override
    public float speed() {
        return 0;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of();
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    private Block lastBlock;
    private final static PotionEffect slowness = new PotionEffect(PotionEffectType.SLOWNESS, 30, 9);
    @Override
    public boolean activity() {
        if (lastBlock != null) {
            lastBlock.setType(Material.AIR);
        }
        LivingEntity target = ((Creature) entity).getTarget();
        if (target != null && chance(0.2)) {
            Block block = target.getEyeLocation().getBlock();
            if (block.getType().isAir()) {
                block.setType(Material.OAK_LEAVES);
                target.addPotionEffect(slowness);
                lastBlock = block;
            }
        }
        return true;
    }

    @Override
    public long repeating() {
        return 30;
    }

    @Override
    public boolean syncActivity(LivingEntity entity) {
        this.entity.heal(5);
        entity.getWorld().spawnParticle(Particle.TINTED_LEAVES,
                entity.getLocation().add(0, 1, 0),
                8, 0.6, 1.2, 0.6
        );
        return true;
    }

    @Override
    public long syncRepeating() {
        return 100;
    }
}
