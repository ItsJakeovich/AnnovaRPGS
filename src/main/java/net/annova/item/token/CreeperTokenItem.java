package net.annova.item.token;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.List;

public final class CreeperTokenItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public String name() {
        return ChatColor.GREEN + "Жетон крипера";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{5};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fКриперы больше на вас не реагируют"
        );
    }

    @EventHandler
    private void mechanic(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player player)) return;
        if (!this.hasItem(player)) return;

        if (event.getEntity().getType().equals(EntityType.CREEPER))
            event.setCancelled(true);
    }
}
