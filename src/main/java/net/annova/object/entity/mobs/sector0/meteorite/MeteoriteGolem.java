package net.annova.object.entity.mobs.sector0.meteorite;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.Item.METEORITE_FRAGMENT;

public class MeteoriteGolem extends RPGEntity implements EntityActivity, Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.IRON_GOLEM;
    }

    @Override
    public String name() {
        return " ";
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
        return 15;
    }

    @Override
    public float speed() {
        return 0.285f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(METEORITE_FRAGMENT.create(player));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public boolean activity() {
        return false;
    }

    @Override
    public long repeating() {
        return 300;
    }

    @Override
    public boolean syncActivity(LivingEntity entity) {
        return false;
    }

    @Override
    public long syncRepeating() {
        return 0;
    }
}
