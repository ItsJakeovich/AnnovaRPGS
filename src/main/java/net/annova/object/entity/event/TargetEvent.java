package net.annova.object.entity.event;

import net.annova.main.GlobalRPG;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.ability.ValueType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import static net.annova.util.BukkitObjects.chance;

public class TargetEvent implements Listener {
    @EventHandler
    public void target(EntityTargetEvent event) {
        RPGEntity rpg = GlobalRPG.getEntity(event.getEntity());
        if (rpg == null) return;
        if (rpg instanceof EntityAction action) {
            if (!action.onTarget(event.getTarget())) event.setCancelled(true);

//            for (RPGAbility ability : rpg.abilities()) {
//                if (!chance(ability.getValue(ValueType.CHANCE))) continue;
//                switch (ability.getType()) {
//                    case NEUTRAL -> {
//                        if (even.g)
//                    }
//                }
//            }
        }
    }
}
