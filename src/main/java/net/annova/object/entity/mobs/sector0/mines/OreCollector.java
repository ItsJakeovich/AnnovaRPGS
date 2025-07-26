package net.annova.object.entity.mobs.sector0.mines;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.MinesMagicArmorNeutral;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.MAGIC_SCRAP;

public class OreCollector extends RPGEntity implements CustomType, EntityAction {
    @Override
    public Class<?> clazz() {
        return MinesMagicArmorNeutral.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 55;
    }

    @Override
    public double damage() {
        return 3;
    }

    @Override
    public float speed() {
        return 0.375f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(MAGIC_SCRAP.create(person, 0.046));
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public boolean onTarget(Entity target) {
        if (target instanceof LivingEntity living)
            new Guardian().createWithTarget(OreCollector.this, living, 1);
        return true;
    }

    @Override
    public boolean onAttack(LivingEntity target) {
        return true;
    }

    @Override
    public void onDamage(MobDamageCE event, LivingEntity damager) {
    }

    @Override
    public void onDeath() {

    }
}
