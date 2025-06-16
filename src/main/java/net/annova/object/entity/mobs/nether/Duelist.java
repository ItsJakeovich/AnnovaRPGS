package net.annova.object.entity.mobs.nether;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityEquipment;
import net.annova.object.entity.core.attribute.ability.ValueType;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.object.entity.core.attribute.ability.RPGAbility.Type.REFLECT;

public class Duelist extends RPGEntity implements EntityEquipment, Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.WITHER_SKELETON;
    }

    @Override
    public String name() {
        return "Duelist";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
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
    public List<ItemStack> loot(Player player) {
        return List.of();
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of(
                REFLECT.get().setValue(ValueType.CHANCE, 0.25)
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

//    @EventHandler
//    private void spawn(CreatureSpawnEvent event) {
//        LivingEntity ent = event.getEntity();
//        if (!(ent.getType().equals(org.bukkit.entity.EntityType.WITHER_SKELETON))) return;
//        if (GlobalRPG.containEntity(ent)) return;
//        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
//
//        if (!chance(0.09)) return;
//        this.create(ent.getLocation());
//        event.setCancelled(true);
//    }
}
