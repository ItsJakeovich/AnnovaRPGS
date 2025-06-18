package net.annova.object.entity.mobs.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.mobs.custom.CustomEnt;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.ENT_ROOT;

public class Ent extends RPGEntity implements CustomType {
    @Override
    public Class<?> clazz() {
        return CustomEnt.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return ChatColor.DARK_GREEN + "Ent";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 160;
    }

    @Override
    public double damage() {
        return 7;
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
    public List<ItemStack> loot(Player player) {
        return List.of(ENT_ROOT.create(player, 0.01));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }
}
