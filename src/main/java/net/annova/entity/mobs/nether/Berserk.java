package net.annova.entity.mobs.nether;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.annova.item.component.NetherCatalystItem;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        return ChatColor.GOLD + "Berserk";
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
    public List<ItemStack> loot() {
        return List.of(
                new NetherCatalystItem().create()
        );
    }

    @Override
    public Set<Ability> abilities() {
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

    @EventHandler
    public void mechanic(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity damager)) return;

        RPGEntity rpg = GlobalRPG.getEntity(damager);
        if (rpg != null) {
            if (rpg instanceof Berserk berserk) {
                if (berserk.regened) {
                    damager.getWorld().createExplosion(event.getEntity().getLocation(), 1);
                }
            }
        }

        if (!(event.getEntity() instanceof LivingEntity ent)) return;

        rpg = GlobalRPG.getEntity(ent);
        if (rpg == null) return;
        if (!(rpg instanceof Berserk berserk)) return;

        if (event.getDamager() instanceof Projectile) {
            event.setCancelled(true);
            return;
        }

        // регенеративная способность
        if (!berserk.regened) {
            double maxHealth = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (ent.getHealth() - event.getFinalDamage() < maxHealth / 10) { // health < 10%
                ent.setHealth(maxHealth);
                berserk.regened = true;
                ent.getWorld().createExplosion(ent.getLocation(), 3F, false, false);
            }
        }
    }
    @EventHandler
    public void defence(EntityDamageEvent event) { // Защита от взрывов
        if (event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
                || event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))
            return;

        RPGEntity rpg = GlobalRPG.getEntity(event.getEntity());
        if (rpg == null) return;
        if (!(rpg instanceof Berserk berserk)) return;
        event.setCancelled(true);
    }
}
