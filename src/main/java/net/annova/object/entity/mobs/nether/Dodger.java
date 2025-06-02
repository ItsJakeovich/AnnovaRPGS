package net.annova.object.entity.mobs.nether;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.AbilityType;
import net.annova.object.entity.core.EntityAction;
import net.annova.object.entity.core.RPGEntity;
import net.annova.event.custom.BattleCE;
import net.annova.main.GlobalRPG;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static net.annova.environment.world.Location.getChorusLocation;
import static net.annova.util.BukkitObjects.random;

public class Dodger extends RPGEntity implements EntityAction {
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

//    @EventHandler
//    private void spawn(CreatureSpawnEvent event) {
//        LivingEntity ent = event.getEntity();
//        if (!(ent.getType().equals(org.bukkit.entity.EntityType.PIGLIN))) return;
//        if (GlobalRPG.containEntity(ent)) return;
//        if (!ent.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
//
//        if (!chance(0.09)) return;
//        this.create(ent.getLocation());
//        event.setCancelled(true);
//    }
    @Override
    public boolean onTarget(Entity target) {
        ((Creature) entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1));
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        ItemStack s;
        s.
        if (!entity.equipment.get(EquipmentSlot.MAINHAND).get.getType().isAir()) return;

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
        return true;
    }

    @Override
    public boolean onDamage(Entity damager) {
        return true;
    }

    @Override
    public void onDeath() {

    }
}
