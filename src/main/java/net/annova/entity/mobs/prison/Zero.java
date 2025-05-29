package net.annova.entity.mobs.prison;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGActivity;
import net.annova.entity.core.RPGEntity;
import net.annova.item.token.PrisonTokenItem;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
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
    public List<ItemStack> loot() {
        return List.of(
                createItem(Material.COOKED_BEEF, random.nextInt(4), 0.11),
                new PrisonTokenItem().create(random.nextInt(18)+3)
        );
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of(
                Ability.KNOCKBACK_RESISTANCE.setValue("resist", 1.00),
                Ability.VAMPIRISM.setValue("regen", 45),
                Ability.VITIATION.setValue("blindness_duration", 100).setValue("slowness_duration", 100).setValue("nausea_duration", 100)
        );
    }

    @Override
    public boolean activity(LivingEntity entity) {
        Creature creature = (Creature) entity;
        if (creature.getTarget() == null) return;
        creature.setTarget(null);
    }

    @Override
    public long repeating() {
        return 200;
    }

    @Override
    public boolean syncActivity(LivingEntity entity) {

    }

    @Override
    public long syncRepeating() {
        return 0;
    }
}
