package net.annova.object.item.token;

import net.annova.event.custom.MouseClickCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import static net.annova.object.item.base.ItemType.SATURATION_TOKEN;

public class SaturationTokenItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(7F);
    }

    @Override
    public int stackSize() {
        return 99;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    @EventHandler
    private void use(MouseClickCE event) {
        if (event.getClickType().equals(MouseClickCE.ClickType.LEFT_CLICK)) return;
        Player player = event.getPlayer();
        if (!SATURATION_TOKEN.use(player, event.getItemId())) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 72000, 0));
    }
}
