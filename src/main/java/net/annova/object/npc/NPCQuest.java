package net.annova.object.npc;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface NPCQuest {
    String questID();
    Collection<ItemStack> need();
    Collection<EntityType> target();

    Collection<ItemStack> reward();
}
