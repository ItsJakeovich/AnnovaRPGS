package net.annova.item.crystal;

import net.annova.entity.traits.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.AnnovaRPG.getInstance;

public class RunicDefenceStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return "&7Рунический кристалл защиты";
    }

    @Override
    public List<Float> modelData() {
        return List.of(333f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fЗащищает от урона свыше &6100 &fединиц",
                "&3= &fИмеет &63 заряда&7. &fВосстановление каждые &630 &fминут"
        );
    }

    @EventHandler
    private void interact(PlayerInteractEvent event) {
        if (event.getAction().name().charAt(0) != 'R') return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        Player player = event.getPlayer();
        if (!equalItem(player.getInventory().getItemInMainHand())) return;
        RPGPerson person = getRPGPerson(player);
        Status status = person.getStatus(Status.Type.SHIELD_CHARGE);
        person.getPerson().send(getInstance(), "defence-stone-remain", "{charges}",
                String.valueOf(status == null ? 3 : status.getValue("charges").intValue()));
    }

    @EventHandler
    private void damage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!hasItem(entity)) return;

        RPGPerson person = getRPGPerson((Player) entity);
        Status status = person.getStatus(Status.Type.SHIELD_CHARGE);
        if (status == null) {
            status = person.addStatus(Status.Type.SHIELD_CHARGE).setValue("charges", 3);
            startChargeTimer(person);
        }

        double charges = status.getValue("charges");
        if (event.getDamage() < 100 || charges < 1) return;

        charges--;
        event.setCancelled(true);
        status.setValue("charges", charges);

        Location loc = entity.getLocation();
        World world = loc.getWorld();

        world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 30);
        world.spawnParticle(Particle.EXPLOSION, loc, 10);
        entity.setVelocity(loc.getDirection().multiply(-1));
        person.getPerson().send(getInstance(), "defence-stone-charges", "{charges}", String.valueOf(charges));
    }

    private void startChargeTimer(RPGPerson person) {
        new BukkitRunnable() {
            public void run() {
                Status status = person.getStatus(Status.Type.SHIELD_CHARGE);
                double charges = status.getValue("charge");
                if (charges == 3) return;
                status.setValue("charge", charges+1);
            }
        }.runTaskTimer(getInstance(), 0L, 18000);
    }
}
