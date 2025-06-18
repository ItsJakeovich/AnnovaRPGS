package net.annova.object.entity.mobs.sector0.swamps;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.entity.core.RPGEntityType.SWAMP_SPIDER;
import static net.annova.object.item.base.ItemType.SPIDER_CHITIN;
import static net.annova.util.BukkitObjects.chance;

public class SwampSpider extends RPGEntity implements EntityAction {
    @Override
    public EntityType<?> type() { // Вылезает из под воды - спавнить под водой.
        return EntityType.CAVE_SPIDER;
    }

    @Override
    public String name() {
        return "SwampSpider";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 120;
    }

    @Override
    public double damage() {
        return 6;
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
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        return true;
    }

    @Override
    public boolean onDamage(Entity damager) {
        return true;
    }

    @Override
    public void onDeath() {
        if (chance(0.25)) {
            SWAMP_SPIDER.getEntity().create(entity);
        }
    }
}
