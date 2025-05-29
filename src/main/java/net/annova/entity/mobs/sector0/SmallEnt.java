package net.annova.entity.mobs.sector0;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.annova.item.EntRoot;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.item.base.Item.ENT_ROOT;

public class SmallEnt extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return STR."\{ChatColor.DARK_GREEN}Ent";
    }

    @Override
    public int health() {
        return 250;
    }

    @Override
    public double damage() {
        return 6.7;
    }

    @Override
    public float speed() {
        return 0.315f;
    }

    @Override
    public boolean small() {
        return true;
    }

    @Override
    public List<ItemStack> loot() {
        return List.of(ENT_ROOT.create(0.03));
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of();
    }
}
