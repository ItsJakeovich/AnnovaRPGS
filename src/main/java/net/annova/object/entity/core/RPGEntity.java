package net.annova.object.entity.core;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.world.SectorLocation;
import net.annova.object.CustomRunnable;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.EntityEquipment;
import net.annova.object.entity.core.attribute.EntitySpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.status.EntityStatus;
import net.annova.object.entity.core.status.StatusType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.main.AnnovaAPI.getSQL;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.addEntity;
import static net.annova.main.GlobalRPG.removeEntity;
import static net.annova.main.Keys.NK_ID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public abstract class RPGEntity {
    public LivingEntity entity;
    public Dungeon dungeon;
    public int point;
    private SectorLocation sectorLocation;
    private final RPGEntityType rpgType;
    private final Map<StatusType, EntityStatus> statuses = new EnumMap<>(StatusType.class);
    public abstract EntityType<?> type();
    public abstract String name();
    public abstract RPGRarity rarity();
    public abstract int health();
    public abstract double damage();
    public abstract float speed();
    public abstract boolean small();
    public abstract List<ItemStack> loot(Player player);

    public abstract Set<RPGAbility> abilities();

    public RPGEntity() {
        this.rpgType = RPGEntityType.getType(this);
    }

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
    private static EntityType<?> register(ResourceKey<EntityType<?>> resourcekey, EntityType.Builder<?> entitytypes_builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, resourcekey, entitytypes_builder.noSave().build(resourcekey));
    }
    public static EntityType<?> register(String key, EntityType.Builder<?> builder) {
        return register(ResourceKey.create(Registries.ENTITY_TYPE, fromNamespaceAndPath("annovas", key)), builder);
    }

    @Nullable
    public EntityStatus getStatus(StatusType type) {
        return statuses.get(type);
    }
    public void addStatus(StatusType type) {
        this.statuses.put(type, new EntityStatus(type));
    }
    public void removeStatus(StatusType type) {
        this.statuses.remove(type);
    }

    public Location getLocation() {
        return CraftLocation.toBukkit(entity.position(), entity.level().getWorld());
    }

    public LivingEntity create(LivingEntity entity) {
        return this.create(CraftLocation.toBukkit(entity.position(), entity.level().getWorld()));
    }
    public LivingEntity create(Location location) {
        return this.create(null, location, false, 0, 1, 1);
    }
    public LivingEntity create(Location location, boolean additional) {
        return this.create(null, location, additional, 0, 1, 1);
    }
    public LivingEntity create(Dungeon dungeon, Location location, boolean additional, float addDamage, float multHealth, float multSpeed) {
        this.dungeon = dungeon;

        ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
        LivingEntity ent = null;
        if (this instanceof CustomType type) {
            try {
                ent = (LivingEntity) type.clazz().getDeclaredConstructor(EntityType.class, Level.class).newInstance(this.type(), level);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                sendInfo("&e| No such method while creating entity: " + this.getClass().getName());
            }
        } else ent = (LivingEntity) this.type().create(level, EntitySpawnReason.EVENT);
        ent.setPos(location.getBlockX()+0.5, location.getBlockY(), location.getBlockZ()+0.5);
        ent.setCustomName(Component.literal(this.name()));
        ent.setCustomNameVisible(true);

        AttributeInstance attribute = ent.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attribute != null) attribute.setBaseValue(this.damage() + addDamage);

        attribute = ent.getAttribute(Attributes.MAX_HEALTH);
        if (attribute != null) {
            float health = this.health() * multHealth;
            attribute.setBaseValue(health);
            ent.setHealth(health);
        }
        ent.persist = false;

        attribute = ent.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null) attribute.setBaseValue(this.speed() * multSpeed);

        if (ent instanceof Zombie zombie) {
            zombie.setBaby(this.small());
        } else if (ent instanceof Slime slime) {
            slime.setSize(1, false);
        }

        ent.getBukkitEntity().getPersistentDataContainer().set(NK_ID, PersistentDataType.STRING, RPGEntityType.getType(this).name());

        this.entity = ent;
        level.tryAddFreshEntityWithPassengers(ent);
        addEntity(this);

        if (this instanceof EntitySpawn spawn && !additional)
            spawn.onSpawn(location);
        if (this instanceof EntityActivity activity) {
            for (CustomRunnable custom :  activity.runnables()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (entity.isAlive() && entity.valid && custom.activity()) return;
                        this.cancel();
                    }
                }.runTaskTimer(getInstance(), 0, custom.repeating());
            }
        }

        if (this instanceof EntityEquipment equipment) {
            org.bukkit.inventory.EntityEquipment equip = ((org.bukkit.entity.LivingEntity) ent.getBukkitEntity()).getEquipment();
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

    public SectorLocation getSectorLocation() {
        return sectorLocation;
    }
    public void setSectorLocation(SectorLocation sectorLocation) {
        this.sectorLocation = sectorLocation;
    }

    public RPGEntityType getRPGType() {
        return rpgType;
    }

    public void remove() {
        if (entity != null) entity.remove(Entity.RemovalReason.KILLED);
        removeEntity(this.getEntity().getBukkitEntity());
        // если будут записываться-то удалять тут тоже
    }

    @Nullable
    public RPGAbility getAbility(RPGAbility ability) {
        for (RPGAbility abil : this.abilities()) {
            if (abil.equals(ability)) return abil;
        }
        return null;
    }

    public void regen(float regen) {
        this.entity.heal(regen);
    }

    public static void addPotionEffect(org.bukkit.entity.LivingEntity entity, PotionEffectType type, int duration) {
        entity.addPotionEffect(new PotionEffect(type, duration, 0));
    }
    public static void addPotionEffect(org.bukkit.entity.LivingEntity entity, PotionEffectType type, int duration, int modifier) {
        entity.addPotionEffect(new PotionEffect(type, duration, modifier));
    }
    public static void healEntity(double regen, org.bukkit.entity.LivingEntity entity) {
        double maxHealth = entity.getAttribute(Attribute.MAX_HEALTH).getValue();
        entity.setHealth(Math.min(entity.getHealth()+regen, maxHealth));
    }
}
