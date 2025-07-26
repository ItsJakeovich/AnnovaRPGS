package net.annova.object.entity.mobs.nether;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.environment.world.SectorLocation.getChorusLocation;
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
    public List<net.minecraft.world.item.ItemStack> loot(Person person) {
        return List.of(
                entity.getItemInHand(InteractionHand.MAIN_HAND)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
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
    private static final MobEffectInstance INVISIBILITY = new MobEffectInstance(MobEffects.INVISIBILITY, 200, 1);
    @Override
    public boolean onTarget(Entity target) {
        entity.addEffect(INVISIBILITY);
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        if (!entity.equipment.get(EquipmentSlot.MAINHAND).isEmpty()) return true;

        if (target instanceof Player player) {
            Inventory inventory = player.getInventory();
            List<ItemStack> items = inventory.getNonEquipmentItems();
            for (int i = 0; i < 40; i++) {
                int index = random.nextInt(items.size());
                ItemStack check = items.get(index);
                if (check.isEmpty()) continue;
                entity.setItemInHand(InteractionHand.MAIN_HAND, check);
                inventory.setItem(index, ItemStack.EMPTY);
                entity.removeEffect(MobEffects.INVISIBILITY);
                getChorusLocation(entity.position(), entity.level().getMinecraftWorld(), 10);
                break;
            }
        }
        return true;
    }

    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
    }

    @Override
    public void onDeath() {

    }
}
