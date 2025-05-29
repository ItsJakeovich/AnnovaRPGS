package net.annova.entity.mobs.nether;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.annova.item.component.NetherCatalystItem;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.util.BukkitObjects.chance;

public class Custos extends RPGEntity implements Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.MAGMA_CUBE;
    }

    @Override
    public String name() {
        return ChatColor.GOLD + "Custos";
    }

    @Override
    public int health() {
        return 4;
    }

    @Override
    public double damage() {
        return 0;
    }

    @Override
    public float speed() {
        return 0.2f;
    }

    @Override
    public boolean small() {
        return true;
    }

    @Override
    public List<ItemStack> loot() {
        return List.of(
                new NetherCatalystItem().create()
        );
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of();
    }

    @EventHandler
    public void spawn(CreatureSpawnEvent event) {
        LivingEntity ent = event.getEntity();
        if (!(ent.getType().equals(org.bukkit.entity.EntityType.MAGMA_CUBE))) return;
        if (GlobalRPG.containEntity(ent)) return;
        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

        if (!chance(0.09)) return;
        this.create(ent.getLocation());
        event.setCancelled(true);
    }
    @EventHandler
    private void immortal(EntityDamageEvent event) {
        Entity ent = event.getEntity();
        RPGEntity rpg = GlobalRPG.getEntity(event.getEntity());
        if (rpg == null) return;
        if (rpg instanceof Custos) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    private void mechanic(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity damager)) return;
        Entity ent = event.getEntity();
        RPGEntity rpg = GlobalRPG.getEntity(event.getEntity());
        if (rpg == null) return;
        if (!(rpg instanceof Custos custos)) return;
        
        damager.damage(event.getDamage() * 1.5, ent);
        event.setCancelled(true);
    }
}
