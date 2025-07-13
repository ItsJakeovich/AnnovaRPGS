package net.annova.object.entity.mobs.nether;

import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.ValueType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.entity.core.attribute.ability.RPGAbility.Type.IMMORTAL;
import static net.annova.object.entity.core.attribute.ability.RPGAbility.Type.SPIKES;

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
    public List<ItemStack> loot(Person person) {
        return List.of();
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of(IMMORTAL.get(), SPIKES.get().setValue(ValueType.MAIN, 1.5));
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
