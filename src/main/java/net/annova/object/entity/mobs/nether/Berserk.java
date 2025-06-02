package net.annova.object.entity.mobs.nether;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.AbilityType;
import net.annova.object.entity.core.EntityAction;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R4.util.CraftLocation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class Berserk extends RPGEntity implements EntityAction {
    @Override
    public EntityType<?> type() {
        return EntityType.BLAZE;
    }

    @Override
    public String name() {
        return "Berserk";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 35;
    }
    // Если здоровье ниже 10% восполняет себе здоровье на фулл. Взрывает. Все выстрелы берсерка взрывные. Работает один раз восполнение здоровья
    @Override
    public double damage() {
        return 16;
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
    public List<ItemStack> loot(Player player) {
        return List.of();
    }

    @Override
    public Set<AbilityType> abilities() {
        return Set.of(AbilityType.EXPLOSION_NEUTRALITY);
    }

//    @EventHandler
//    public void spawn(CreatureSpawnEvent event) {
//        LivingEntity ent = event.getEntity();
//        if (!(ent.getType().equals(org.bukkit.entity.EntityType.BLAZE))) return;
//        if (GlobalRPG.containEntity(ent)) return;
//        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
//
//        if (!chance(0.09)) return;
//        this.create(ent.getLocation());
//        event.setCancelled(true);
//    }

    private boolean regened = false;

    @Override
    public boolean onTarget(Entity target) {
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        if (this.regened) {
            target.getWorld().createExplosion(target.getLocation(), 1);
        }
        return true;
    }

    @Override
    public boolean onDamage(Entity damager) {
        if (!this.regened) {
            double maxHealth = entity.getMaxHealth();
            if (entity.getHealth() < maxHealth / 10) { // health < 10%
                entity.setHealth((float) maxHealth);
                this.regened = true;
                World world = this.entity.level().getWorld();
                world.createExplosion(CraftLocation.toBukkit(entity.position(), world), 3F, false, false);
            }
        }

        return !(damager instanceof Projectile); // if is Projective -> event cancelled
    }

    @Override
    public void onDeath() {

    }
}
