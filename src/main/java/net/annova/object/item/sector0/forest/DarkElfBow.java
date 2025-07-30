package net.annova.object.item.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.RPGTool;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.environment.world.GlobalWorld.isNight;
import static net.annova.object.item.base.ItemType.DARK_ELF_BOW;
import static net.annova.util.BukkitObjects.random;

public class DarkElfBow extends RPGItem implements RPGTool, Listener {
    @Override
    public Item type() {
        return Items.BOW;
    }

    @Override
    public List<Float> modelData() {
        return List.of(418f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    @Override
    public double damage() {
        return 3.5;
    }

    @Override
    public double attackSpeed() {
        return 1;
    }

    private final PotionEffect darkness = new PotionEffect(PotionEffectType.DARKNESS, 100, 0);
    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (DARK_ELF_BOW.equals(event.getBow())) {
            Arrow arrow = (Arrow) event.getProjectile();
            float force = event.getForce();
            arrow.setDamage(arrow.getDamage()+ 3.5*force);
            arrow.addCustomEffect(darkness, true);
            LivingEntity shooter = event.getEntity();
            World world = shooter.getWorld();
            Location location = shooter.getLocation();
            world.spawnParticle(Particle.DRAGON_BREATH, location, 30, 3.5, 2.5, 3.5);

            if (shooter.getLocation().getPitch() > -50 || !isNight(arrow.getWorld())) return;
            float speed = 3 * force;
            world.spawnArrow(location.add(random.nextDouble(1.5)-0.75, random.nextDouble(1)-0.5, random.nextDouble(1.5)-0.75), arrow.getVelocity(), speed, force);
            world.spawnArrow(location.add(random.nextDouble(1.5)-0.75, random.nextDouble(1)-0.5, random.nextDouble(1.5)-0.75), arrow.getVelocity(), speed, force);
        }
    }
}
