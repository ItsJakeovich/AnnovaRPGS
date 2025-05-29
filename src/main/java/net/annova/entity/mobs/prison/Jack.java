package net.annova.entity.mobs.prison;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.annova.item.token.PrisonTokenItem;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.util.BukkitObjects.random;

public class Jack extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return EntityType.VINDICATOR;
    }

    @Override
    public String name() {
        return "Jack";
    }

    @Override
    public int health() {
        return 550;
    }

    @Override
    public double damage() {
        return 30;
    }

    @Override
    public float speed() {
        return 0.2f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot() {
        return List.of(
                createItem(Material.COOKED_BEEF, random.nextInt(4), 0.11),
                new PrisonTokenItem().create(random.nextInt(2), 0.11)
        );
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of(
                Ability.VAMPIRISM.setValue("regen", 25)
        );
    }
}
