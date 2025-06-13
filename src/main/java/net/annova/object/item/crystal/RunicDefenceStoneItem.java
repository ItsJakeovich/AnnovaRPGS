package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.person.status.Status;
import net.annova.event.custom.EntityDamageCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.item.base.Item.RUNIC_DEFENCE_STONE;
import static net.annova.object.person.status.StatusType.SHIELD_CHARGE;
import static net.annova.object.person.status.ValueType.CHARGE;

public class RunicDefenceStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
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
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3| &fЗащищает от урона свыше &6100 &fединиц",
                "&3| &fИмеет &63 заряда&7. &fВосстановление каждые &630 &fминут"
        );
    }

    @EventHandler
    private void interact(PlayerInteractEvent event) {
        if (event.getAction().name().charAt(0) != 'R') return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        Player player = event.getPlayer();
        if (!RUNIC_DEFENCE_STONE.equals(player.getInventory().getItemInMainHand())) return;
        RPGPerson person = getRPGPerson(player);
        StatusManager manager = person.getStatusManager();
        Status status = manager.getStatus(SHIELD_CHARGE);
        person.getPerson().send(getInstance(), "defence-stone-remain", "{charges}",
                String.valueOf(status == null ? 3 : status.getValue(CHARGE).intValue()));
    }

    @EventHandler
    private void damage(EntityDamageCE event) {
        LivingEntity entity = event.getEntity();
        if (!RUNIC_DEFENCE_STONE.hasPlayer(entity)) return;

        RPGPerson person = getRPGPerson((Player) entity);
        StatusManager manager = person.getStatusManager();
        Status status = manager.getStatus(SHIELD_CHARGE);
        if (status != null) return;

        status = manager.addStatus(SHIELD_CHARGE).setValue(CHARGE, 3);

        double charges = status.getValue(CHARGE);
        if (event.getDamage() < 100 || charges < 1) return;

        charges--;
        event.setCancelled(true);
        status.setValue(CHARGE, charges);

        Location loc = entity.getLocation();
        World world = loc.getWorld();

        world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 30);
        world.spawnParticle(Particle.EXPLOSION, loc, 10);
        entity.setVelocity(loc.getDirection().multiply(-1));
        person.getPerson().send(getInstance(), "defence-stone-charges", "{charges}", String.valueOf(charges));
    }
}
