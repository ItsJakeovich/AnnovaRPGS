package net.annova.object.entity.core;

import net.annova.environment.dungeon.Dungeon;
import net.annova.object.RPGRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Zombie;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static net.annova.main.AnnovaAPI.getSQL;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.addEntity;
import static net.annova.main.GlobalRPG.removeEntity;
import static net.annova.main.Keys.NK_ID;

public abstract class RPGEntity {
    public LivingEntity entity;
    public Dungeon dungeon;
    public int point;
    public abstract EntityType<?> type();
    public abstract String name();
    public abstract RPGRarity rarity();
    public abstract int health();
    public abstract double damage();
    public abstract float speed();
    public abstract boolean small();
    public abstract List<ItemStack> loot(Player player);

    public abstract Set<AbilityType> abilities();

    public static final String TABLE = "ENTITY";
    public static void init() {
        getSQL().initTable(TABLE,
                "CLASS VARCHAR(128)",
                "LOCATION VARCHAR(128)",
                "LEVEL SMALLINT(1)",
                "EXP INTEGER(1)",
                "INVENTORY TEXT"
        );
        //ResultSet rs = getSQL().(TABLE);
    }

    public LivingEntity create(LivingEntity entity) {
        return this.create(CraftLocation.toBukkit(entity.position(), entity.level().getWorld()));
    }
    public LivingEntity create(Location location) {
        return this.create(null, location, 0, 1, 1);
    }
    public LivingEntity create(Dungeon dungeon, Location location, float addDamage, float multHealth, float multSpeed) {
        this.dungeon = dungeon;

        ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
        LivingEntity ent = (LivingEntity) this.type().create(level, EntitySpawnReason.EVENT);
        ent.setPos(location.getBlockX()+0.5, location.getBlockY(), location.getBlockZ()+0.5);
        ent.setCustomName(Component.literal(this.rarity().getColor() + this.name()));

        AttributeInstance attribute = ent.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attribute != null) attribute.setBaseValue(this.damage() + addDamage);

        attribute = ent.getAttribute(Attributes.MAX_HEALTH);
        if (attribute != null) {
            float health = this.health() * multHealth;
            attribute.setBaseValue(health);
            ent.setHealth(health);
        }
        ent.persist = true;

        attribute = ent.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null) attribute.setBaseValue(this.speed() * multSpeed);

        if (ent instanceof Zombie zombie) {
            zombie.setBaby(this.small());
        } else if (ent instanceof Slime slime) {
            slime.setSize(1, false);
        }

        ent.getBukkitEntity().getPersistentDataContainer().set(NK_ID, PersistentDataType.STRING, RPGEntityType.getId(this));

        this.entity = ent;
        level.tryAddFreshEntityWithPassengers(ent);
        addEntity(this);

        if (this instanceof RPGSpawn spawn)
            spawn.onSpawn(ent, location);
        if (this instanceof RPGActivity activity) {
            if (activity.repeating() != 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!entity.getBukkitEntity().isValid()
                                || !activity.activity()
                        ) this.cancel();
                    }
                }.runTaskTimer(getInstance(), 0, activity.repeating());
            }
            if (activity.syncRepeating() != 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!entity.getBukkitEntity().isValid()
                                || !activity.syncActivity((org.bukkit.entity.LivingEntity) ent.getBukkitEntity())
                        ) this.cancel();
                    }
                }.runTaskTimer(getInstance(), 0, activity.syncRepeating());
            }
        }

        if (this instanceof RPGEquipment equipment) {
            EntityEquipment equip = ((org.bukkit.entity.LivingEntity) ent.getBukkitEntity()).getEquipment();
            equip.setItemInMainHand(equipment.equipment()[0]);
            equip.setItemInOffHand(equipment.equipment()[1]);
            equip.setHelmet(equipment.equipment()[2]);
            equip.setChestplate(equipment.equipment()[3]);
            equip.setLeggings(equipment.equipment()[4]);
            equip.setBoots(equipment.equipment()[5]);
        }

        return ent;
    }

    public String getId() {
        return entity.getBukkitEntity().getPersistentDataContainer().get(NK_ID, PersistentDataType.STRING);
    }

    public LivingEntity getEntity() {
        return entity;
    }
    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }
    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void remove() {
        if (entity != null) entity.remove(Entity.RemovalReason.KILLED);
        removeEntity(this.getEntity().getBukkitEntity());
        // если будут записываться то удалять тут тоже
    }

    @Nullable
    public AbilityType getAbility(AbilityType ability) {
        for (AbilityType abil : this.abilities()) {
            if (abil.equals(ability)) return abil;
        }
        return null;
    }

    public void regen(float regen) {
//        org.bukkit.entity.LivingEntity ent = (org.bukkit.entity.LivingEntity) this.entity.getBukkitEntity();
//        double maxHealth = ent.getAttribute(Attribute.MAX_HEALTH).getValue();
//        ent.setHealth(Math.min(ent.getHealth()+regen, maxHealth));

        this.entity.heal(regen);
    }

    public static void addPotionEffect(org.bukkit.entity.LivingEntity entity, PotionEffectType type, double duration) {
        entity.addPotionEffect(new PotionEffect(type, (int) duration, 0));
    }
}
