package net.annova.object.entity.mobs.sector0.mines;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.EntitySpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.MAGIC_SCRAP;

public class Guardian extends RPGEntity implements EntitySpawn, EntityAction {
    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Guardian";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 200;
    }

    @Override
    public double damage() {
        return 9;
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
        return List.of(MAGIC_SCRAP.create(player, 0.01));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    AttributeModifier modifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("annovas", "guardian_boost")
            , 0.15f, AttributeModifier.Operation.ADD_VALUE
    );
    @Override
    public boolean onTarget(Entity target) {
        AttributeInstance attribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (!attribute.hasModifier(modifier.id())) {
            attribute.addTransientModifier(modifier);
        }
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(modifier);
        return true;
    }

    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
    }

    @Override
    public void onDeath() {

    }

    @Override
    public void onSpawn(Location location) {

    }
}
