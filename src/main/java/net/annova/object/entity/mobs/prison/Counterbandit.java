package net.annova.object.entity.mobs.prison;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.item.base.ItemType.PRISON_TOKEN;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.CB_DAMAGE_DEBUFF;
import static net.annova.util.BukkitObjects.random;

public class Counterbandit extends RPGEntity implements EntityAction, Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Counterbandit";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 350;
    }

    @Override
    public double damage() {
        return 10;
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

    @Override
    public boolean onTarget(Entity target) {
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        if (target instanceof Player player) {
            getRPGPerson(player).getStatusManager().addStatus(CB_DAMAGE_DEBUFF);
        }
        return true;
    }

    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
    }

    @Override
    public void onDeath() {
        for (Player player : this.entity.level().getWorld().getPlayers()) {
            getRPGPerson(player).getStatusManager().removeStatus(CB_DAMAGE_DEBUFF);
        }
    }
}
