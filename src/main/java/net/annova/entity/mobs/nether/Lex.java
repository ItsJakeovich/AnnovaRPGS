package net.annova.entity.mobs.nether;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.annova.event.custom.EntityDamageCE;
import net.annova.item.component.NetherCatalystItem;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.annova.util.BukkitObjects.chance;

public class Lex extends RPGEntity implements Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIFIED_PIGLIN;
    }

    @Override
    public String name() {
        return ChatColor.RED + "Lex";
    }

    @Override
    public int health() {
        return 1000;
    }

    @Override
    public double damage() {
        return 24;
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
        if (!(event.getEntity() instanceof PigZombie zombie)) return;
        if (GlobalRPG.containEntity(zombie)) return;
        if (!event.getEntity().getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
        World world = zombie.getWorld();
        for (Entity ent : world.getNearbyEntities(zombie.getLocation(), 25, 20, 25)) {
            String name = ent.getCustomName();
            if (name == null) continue;
            if (name.equals("§7Армеец")) return;
        }

        if (!chance(0.09)) return;
        this.create(zombie.getLocation());
        event.setCancelled(true);
    }

    private boolean spawnedSoldiers = false;
    private final Set<Entity> soldiers = new HashSet<>();
    @EventHandler
    public void hit(EntityDamageCE event) {
        Entity ent = event.getEntity();
        RPGEntity rpg = GlobalRPG.getEntity(event.getEntity());
        if (!(rpg instanceof Lex lex)) return;
        if (!lex.spawnedSoldiers) {
            Soldier soldier = new Soldier();
            for (int i = 0; i < 8; i++) {
                soldiers.add(soldier.create(ent.getLocation()).getBukkitEntity());
            }
            lex.spawnedSoldiers = true;
        } else {
            soldiers.removeIf(check -> !check.isValid());
            event.setDamage(event.getDamage() * (1 - 0.1 * soldiers.size()));
        }
    }
}
