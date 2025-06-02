package net.annova.object.entity.mobs.prison;

import net.annova.object.RPGRarity;
import net.annova.object.entity.ability.AbilityType;
import net.annova.object.entity.core.RPGActivity;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.item.base.Item.PRISON_TOKEN;
import static net.annova.object.entity.ability.AbilityType.*;
import static net.annova.util.BukkitObjects.random;

public class Zero extends RPGEntity implements RPGActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.VINDICATOR;
    }

    @Override
    public String name() {
        return "Zero";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 950;
    }

    @Override
    public double damage() {
        return 20;
    }

    @Override
    public float speed() {
        return 0.24f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(
                createItem(Material.COOKED_BEEF, random.nextInt(4), 0.11),
                PRISON_TOKEN.create(player, random.nextInt(18)+3)
        );
    }

    @Override
    public Set<AbilityType> abilities() {
        return Set.of(
                KNOCKBACK_RESISTANCE.setValue("resist", 1.00),
                VAMPIRISM.setValue("regen", 45),
                VITIATION.setValue("blindness_duration", 100).setValue("slowness_duration", 100).setValue("nausea_duration", 100)
        );
    }

    @Override
    public boolean activity(LivingEntity entity) {
        Creature creature = (Creature) entity;
        if (creature.getTarget() == null) return true;
        creature.setTarget(null);
        return true;
    }

    @Override
    public long repeating() {
        return 200;
    }

    @Override
    public boolean syncActivity(LivingEntity entity) {
        return true;
    }

    @Override
    public long syncRepeating() {
        return 0;
    }
}
