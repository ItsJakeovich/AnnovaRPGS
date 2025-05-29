package net.annova.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.sendPackets;

public class RPortalEvent implements Listener {
    public final Set<BlockState> portals = new HashSet<>();
    @EventHandler
    private void portal(PortalCreateEvent event) {
        event.setCancelled(true);
        List<BlockState> blocks = event.getBlocks();
        for (BlockState state : blocks) {
            if (state.getType().equals(Material.FIRE)) state.setType(Material.AIR);
        }
        BlockState id = blocks.getFirst();
        if (portals.contains(id)) return;
        portals.add(id);
        BlockState state = blocks.get(13);
        double blockX = state.getX();
        double blockZ = state.getZ();
        if (blockX == blocks.get(14).getX()) blockX+=0.5;
        else blockZ+=0.5;

        final double x = blockX;
        final double y = state.getY() + 1;
        final double z = blockZ;
        new BukkitRunnable() {
            private int i = 0;
            private final Location location = state.getLocation();
            private final World world = event.getWorld();
            @Override
            public void run() {
                if (i == 4) {
                    world.playSound(location, Sound.BLOCK_PORTAL_AMBIENT, 4, 4);
                    sendPackets(world, new ClientboundLevelParticlesPacket(ParticleTypes.PORTAL, true, false,
                            x, y+0.5, z,
                            0f, 0f, 0f, 4f, 1024)
                    );
                }

                if (i == 7) {
                    world.playSound(location, Sound.ENTITY_TNT_PRIMED, 4, 4);
                    sendPackets(world, new ClientboundLevelParticlesPacket(ParticleTypes.DRAGON_BREATH, true, false,
                            x, y+0.5, z,
                            0.4f, 0.4f, 0.4f, 0f, 1000));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () ->
                            sendPackets(world, new ClientboundLevelParticlesPacket(ParticleTypes.DRAGON_BREATH, true, false,
                            x, y+0.5, z,
                            0.8f, 0.8f, 0.8f, 0f, 500))
                            , 4);
                    portals.remove(id);
                    this.cancel();
                }
                if (i < 3) {
                    world.playSound(location, Sound.BLOCK_PORTAL_TRIGGER, 4, 4);
                    for (BlockState state : event.getBlocks()) {
                        Block block = state.getBlock();
                        if (block.getType() != Material.OBSIDIAN) block.setType(Material.AIR);
                        sendPackets(world, new ClientboundLevelParticlesPacket(ParticleTypes.PORTAL, true, false,
                                x, y, z,
                                0.05f, 0.85f, 0.55f, 0f, 48)
                        );
                    }
                }
                i++;
            }
        }.runTaskTimer(getInstance(), 0L, 20L);
    }
}
