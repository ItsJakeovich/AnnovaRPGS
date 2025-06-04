package net.annova.object.entity.mobs.sector0.caves;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.RPGSpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.Item.MAGIC_SCRAP;

public class OreCollector extends RPGEntity implements EntityAction {
    @Override
    public EntityType<?> type() {
        return MAGIC_ARMOR;
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
        return 0;
    }

    @Override
    public double damage() {
        return 0;
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
        return List.of(MAGIC_SCRAP.create(player, 0.01));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public boolean onTarget(Entity target) {
        return false;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        return false;
    }

    @Override
    public boolean onDamage(Entity damager) {
        return false;
    }

    @Override
    public void onDeath() {

    }
}
