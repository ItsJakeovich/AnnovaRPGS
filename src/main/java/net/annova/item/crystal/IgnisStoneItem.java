package net.annova.item.crystal;

import net.annova.entity.traits.status.Status;
import net.annova.event.custom.BattleCE;
import net.annova.event.custom.EntityDamageByEntityCE;
import net.annova.event.custom.EntityDamageCE;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.annova.item.base.Item.IGNIS_STONE;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.main.AnnovaRPG.getInstance;

public class IgnisStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.GOLD + "Огненный кристалл";
    }

    @Override
    public List<Float> modelData() {
        return List.of(303f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fНакладывает эффект &cогнестойкости",
                "&3= &fВесь исхо"
        );
    }

    @EventHandler
    private void drop(EntityDeathEvent event) {
        if (!event.getEntity().getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
        if (!chance(0.003)) return;

        event.getDrops().add(this.create());
    }
    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!IGNIS_STONE.equals(item)) return;

        removeItem(player, item);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0));
        RPGPerson person = getRPGPerson(player);
        Status status = person.getStatus(Status.Type.IGNIS);
        if (status != null) return;

        person.addStatus(Status.Type.IGNIS);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(),
                () -> person.removeStatus(Status.Type.IGNIS)
                , 1200);
    }

    private final Set<Entity> targets = new HashSet<>();

    @EventHandler
    private void damager(BattleCE event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (getRPGPerson(player).getStatus(Status.Type.IGNIS) == null) return;

        LivingEntity entity = event.getEntity();
        if (targets.contains(entity)) return;
        targets.add(entity);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(),
                () -> targets.remove(entity)
                , 1200);
    }
    @EventHandler
    private void damager(EntityDamageCE event) {
        if (!targets.contains(event.getEntity())) return;
        event.increaseDamage(10);
    }
}