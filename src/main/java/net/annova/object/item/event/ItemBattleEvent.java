package net.annova.object.item.event;

import net.annova.event.custom.BattleCE;
import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.attribute.ItemAction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.object.item.base.ItemType.getItemType;

public class ItemBattleEvent implements Listener {
    @EventHandler
    public void battleDamager(BattleCE event) {
        LivingEntity damager = event.getDamager();
        ItemType itemType = getItemType(damager.getEquipment().getItemInMainHand());
        if (itemType != null && itemType.getItem() instanceof ItemAction action) {
            action.onAttack(damager, event.getEntity());
        }
    }
}
