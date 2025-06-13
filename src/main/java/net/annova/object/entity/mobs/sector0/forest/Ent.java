package net.annova.object.entity.mobs.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.Item.ENT_ROOT;

public class Ent extends RPGEntity implements EntityAction {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return ChatColor.DARK_GREEN + "Ent";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 160;
    }

    @Override
    public double damage() {
        return 7;
    }

    @Override
    public float speed() {
        return 0.33f;
    }

    @Override
    public boolean small() {
        return true;
    }

    @Override
    public List<ItemStack> loot(Player player) {
        return List.of(ENT_ROOT.create(player, 0.01));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public boolean onTarget(Entity target) {
        return true;
    }

    private final static PotionEffect slowness = new PotionEffect(PotionEffectType.SLOWNESS, 60, 1);
    @Override
    public boolean onAttack(LivingEntity target) {
        target.addPotionEffect(slowness);
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
