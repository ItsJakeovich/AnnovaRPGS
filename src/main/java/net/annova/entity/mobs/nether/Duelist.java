package net.annova.entity.mobs.nether;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.annova.entity.core.RPGEquipment;
import net.annova.item.component.NetherCatalystItem;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.util.BukkitObjects.chance;

public class Duelist extends RPGEntity implements RPGEquipment, Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.WITHER_SKELETON;
    }

    @Override
    public String name() {
        return ChatColor.BLACK + "Duelist";
    }

    @Override
    public int health() {
        return 750;
    }

    @Override
    public double damage() {
        return 40;
    }

    @Override
    public float speed() {
        return 0.25f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot() {
        return List.of(
                new NetherCatalystItem().create()
        );
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of(
                Ability.REFLECT.setValue("chance", 0.25)
        );
    }

    @Override
    public ItemStack[] equipment() {
        return new ItemStack[] {
            createItem(Material.IRON_SWORD, ChatColor.GRAY + "Стальная зубочистка"),
            createItem(Material.IRON_SWORD, ChatColor.GRAY + "Стальная зубочистка"),
            createItem(Material.CHAINMAIL_HELMET),
            createItem(Material.CHAINMAIL_CHESTPLATE),
            createItem(Material.CHAINMAIL_LEGGINGS),
            createItem(Material.CHAINMAIL_BOOTS)
        };
    }

    @EventHandler
    private void spawn(CreatureSpawnEvent event) {
        LivingEntity ent = event.getEntity();
        if (!(ent.getType().equals(org.bukkit.entity.EntityType.WITHER_SKELETON))) return;
        if (GlobalRPG.containEntity(ent)) return;
        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

        if (!chance(0.09)) return;
        this.create(ent.getLocation());
        event.setCancelled(true);
    }
}
