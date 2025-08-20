package net.annova.object.entity.event;

import net.annova.main.GlobalRPG;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.person.RPGPerson;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.equipment.ArmorSet.CHITIN;

public class TargetEvent implements Listener {
    @EventHandler
    public void target(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        Entity ent = event.getTarget();
        if (ent == null) return;
        net.minecraft.world.entity.Entity target = ((CraftEntity) ent).getHandle();
        if (target instanceof Player player) {
            RPGPerson person = getRPGPerson(player);
            EntityType type = entity.getType();
            if ((type == EntityType.SPIDER || type == EntityType.CAVE_SPIDER)
                    && CHITIN.equals(person.getArmorSet())) event.setCancelled(true);
        }

        RPGEntity rpg = GlobalRPG.getRPGEntity(entity);
        if (rpg == null) return;
        if (rpg instanceof EntityAction action) {
            if (action.onTarget(target)) return;
            event.setCancelled(true);
        }
    }
}
