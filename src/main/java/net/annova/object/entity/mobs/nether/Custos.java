package net.annova.object.entity.mobs.nether;

import net.annova.object.RPGRarity;
import net.annova.object.entity.ability.AbilityType;
import net.annova.object.entity.core.RPGEntity;
import net.annova.event.custom.BattleCE;
import net.annova.event.custom.EntityDamageCE;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
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
        return "Custos";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
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
    public List<ItemStack> loot(Player player) {
        return List.of();
    }

    @Override
    public Set<AbilityType> abilities() {
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
    private void immortal(EntityDamageCE event) {
        RPGEntity rpg = GlobalRPG.getEntity(event.getEntity());
        if (rpg instanceof Custos) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    private void mechanic(BattleCE event) {
        RPGEntity rpg = GlobalRPG.getEntity(event.getEntity());
        if (rpg instanceof Custos) {
            LivingEntity damager = event.getDamager();
            damager.damage(event.getBaseDamage() * 1.5, damager);
            event.setCancelled(true);
        }
    }
}
