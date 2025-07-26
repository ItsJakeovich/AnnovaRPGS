package net.annova.object.item.crystal;

import net.annova.event.custom.InteractCE;
import net.annova.object.RPGRarity;
import net.annova.object.person.status.Status;
import net.annova.event.custom.MobDamageCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.IOException;
import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.item.base.ItemType.RUNIC_DEFENCE_STONE;
import static net.annova.object.person.status.StatusType.SHIELD_CHARGE;
import static net.annova.object.person.status.ValueType.CHARGE;

public class RunicDefenceStoneItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(333f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @EventHandler
    private void interact(InteractCE event) {
        PlayerInteractEvent handle = event.getHandle();
        if (handle.getAction().name().charAt(0) != 'R') return;
        if (!handle.getHand().equals(EquipmentSlot.HAND)) return;

        ServerPlayer player = event.getPerson().getServerPlayer();
        if (!RUNIC_DEFENCE_STONE.equals(player.getMainHandItem())) return;

        RPGPerson person = getRPGPerson(player);
        StatusManager manager = person.getStatusManager();
        Status status = manager.getStatus(SHIELD_CHARGE);
        person.getPerson().send(getInstance(), "defence-stone-remain", "{charges}",
                String.valueOf(status == null ? 3 : status.getValue(CHARGE).intValue()));
    }

    @EventHandler
    private void damage(MobDamageCE event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!RUNIC_DEFENCE_STONE.hasItem(player)) return;

        RPGPerson person = getRPGPerson(player);
        StatusManager manager = person.getStatusManager();
        Status status = manager.getStatus(SHIELD_CHARGE);
        if (status != null) return;

        status = manager.addStatus(SHIELD_CHARGE).setValue(CHARGE, 3);

        double charges = status.getValue(CHARGE);
        if (event.getDamage() < 100 || charges < 1) return;

        charges--;
        event.setCancelled(true);
        status.setValue(CHARGE, charges);

        Vec3 position = player.getEyePosition();
        ServerLevel level = player.serverLevel();
        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, position.x, position.y, position.z, 10, 2, 1, 2, 0);
        level.sendParticles(ParticleTypes.EXPLOSION, position.x, position.y, position.z, 5, 2, 1, 2, 0.3);

        player.setDeltaMovement(player.getLookAngle().scale(-1));
        player.hurtMarked = true;
        person.getPerson().send(getInstance(), "defence-stone-charges", "{charges}", String.valueOf(charges));
    }
}
