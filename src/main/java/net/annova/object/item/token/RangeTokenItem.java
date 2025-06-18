package net.annova.object.item.token;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.object.item.base.ItemType.RANGE_TOKEN;
import static net.annova.main.AnnovaRPG.getInstance;

public class RangeTokenItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(11f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fУвеличивает радиус взаимодействия с блоками"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getAction().name().charAt(0) != 'R') return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!RANGE_TOKEN.equals(item)) return;

        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nms = craftPlayer.getHandle();
        new BukkitRunnable() {
            private final AttributeInstance attribute = nms.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
            @Override
            public void run() {
                if (RANGE_TOKEN.has(player)) attribute.setBaseValue(7);
                else {
                    attribute.setBaseValue(4.5);
                    this.cancel();
                }
            }
        }.runTaskTimer(getInstance(), 0L, 1200L);
    }
}
