package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.object.CustomRunnable;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.magic.projectile.WebBall;
import net.annova.object.person.status.StatusType;
import net.minecraft.world.entity.EntityType;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.SPIDER_CHITIN;
import static net.annova.util.BukkitObjects.chance;

public class WhipSpider extends RPGEntity implements EntityAction, EntityActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.CAVE_SPIDER;
    }

    @Override
    public String name() {
        return "WhipSpider";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 90;
    }

    @Override
    public double damage() {
        return 5;
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
        return List.of(SPIDER_CHITIN.create(player, 0.01));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public boolean onTarget(Entity target) {
        new WebBall().create(entity, target);
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        if (chance(0.3)) {
            if (target instanceof Player player) {
                getRPGPerson(player).getStatusManager().addStatus(StatusType.PARALYSIS);
            }
        }
        return true;
    }

    @Override
    public boolean onDamage(Entity damager) {
        return true;
    }

    @Override
    public void onDeath() {
    }

    @Override
    public List<CustomRunnable> runnables() {
        return List.of();
    }
}
