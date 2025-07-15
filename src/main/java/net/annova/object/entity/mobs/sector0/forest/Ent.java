package net.annova.object.entity.mobs.sector0.forest;

import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.mobs.custom.CustomEnt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.ENT_ROOT;
import static net.annova.util.BukkitObjects.random;

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
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 40;
    }

    @Override
    public double damage() {
        return 3;
    }

    @Override
    public float speed() {
        return 0.33f;
    }

    @Override
    public boolean small() {
        return true;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(ENT_ROOT.create(person, random.nextInt(2)+1, 0.055));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }
}
