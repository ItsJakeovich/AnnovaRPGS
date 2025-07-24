package net.annova.event.summon;

import net.annova.event.custom.BattleCE;
import net.annova.event.custom.LivingEntityDamageByEntityCE;
import net.annova.event.custom.MobDamageCE;
import net.annova.event.custom.PlayerDamageCE;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.ArrayList;
import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.random;

public class EntityEvent implements Listener {
    @EventHandler(priority = EventPriority.LOWEST) // Отключение восстановления здоровья
    public void regain(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void damageCESummoner(org.bukkit.event.entity.EntityDamageEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Player player) {
            PlayerDamageCE ev = new PlayerDamageCE(((CraftPlayer) player).getHandle(), event);
            this.baseFunction(event, ev);
        } else if (event.getEntity() instanceof Mob entity) {
            MobDamageCE ev = new MobDamageCE(((CraftMob) entity).getHandle(), event);
            this.baseFunction(event, ev);
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void damageByEntitySummoner(EntityDamageByEntityEvent event) {
        Entity eventEntity = ((CraftEntity) event.getEntity()).getHandle();
        Entity eventDamager = ((CraftEntity) event.getDamager()).getHandle();

        if (eventEntity instanceof LivingEntity entity && eventDamager instanceof LivingEntity damager) {
            BattleCE ev = new BattleCE(entity, damager, event);
            Bukkit.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                event.setCancelled(true);
                return;
            }
            event.setDamage(ev.getDamage());
        } else if (eventEntity instanceof LivingEntity entity) {
            LivingEntityDamageByEntityCE ev = new LivingEntityDamageByEntityCE(entity, event);
            Bukkit.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                event.setCancelled(true);
                return;
            }
            event.setDamage(ev.getDamage());
        }
    }

    private void baseFunction(org.bukkit.event.entity.EntityDamageEvent event, MobDamageCE ev) {
        Bukkit.getPluginManager().callEvent(ev);
        event.setDamage(ev.getDamage());
        if (ev.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        event.setDamage(ev.getDamage()); // Это и для моба, и для игрока.
        double finalDamage = event.getFinalDamage();

        LivingEntity entity = ev.getEntity();
        if (ev.getEntity() instanceof Player player) { // для игрока
            event.setCancelled(true);
            if (player.invulnerableTime != 0) return;

            org.bukkit.entity.Entity bukkit = event.getDamageSource().getCausingEntity();
            Entity nms = bukkit == null ? null : ((CraftEntity) bukkit).getHandle();
            getRPGPerson(player).damageWithCooldown(nms,
                    (float) finalDamage
            );
        }
        this.spawnIndicator((ServerLevel) entity.level(), entity.position().add(0, entity.getEyeHeight(), 0), finalDamage);
    }

    public static List<ArmorStand> indicators = new ArrayList<>();

    private void spawnIndicator(ServerLevel level, Vec3 position, double damage) {
        if (damage < 1) return;
        position.add(random.nextFloat(2)-1, random.nextFloat(2)-1, random.nextFloat(2)-1);
        ArmorStand stand = new ArmorStand(EntityType.ARMOR_STAND, level);
        stand.setPos(position);
        stand.setMarker(true);
        stand.setInvisible(true);
        stand.setNoGravity(true);
        stand.setSmall(true);
        stand.setCustomNameVisible(true);
        stand.setCustomName(Component.literal(String.valueOf((double) Math.round(damage*100) / 100))
                .withStyle(Style.EMPTY.withColor(16733525).withBold(true))
        );
        level.tryAddFreshEntityWithPassengers(stand);

        indicators.add(stand);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
            stand.remove(Entity.RemovalReason.DISCARDED);
            indicators.remove(stand);
        }, 25L);
    }
}
