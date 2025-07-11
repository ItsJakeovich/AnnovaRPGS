package net.annova.object.entity.mobs.sector0.dryad;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.WOLF_SKIN;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class DarkWolf extends RPGEntity implements EntityAction {
    @Override
    public EntityType<?> type() {
        return EntityType.WOLF;
    }

    @Override
    public String name() {
        return "DarkWolf";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 300;
    }

    @Override
    public double damage() {
        return 12;
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
        return List.of(
                WOLF_SKIN.create(player, random.nextInt(4)+1, 0.1)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public boolean onTarget(Entity target) {
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        if (!chance(0.1)) return true;
        Vector direction = target.getLocation().getDirection();
        target.setVelocity(direction.multiply(-1).setY(0.425).normalize().multiply(1.675));
        return true;
    }

    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
    }

    @Override
    public void onDeath() {

    }
}
