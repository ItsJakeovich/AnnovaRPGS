package net.annova.object.entity.mobs.nether;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.AbilityType;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.ValueType;
import net.minecraft.world.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class Custos extends RPGEntity {
    @Override
    public EntityType<?> type() {
        return EntityType.MAGMA_CUBE;
    }

    @Override
    public String name() {
        return "Custos";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 4;
    }

    @Override
    public double damage() {
        return 0;
    }

    @Override
    public float speed() {
        return 0.2f;
    }

    @Override
    public boolean small() {
        return true;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of();
    }

    @Override
    public Set<AbilityType> abilities() {
        return Set.of(AbilityType.IMMORTAL, AbilityType.SPIKES.setValue(ValueType.MAIN, 1.5));
    }

//    @EventHandler
//    public void spawn(CreatureSpawnEvent event) {
//        LivingEntity ent = event.getEntity();
//        if (!(ent.getType().equals(org.bukkit.entity.EntityType.MAGMA_CUBE))) return;
//        if (GlobalRPG.containEntity(ent)) return;
//        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
//
//        if (!chance(0.09)) return;
//        this.create(ent.getLocation());
//        event.setCancelled(true);
//    }
}
