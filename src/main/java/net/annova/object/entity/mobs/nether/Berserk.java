package net.annova.object.entity.mobs.nether;

import net.annova.object.RPGRarity;
import net.annova.object.entity.ability.AbilityType;
import net.annova.object.entity.core.RPGEntity;
import net.annova.event.custom.BattleCE;
import net.annova.event.custom.EntityDamageCE;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.util.BukkitObjects.chance;

public class Berserk extends RPGEntity implements Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.BLAZE;
    }

    @Override
    public String name() {
        return "Berserk";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 35;
    }
    // Если здоровье ниже 10% восполняет себе здоровье на фулл. Взрывает. Все выстрелы берсерка взрывные. Работает один раз восполнение здоровья
    @Override
    public double damage() {
        return 16;
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
    public List<ItemStack> loot(Player player) {
        return List.of();
    }

    @Override
    public Set<AbilityType> abilities() {
        return Set.of();
    }

    private boolean regened = false;

    @EventHandler
    public void spawn(CreatureSpawnEvent event) {
        LivingEntity ent = event.getEntity();
        if (!(ent.getType().equals(org.bukkit.entity.EntityType.BLAZE))) return;
        if (GlobalRPG.containEntity(ent)) return;
        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

        if (!chance(0.09)) return;
        this.create(ent.getLocation());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void mechanic(BattleCE event) {
        LivingEntity damager = event.getDamager();
        RPGEntity rpg = GlobalRPG.getEntity(damager);
        if (rpg instanceof Berserk berserk) {
            if (berserk.regened) {
                damager.getWorld().createExplosion(event.getEntity().getLocation(), 1);
            }
        }
        LivingEntity entity = event.getEntity();
        rpg = GlobalRPG.getEntity(entity);
        if (!(rpg instanceof Berserk berserk)) return;

        if (event.getDamager() instanceof Projectile) {
            event.setCancelled(true);
            return;
        }

        // регенеративная способность
        if (!berserk.regened) {
            double maxHealth = entity.getAttribute(Attribute.MAX_HEALTH).getValue();
            if (entity.getHealth() - event.getDamage() < maxHealth / 10) { // health < 10%
                entity.setHealth(maxHealth);
                berserk.regened = true;
                entity.getWorld().createExplosion(entity.getLocation(), 3F, false, false);
            }
        }
    }
    @EventHandler
    public void defence(EntityDamageCE event) { // Защита от взрывов
        EntityDamageEvent.DamageCause cause = event.getEvent().getCause();
        switch (cause) {
            case BLOCK_EXPLOSION, ENTITY_EXPLOSION:
                return;
        }

        RPGEntity rpg = GlobalRPG.getEntity(event.getEntity());
        if (!(rpg instanceof Berserk)) return;
        event.setCancelled(true);
    }
}
