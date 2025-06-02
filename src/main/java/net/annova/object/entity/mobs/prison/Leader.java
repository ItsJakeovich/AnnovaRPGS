package net.annova.object.entity.mobs.prison;

import net.annova.main.GlobalRPG;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.RPGActivity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.status.StatusType;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.item.base.Item.PRISON_TOKEN;
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
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    private final Set<net.minecraft.world.entity.LivingEntity> prisoners = new HashSet<>();
    @Override
    public boolean activity() {
        RPGEntity summon = PRISONER.getEntity();
        prisoners.removeIf(ent -> ent.isAlive() && ent.valid && ent.isChunkLoaded() && ent.inWorld);
        if (prisoners.size() < 8) {
            for (int i = 4; i > 0; i--) {
                prisoners.add(summon.create(entity));
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
        for (Entity target : entity.getNearbyEntities(8, 4, 8)) {
            if (target instanceof Player) return true;
            if (target instanceof LivingEntity targ) {
                targ.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120, 0));

                healEntity(1, targ);

                RPGEntity rpgTarget = GlobalRPG.getEntity(targ);
                if (rpgTarget == null) return true;
                rpgTarget.addStatus(StatusType.LEADER_DAMAGE_BUFF);
            }
        }
        return true;
    }

    @Override
    public long syncRepeating() {
        return 100;
    }
}
