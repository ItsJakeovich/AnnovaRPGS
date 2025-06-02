package net.annova.object.entity.mobs.prison;

import net.annova.object.RPGRarity;
import net.annova.object.entity.ability.AbilityType;
import net.annova.object.entity.core.RPGActivity;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.item.base.Item.PRISON_TOKEN;
import static net.annova.object.entity.ability.AbilityType.ORATOR;
import static net.annova.object.entity.core.RPGEntityType.PRISONER;
import static net.annova.util.BukkitObjects.random;

public class Leader extends RPGEntity implements RPGActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Leader";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 250;
    }

    @Override
    public double damage() {
        return 12;
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
    public List<ItemStack> loot(Player player) {
        return List.of(
                createItem(Material.COOKED_BEEF, random.nextInt(4), 0.11),
                PRISON_TOKEN.create(player, random.nextInt(2), 0.11)
        );
    }

    @Override
    public Set<AbilityType> abilities() {
        return Set.of(
                ORATOR.setValue("damage_boost", 1.15)
                        .setValue("health_boost", 1.10)
                        .setValue("auto_regeneration_boost", 1)
        );
    }

    private final Set<LivingEntity> prisoners = new HashSet<>();
    @Override
    public boolean activity(LivingEntity entity) {
        RPGEntity summon = PRISONER.getEntity();

        prisoners.removeIf(ent -> !ent.isValid());
        if (prisoners.size() < 8) {
            for (int i = 4; i > 0; i--) {
                summon.create(entity.getLocation());
            }
        }
        return true;
    }

    @Override
    public long repeating() {
        return 1200;
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
