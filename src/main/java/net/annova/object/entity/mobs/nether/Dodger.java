package net.annova.object.entity.mobs.nether;

import net.annova.object.RPGRarity;
import net.annova.object.entity.ability.AbilityType;
import net.annova.object.entity.core.RPGEntity;
import net.annova.event.custom.BattleCE;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static net.annova.environment.world.Location.getChorusLocation;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class Dodger extends RPGEntity implements Listener {
    @Override
    public EntityType<?> type() {
        return EntityType.PIGLIN;
    }

    @Override
    public String name() {
        return "Dodger";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 65;
    }

    @Override
    public double damage() {
        return 6;
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
        return List.of(
                ((LivingEntity) entity.getBukkitEntity()).getEquipment().getItemInMainHand()
        );
    }

    @Override
    public Set<AbilityType> abilities() {
        return Set.of();
    }

    @EventHandler
    private void spawn(CreatureSpawnEvent event) {
        LivingEntity ent = event.getEntity();
        if (!(ent.getType().equals(org.bukkit.entity.EntityType.PIGLIN))) return;
        if (GlobalRPG.containEntity(ent)) return;
        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

        if (!chance(0.09)) return;
        this.create(ent.getLocation());
        event.setCancelled(true);
    }

    @EventHandler
    private void target(EntityTargetEvent event) {
        Entity ent = event.getEntity();
        RPGEntity rpg = GlobalRPG.getEntity(ent);
        if (rpg == null) return;
        if (!(rpg instanceof Dodger)) return;
        ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1));
    }
    @EventHandler
    private void attack(BattleCE event) {
        LivingEntity entity = event.getDamager();
        RPGEntity rpg = GlobalRPG.getEntity(entity);
        if (!(rpg instanceof Dodger)) return;
        if (!entity.getEquipment().getItemInMainHand().getType().isAir()) return;

        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack[] inventory = player.getInventory().getContents();
        for (int i = 0 ; i < 40; i++) {
            int index = random.nextInt(inventory.length);
            ItemStack check = inventory[index];
            if (check == null) continue;
            entity.getEquipment().setItemInMainHand(check);
            player.getInventory().setItem(index, null);
            entity.removePotionEffect(PotionEffectType.INVISIBILITY);
            getChorusLocation(entity.getLocation(), 10);
            break;
        }
    }
}
