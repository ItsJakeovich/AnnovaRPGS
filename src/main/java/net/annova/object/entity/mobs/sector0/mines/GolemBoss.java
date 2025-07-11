package net.annova.object.entity.mobs.sector0.mines;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.MAGIC_SCRAP;

public class GolemBoss extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return EntityType.IRON_GOLEM;
    }

    @Override
    public String name() {
        return "GolemBoss";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public int health() {
        return 800;
    }

    @Override
    public double damage() {
        return 18;
    }

    @Override
    public float speed() {
        return 0.15f;
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
}
