package net.annova.object.entity.mobs.nether;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.World;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Projectile;

import java.util.List;
import java.util.Set;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.object.entity.core.attribute.ability.RPGAbility.Type.EXPLOSION_NEUTRALITY;

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
    public List<ItemStack> loot(Person person) {
        return List.of();
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of(EXPLOSION_NEUTRALITY.get());
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
            Vec3 position = target.position();
            world0.explode(null, position.x, position.y, position.z, 1.0f, true, Level.ExplosionInteraction.STANDARD);
        }
        return true;
    }

    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
        if (!this.regened) {
            double maxHealth = entity.getMaxHealth();
            if (entity.getHealth() < maxHealth / 10) { // health < 10%
                entity.setHealth((float) maxHealth);
                this.regened = true;
                World world = this.entity.level().getWorld();
                world.createExplosion(CraftLocation.toBukkit(entity.position(), world), 3F, false, false);
            }
        }

        event.setCancelled(damager instanceof Projectile); // if is Projective -> event cancelled
    }

    @Override
    public void onDeath() {

    }
}
