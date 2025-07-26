package net.annova.object.entity.mobs.nether;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lex extends RPGEntity implements EntityAction {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIFIED_PIGLIN;
    }

    @Override
    public String name() {
        return "Lex";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 1000;
    }

    @Override
    public double damage() {
        return 24;
    }

    @Override
    public float speed() {
        return 0.3f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of();
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

//    @EventHandler
//    public void spawn(CreatureSpawnEvent event) {
//        if (!(event.getEntity() instanceof PigZombie zombie)) return;
//        if (GlobalRPG.containEntity(zombie)) return;
//        if (!event.getEntity().getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
//        World world = zombie.getWorld();
//        for (Entity ent : world.getNearbyEntities(zombie.getLocation(), 25, 20, 25)) {
//            String name = ent.getCustomName();
//            if (name == null) continue;
//            if (name.equals("§7Армеец")) return;
//        }
//
//        if (!chance(0.09)) return;
//        this.create(zombie.getLocation());
//        event.setCancelled(true);
//    }

    private boolean spawnedSoldiers = false;
    private final Set<Entity> soldiers = new HashSet<>();
    @Override
    public boolean onTarget(Entity target) {
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        return true;
    }

    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
        if (!this.spawnedSoldiers) {
            Soldier soldier = new Soldier();
            for (int i = 0; i < 8; i++) {
                soldiers.add(soldier.create(entity));
            }
            this.spawnedSoldiers = true;
        } else {
            soldiers.removeIf(check -> !check.isAlive() || !check.valid);
            event.setCancelled(!soldiers.isEmpty()); // if is not empty -> event cancelled.
        }
    }

    @Override
    public void onDeath() {

    }
}
