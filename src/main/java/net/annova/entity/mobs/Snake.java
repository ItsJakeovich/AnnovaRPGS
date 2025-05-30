package net.annova.entity.mobs;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.annova.event.custom.EntityDamageByEntityCE;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.util.BukkitObjects.random;

public class Snake extends RPGEntity implements Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.SILVERFISH;
    }

    @Override
    public String name() {
        return "Snake";
    }

    @Override
    public int health() {
        return 20;
    }

    @Override
    public double damage() {
        return 8;
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
    public List<ItemStack> loot() {
        return List.of(
            createItem(Material.RABBIT_HIDE, "&fЗмеиная кожа", 1, random.nextInt(2), 0.2)
        );
    }

    @Override
    public Set<Ability> abilities() {
        return Set.of();
    }

    // Spawn in UpdateSpawnTimer

    private final PotionEffect poison = new PotionEffect(PotionEffectType.POISON, 100, 1);
    private final PotionEffect slowness = new PotionEffect(PotionEffectType.SLOWNESS, 100, 0);
    @EventHandler
    public void mechanic(EntityDamageByEntityCE event) {
        if (!this.equals(GlobalRPG.getEntity(event.getDamager()))) return;
        LivingEntity target = event.getEntity();
        target.addPotionEffect(poison);
        target.addPotionEffect(slowness);
    }
}
