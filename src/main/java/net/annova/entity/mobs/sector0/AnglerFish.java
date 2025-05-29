package net.annova.entity.mobs.sector0;

import net.annova.entity.core.RPGActivity;
import net.annova.entity.core.RPGEntity;
import net.annova.entity.traits.ability.Ability;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.util.BukkitObjects.*;

public class AnglerFish extends RPGEntity implements RPGActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.SALMON;
    }

    @Override
    public String name() {
        return "AnglerFish";
    }

    @Override
    public int health() {
        return 7;
    }

    @Override
    public double damage() {
        return 0;
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
    public List<ItemStack> loot() {
        return List.of();
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of();
    }


    private final Material[] FISH_ITEMS = {
            Material.SALMON, Material.TROPICAL_FISH, Material.PUFFERFISH, Material.COD
    };
    private final Material[] TRASH_ITEMS = {
            Material.STICK, Material.GLASS_BOTTLE, Material.LEATHER, Material.STRING
    };
    @Override
    public boolean activity(LivingEntity entity) {
        if (chance(0.05)) return false;
        entity.getWorld().dropItemNaturally(entity.getLocation()
                , new ItemStack((Material) (chance(0.2) ? getRandom(TRASH_ITEMS) : getRandom(FISH_ITEMS)) // material
                        , chance(0.05) ? 2 : 1) // amount
        );
        return true;
    }
    @Override
    public long repeating() {
        return 6000;
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
