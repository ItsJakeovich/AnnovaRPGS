package net.annova.object.entity.core;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.world.SectorLocation;
import net.annova.main.AnnovaAPI;
import net.annova.object.CustomRunnable;
import net.annova.object.person.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.EntityEquipment;
import net.annova.object.entity.core.attribute.EntitySpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.status.EntityStatus;
import net.annova.object.entity.core.status.StatusType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.environment.world.SectorLocation.getChorusLocation;
import static net.annova.main.AnnovaAPI.getMinecraftServer;
import static net.annova.main.AnnovaAPI.getSQL;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.*;
import static net.annova.main.Keys.NK_ID;
import static net.annova.object.person.equipment.Equipment.getOpsTag;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public abstract class RPGEntity {
    public Mob entity;
    public Dungeon dungeon;
    public int point;
    private int experience;
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

    public abstract List<ItemStack> loot(Person person);

    public abstract Set<RPGAbility> abilities();

    public RPGEntity() {
        this.rpgType = RPGEntityType.getType(this);
    }

    public static final String TABLE = "ENTITY";

    private static final EquipmentSlot[] slots = {
            EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST,
            EquipmentSlot.LEGS, EquipmentSlot.FEET
    };
    public static void init() {
        getSQL().initTable(TABLE,
                "UID VARCHAR(36)",
                "TYPE VARCHAR(60)",
                "POSITION VARCHAR(64)",
                "EXP INT UNSIGNED",
                "EQUIPMENT TEXT",

                "PRIMARY KEY (UID)"
        );
        try (PreparedStatement ps = getSQL().getConnection().prepareStatement("SELECT * FROM " + TABLE)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] position = rs.getString("POSITION").split(",");
                Vec3 pos = new Vec3(Double.parseDouble(position[1]), Double.parseDouble(position[2]), Double.parseDouble(position[3]));
                ServerLevel level = getMinecraftServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.withDefaultNamespace(position[0].toLowerCase())));
                RPGEntity entity = RPGEntityType.valueOf(rs.getString("TYPE")).getEntity();
                entity.create(pos, level, true);
                entity.experience = rs.getInt("EXP");

                String[] equipment = rs.getString("EQUIPMENT").split(";");
                net.minecraft.world.entity.EntityEquipment equip = entity.entity.equipment; try {
                    for (int i = 0; i < equipment.length; i++) {
                        String serialItem = equipment[i];
                        if (serialItem.equals(" ")) continue;
                        equip.set(slots[i], ItemStack.parse(getMinecraftServer().registryAccess(), TagParser.parseCompoundFully(serialItem)).get());
                    }
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException(e);
                }
            } rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void save() {
        BlockPos pos = this.entity.blockPosition();
        Map<String, Object> save = new HashMap<>();
        save.put("UID", this.entity.getUUID().toString());
        save.put("TYPE", this.rpgType.name());
        save.put("POSITION", this.entity.level().dimension().location().getPath() + "," +
                pos.getX() + "," + pos.getY() + "," + pos.getZ());
        save.put("EXP", this.experience);
        save.put("EQUIPMENT", this.serializeEquipment());
        AnnovaAPI.getSQL().saveObject(TABLE, "UID", save);
    }

    public String serializeEquipment() {
        StringJoiner joiner = new StringJoiner(";");
        net.minecraft.world.entity.EntityEquipment equipment = this.entity.equipment;
        ItemStack[] items = new ItemStack[] {
                equipment.get(EquipmentSlot.MAINHAND),
                equipment.get(EquipmentSlot.OFFHAND),
                equipment.get(EquipmentSlot.HEAD),
                equipment.get(EquipmentSlot.CHEST),
                equipment.get(EquipmentSlot.LEGS),
                equipment.get(EquipmentSlot.FEET),
        };
        for (ItemStack item : items) {
            if (item.isEmpty()) joiner.add(" ");
            else joiner.add(item.save(getOpsTag()).toString());
        }
        return joiner.toString();
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

    public void createWithTarget(RPGEntity entity, LivingEntity target) {
        this.createWithTarget(entity.entity.level().getMinecraftWorld(), entity.entity.position(), target, 2);
    }
    public void createWithTarget(RPGEntity entity, LivingEntity target, int chorus) {
        this.createWithTarget(entity.entity.level().getMinecraftWorld(), entity.entity.position(), target, chorus);
    }
    public Mob createWithTarget(LivingEntity entity, LivingEntity target, int chorus) {
        return this.createWithTarget(entity.level().getMinecraftWorld(), entity.position(), target, chorus);
    }
    public Mob createWithTarget(ServerLevel level, Vec3 position, org.bukkit.entity.LivingEntity target, int chorus) {
        return this.createWithTarget(level, position, target, chorus, 0);
    }
    public Mob createWithTarget(ServerLevel level, Vec3 position, org.bukkit.entity.LivingEntity target, int chorus, long discardedAfter) {
        return this.createWithTarget(level, position, ((CraftLivingEntity) target).getHandle(), chorus, discardedAfter);
    }
    public Mob createWithTarget(ServerLevel level, Vec3 position, LivingEntity target, int chorus) {
        return this.createWithTarget(level, position, target, chorus, 0);
    }
    public Mob createWithTarget(LivingEntity entity, LivingEntity target, int chorus, long discardedAfter) {
        return this.createWithTarget(entity.level().getMinecraftWorld(), entity.position(), target, chorus, discardedAfter);
    }
    public Mob createWithTarget(ServerLevel level, Vec3 position, LivingEntity target, int chorus, long discardedAfter) {
        Mob living = this.create(position, level, chorus, true);
        living.setTarget(target, EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);
        if (discardedAfter != 0)
            Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> living.remove(Entity.RemovalReason.DISCARDED), discardedAfter);
        return living;
    }


    public LivingEntity create(LivingEntity entity) {
        ServerLevel level = entity.level().getMinecraftWorld(); // если что попробуй .getMinecraftWorld()
        return this.create(getChorusLocation(entity.position(), level, 1), level);
    }
    public Mob create(Vec3 position, ServerLevel level) {
        return this.create(null, position, level, false, 0, 1, 1);
    }

    public Mob create(Vec3 position, ServerLevel level, int chorus) {
        return this.create(null, getChorusLocation(position, level, chorus), level, false, 0, 1, 1);
    }
    public Mob create(Vec3 position, ServerLevel level, boolean additional) {
        return this.create(null, position, level, additional, 0, 1, 1);
    }
    public Mob create(Vec3 position, ServerLevel level, int chorus, boolean additional) {
        return this.create(null, getChorusLocation(position, level, chorus), level, additional, 0, 1, 1);
    }
    public Mob create(Dungeon dungeon, Vec3 position, ServerLevel level, boolean additional, float addDamage, float multHealth, float multSpeed) {
        this.dungeon = dungeon;

        LivingEntity ent = null;
        if (this instanceof CustomType type) {
            try {
                ent = (LivingEntity) type.clazz().getDeclaredConstructor(EntityType.class, Level.class).newInstance(this.type(), level);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                sendInfo("| No such method while creating entity: " + this.getClass().getName());
            }
        } else ent = (LivingEntity) this.type().create(level, EntitySpawnReason.EVENT);
        ent.setPos(position);
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

        this.entity = (Mob) ent;
        level.tryAddFreshEntityWithPassengers(ent);
        addEntity(this);

        if (this instanceof EntitySpawn spawn && !additional)
            spawn.onSpawn(level, ent.position());
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
            ent.setItemSlot(EquipmentSlot.MAINHAND, equipment.equipment()[0]);
            ent.setItemSlot(EquipmentSlot.OFFHAND, equipment.equipment()[1]);
            ent.setItemSlot(EquipmentSlot.HEAD, equipment.equipment()[2]);
            ent.setItemSlot(EquipmentSlot.CHEST, equipment.equipment()[3]);
            ent.setItemSlot(EquipmentSlot.LEGS, equipment.equipment()[4]);
            ent.setItemSlot(EquipmentSlot.FEET, equipment.equipment()[5]);
        }

        return (Mob) ent;
    }

    public String getId() {
        return entity.getBukkitEntity().getPersistentDataContainer().get(NK_ID, PersistentDataType.STRING);
    }

    public Mob getEntity() {
        return entity;
    }
    public void setEntity(Mob entity) {
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

    public int getExperience() {
        return experience;
    }
    public void setExperience(int experience) {
        this.experience = experience;
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
    public static void hurtEntity(double damage, LivingEntity entity, LivingEntity damager) {
        hurtEntity((float) damage, entity, damager);
    }
    public static void hurtEntity(float damage, LivingEntity entity, LivingEntity damager) {
        DamageSource reason = damager.damageSources().generic();
        if (damager instanceof Player player) {
            reason = damager.damageSources().playerAttack(player);
        } else if (damager instanceof org.bukkit.entity.LivingEntity) {
            reason = damager.damageSources().mobAttack(damager);
        }
        entity.hurtServer(damager.level().getMinecraftWorld(), reason, damage);
    }
}
