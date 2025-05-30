package net.annova.entity.mobs.prison;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.annova.entity.traits.status.Status;
import net.annova.event.custom.BattleCE;
import net.annova.event.custom.RPGEntityDeathCE;
import net.annova.item.token.PrisonTokenItem;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.item.base.Item.PRISON_TOKEN;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.random;

public class Counterbandit extends RPGEntity implements Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Counterbandit";
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
    public List<ItemStack> loot() {
        return List.of(
                createItem(Material.COOKED_BEEF, random.nextInt(4), 0.11),
                PRISON_TOKEN.create(random.nextInt(2), 0.11)
        );
    }

    @EventHandler
    public void mechanic(BattleCE event) {
        if (!this.equals(GlobalRPG.getEntity(event.getDamager()))) return;
        if (event.getEntity() instanceof Player player) {
            getRPGPerson(player).addStatus(Status.Type.CB_DAMAGE_DEBUFF);
        }
    }
    @EventHandler
    public void mechanicDead(RPGEntityDeathCE event) {
        for (Player player : entity.level().getWorld().getPlayers()) {
            getRPGPerson(player).removeStatus(Status.Type.CB_DAMAGE_DEBUFF);
        }
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of();
    }
}
