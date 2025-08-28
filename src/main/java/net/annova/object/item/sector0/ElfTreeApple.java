package net.annova.object.item.sector0;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.ELF_TREE_APPLE;
import static net.annova.object.person.status.StatusType.ELF_APPLE_BONUS;

public class ElfTreeApple extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.APPLE;
    }

    @Override
    public List<Float> modelData() {
        return List.of(468f);
    }

    @Override
    public int stackSize() {
        return 64;
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
    public void consume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (ELF_TREE_APPLE.equals(item)) {
            getRPGPerson(event.getPlayer()).addStatus(ELF_APPLE_BONUS);
        }
    }
}
