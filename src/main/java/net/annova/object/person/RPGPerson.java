package net.annova.object.person;

import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.world.RPGAdvancement;
import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.WorldSector;
import net.annova.object.item.base.*;
import net.annova.main.AnnovaAPI;
import net.annova.main.AnnovaGlobal;
import net.annova.main.AnnovaSQL;
import net.annova.object.Person;
import net.annova.object.item.base.RPGSet;
import net.annova.object.person.equipment.Equipment;
import net.annova.object.person.quest.QuestManager;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.ValueType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.event.entity.EntityKnockbackEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Set;

import static net.annova.environment.world.GlobalWorld.worldSectors;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.addRPGPerson;
import static net.annova.util.MessageObjects.sendForAll;
import static net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE;

public class RPGPerson {
    private final Person person;
    private Dungeon dungeon;
    private int dungeonPoints = 0;
    private Team team;
    private RPGSet set;
    private final RPGItem[] armor = new RPGItem[4];
    private boolean ready = false;
    private final Equipment equipment = new Equipment(this);
    private final StatusManager statusManager = new StatusManager(this);
    private final List<ItemStack> pendingItems = new ArrayList<>();
    private final Map<String, Long> speedModifiers = new HashMap<>();
    private final QuestManager questManager = new QuestManager();
    private boolean teleportAccess = false;
    private boolean showBar = true;

    private SectorLocation sectorLocation;

    private final EnumSet<RPGAdvancement> advancements = EnumSet.noneOf(RPGAdvancement.class);

    private float health = 20;
    private float maxHealth = 20;
    private float regeneration = 0f;
    private float additionalHealth = 0;

    private static final String TABLE = "RPGPERSON";
    public static void init() {
        AnnovaSQL sql = AnnovaAPI.getSQL();
        sql.initTable(TABLE,
                "NAME VARCHAR(24)",
                "QUEST_MANAGER MEDIUMTEXT",

                "PRIMARY KEY (NAME)"
        );
        sql.loadFromDB(TABLE, RPGPerson.class)
                .forEach(person -> addRPGPerson((RPGPerson) person));
    }
    public void save() {
        Map<String, Object> save = new HashMap<>();
        save.put("NAME", this.person.getName());
        save.put("QUEST_MANAGER", this.questManager.serializeQuests());
        AnnovaAPI.getSQL().saveObject(TABLE, "NAME", save);
    }
    
    public RPGPerson(Person person) {
        this.person = person;
    }
    public RPGPerson(ResultSet rs) throws SQLException {
        this.person = AnnovaGlobal.getPerson(rs.getString("NAME"));
        this.questManager.deserialize(rs.getString("QUEST_MANAGER"));
    }

    public Person getPerson() {
        return person;
    }
    public Player getPlayer() {
        return this.person.getPlayer();
    }

    @Nullable
    public Dungeon getDungeon() {
        return dungeon;
    }
    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public int getDungeonPoints() {
        return dungeonPoints;
    }
    public void setDungeonPoints(int points) {
        this.dungeonPoints = points;
    }
    public void addDungeonPoints(int points) {
        if (this.dungeon == null) return;
        int newPoints = (int) (points * (1 + ((dungeon.getPlayers().size()-1)*0.3)));
        this.dungeonPoints = Math.min(dungeonPoints + newPoints, 10000);

        this.person.sendBar(person.getString(getInstance(), "dungeon-points")
                .replace("{plus}", String.valueOf(newPoints))
                .replace("{points}", String.valueOf(dungeonPoints))
        );
        Player player = this.getPerson().getPlayer();
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
    }
    public void reduceDungeonPoints(int points) {
        this.dungeonPoints -= points;
    }

    public void damageWithCooldown(Player player, float damage) {
        this.damageWithCooldown(player, null, damage);
    }
    public void damageWithCooldown(Player player, Entity damager, float damage) {
        if (player.getNoDamageTicks() != 0) return;
        player.setNoDamageTicks(10);
        this.damage(player, damager, damage);
    }
    public void damage(Player player, float damage) {
        this.damage(player, null, damage);
    }
    public void damage(Player player, Entity damager, float damage) {
        player.playHurtAnimation(1.0f);
        player.playSound(player, Sound.ENTITY_PLAYER_HURT, 1, 1);
        this.knockback(player, damager);

        this.setHealth(this.health - damage);
    }
    public void knockback(Player player, Entity damager) {
        if (damager == null) return;

        ServerPlayer server = ((CraftPlayer) player).getHandle();
        double force = 0.6 * (1.0 - server.getAttributeValue(KNOCKBACK_RESISTANCE));

        net.minecraft.world.entity.Entity nms = ((CraftEntity) damager).getHandle();
        double dirX, dirZ;
        if (nms instanceof Projectile projectile) {
            DoubleDoubleImmutablePair pair = projectile.calculateHorizontalHurtKnockbackDirection(server, null);
            dirX = -pair.leftDouble();
            dirZ = -pair.rightDouble();
        } else {
            Vec3 vec3 = nms.position();
            Vec3 playerPos = server.position();
            dirX = vec3.x - playerPos.x;
            dirZ = vec3.z - playerPos.z;
        }

        Vec3 vec3d = server.getDeltaMovement();
        while (dirX*dirX + dirZ*dirZ < 1.0E-5F) {
            dirZ = (Math.random() - Math.random()) * 0.01;
            dirX = (Math.random() - Math.random()) * 0.01;
        }
        Vec3 knockback = (new Vec3(dirX, 0.0, dirZ)).normalize().scale(force);
        EntityKnockbackEvent event = new EntityKnockbackByEntityEvent(player, damager,
                EntityKnockbackEvent.KnockbackCause.ENTITY_ATTACK, force, new Vector(-knockback.x, -knockback.y, -knockback.z),
                new Vector(
                        vec3d.x / 2.0 - knockback.x,
                        server.onGround ? Math.min(0.4, vec3d.y / 2.0 + force) : vec3d.y,
                        vec3d.z / 2.0 - knockback.z
                )
        );

        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        Vector vector = event.getFinalKnockback();
        server.setDeltaMovement(new Vec3(vector.getX(), vector.getY(), vector.getZ()));
        server.hurtMarked = true;
    }

    public void regen(Player player) {
        float regeneration = this.regeneration;
        float food = player.getFoodLevel();
        if (food == 0) {
            regeneration -= 3;
            player.sendHurtAnimation(10);
        }
        else regeneration += 3 * food/20;

        if (regeneration < 0) {
            this.person.getPlayer().damage(0.1, DamageSource.builder(DamageType.STARVE).build());
        }
        this.regen(regeneration);
    }
    public void regen(float regen) {
        this.setHealth(this.health + regen);
    }
    public void setHealth(float health) {
        if (health <= 0) {
            this.death();
            health = maxHealth;
        }
        if (health > maxHealth) health = maxHealth;
        this.health = health;
        this.updateStatusBar();
    }
    public void updateHealth() {
        float newMaxHealth = 20;
        Player player = person.getPlayer();
        if (player == null) return;

        double valueHealth = this.getFinalValue(ValueType.HEALTH);
        newMaxHealth += (float) (additionalHealth + valueHealth);

        this.setHealth(newMaxHealth * health / maxHealth);
        this.maxHealth = newMaxHealth;
    }
    public void updateStatusBar() {
        if (!this.showBar) return;
        String message = this.person.getString(getInstance(), "bar-health")
                .replace("{h}", String.valueOf((int) this.health))
                .replace("{m}", String.valueOf((int) this.maxHealth));
        Player player = this.person.getPlayer();
        player.setHealth(this.person.getMaxHealth(player) * health/maxHealth);
        this.person.sendBar(player, message);
    }

    public void death() {
        Player player = this.person.getPlayer();
        player.setFireTicks(0);
        player.setFreezeTicks(0);
        player.setNoDamageTicks(60);
        if (dungeon != null) {
            player.setGameMode(GameMode.SPECTATOR);
            sendForAll(getInstance(), "death-dungeon", "{name}", this.person.getName());
            this.startDungeonRespawn(player);
        } else {
            teleportHome(player);
            sendForAll(getInstance(), "death-base", "{name}", this.person.getName());
        }
    }
    private void startDungeonRespawn(Player player) {
        new BukkitRunnable() {
            private int delay = 12;
            @Override
            public void run() {
                if (delay == 0) {
                    player.setGameMode(GameMode.SURVIVAL);
                    if (dungeon == null) {
                        Location loc = player.getRespawnLocation();
                        RPGPerson.this.teleport(loc == null ? Bukkit.getWorld("world").getSpawnLocation() : loc);
                    } else {
                        dungeon.teleportPerson(RPGPerson.this);
                        player.playSound(player, Sound.BLOCK_ANVIL_BREAK, 1, 1);
                    }
                    this.cancel();
                }
                person.sendBar(getInstance(), "dungeon-respawn", "{delay}", String.valueOf(delay));
                player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
                delay--;
            }
        }.runTaskTimer(getInstance(), 0L, 20L);
    }

    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }

    @Nullable
    public RPGSet getSet() {
        return set;
    }
    public void setSet(net.annova.object.item.base.Set base) {
        RPGSet set = null;
        if (base == null) {
            if (this.set == null) return;
            additionalHealth = 0;
            regeneration = 0f;
        } else {
            set = base.getSet();
            if (this.set == set) return;

            additionalHealth = set.health();
            regeneration = set.regeneration();
        }

        this.updateHealth();

        this.set = set;
    }
    public void updateSet(Player player) {
//        this.setSet(Set.getSet(player));
    }
    public void updateArmor(Player player) {
//        Player player = this.getPerson().getPlayer();
//        ItemStack[] armorContent = player.getEquipment().getArmorContents();
//        for (int i = 0; i < 4; i++) {
//            RPGItem piece = (RPGItem) getArmor(armorContent[i], i);
//            if (this.armor[i] == piece) continue;
//
//            this.armor[i] = piece;
//
//            if (!(piece instanceof RPGArmorActivity activity)) continue;
//            if (activity.repeating() == 0) continue;
//
//            int slot = i;
//            new BukkitRunnable() {
//                private final Player pl = RPGPerson.this.getPerson().getPlayer();
//
//                @Override
//                public void run() {
//                    if (armor[slot] != piece) this.cancel();
//                    if (!pl.isOnline()) return;
//
//                    activity.activity(pl);
//                }
//            }.runTaskTimer(getInstance(), 0, activity.repeating());
//        }
    }

    public RPGItem[] getRPGArmor() {
        return armor;
    }

    public void updateSpeed(float speed) {
        float plus = 0;
        if (speed != 0) {
            Material type = person.getPlayer().getLocation().add(0, -1, 0).getBlock().getType();
            for (String modifier : speedModifiers.keySet()) {
                String[] attribute = modifier.split(":");
                if (attribute.length > 2) if (type != Material.valueOf(attribute[2])) continue;
                plus += Float.parseFloat(attribute[1]);
            }
            plus += set == null ? 0 : set.speed();
        }
        this.person.setAdditionalSpeed(plus);

        this.person.setSpeed(speed);
    }

    public boolean isReady() {
        return ready;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Equipment getEquipment() {
        return equipment;
    }
    public StatusManager getStatusManager() {
        return statusManager;
    }
    public double getFinalValue(ValueType type) {
        return this.equipment.getFinalValue(type) + this.statusManager.getFinalValue(type);
    }
    public String getStringFinalValue(ValueType type) {
        String string;
        double value = (double) (int) (this.getFinalValue(type) * 100) / 100;
        if (value == 0)     return "{sc}" + value;
        else if (value > 0) return "{tc}+" + value;
        else                return "{fc}" + value;
    }
    private AttributeInstance blockInteraction, entityInteraction;
    private static final ResourceLocation blockRangeLocation = ResourceLocation.fromNamespaceAndPath("annovas", "block_range");
    private static final ResourceLocation entityRangeLocation = ResourceLocation.fromNamespaceAndPath("annovas", "entity_range");
    public void updateAttributes(Player player) {
        if (blockInteraction == null) {
            ServerPlayer server = ((CraftPlayer) player).getHandle();
            this.blockInteraction = server.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
            this.entityInteraction = server.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
        }
        blockInteraction.addOrUpdateTransientModifier(new AttributeModifier(
                blockRangeLocation, this.getFinalValue(ValueType.BLOCK_RANGE), AttributeModifier.Operation.ADD_VALUE
        ));
        entityInteraction.addOrUpdateTransientModifier(new AttributeModifier(
                entityRangeLocation, this.getFinalValue(ValueType.ENTITY_RANGE), AttributeModifier.Operation.ADD_VALUE
        ));
    }

    public List<ItemStack> getPendingItems() {
        return pendingItems;
    }
    public void addPendingItem(ItemStack toGive) {
        if (toGive == null || toGive.getType().equals(Material.AIR)) return;
        this.pendingItems.add(toGive);
    }
    public void clearPending() {
        this.pendingItems.clear();
    }

    public Map<String, Long> getSpeedModifiers() {
        return speedModifiers;
    }
    public long getSpeedModifier(String modifier) {
        return speedModifiers.get(modifier);
    }
    public void addSpeedModifier(String modifier) {
        speedModifiers.put(modifier, System.currentTimeMillis());
    }
    public void removeSpeedModifier(String modifier) {
        speedModifiers.remove(modifier);
    }

    public float getHealth() {
        return health;
    }
    public float getMaxHealth() {
        return maxHealth;
    }
    public float getRegeneration() {
        return regeneration;
    }
    public float getAdditionalHealth() {
        return additionalHealth;
    }

    @Nullable
    public SectorLocation getSectorLocation() {
        return sectorLocation;
    }
    public String getStringSectorLocation() {
        return sectorLocation == null
                ? this.person.getString(getInstance(), "sector-loc-empty")
                : "{mc}" + sectorLocation.getClass().getSimpleName().toLowerCase()
                ;
    }
    public void updateSectorLocation(Player player) {
        this.sectorLocation = null;

        WorldSector sector = worldSectors.get(player.getWorld());
        if (sector == null) return;
        Location playerLocation = player.getLocation();
        for (SectorLocation sectorLocation : sector.zones()) {
            if (sectorLocation.isOutside(playerLocation)) continue;
            this.sectorLocation = sectorLocation;
            break;
        }

        // Movement event
        if (sectorLocation == null) return;
        sectorLocation.movement(player);
    }

    public boolean isTeleportAccess() {
        return teleportAccess;
    }
    public void setTeleportAccess(boolean teleportAccess) {
        this.teleportAccess = teleportAccess;
    }
    public void teleport(Player player) {
        this.teleport(person.getPlayer(), player.getLocation());
    }

    public void teleport(Location location) {
        this.teleport(person.getPlayer(), location);
    }
    public void teleport(Player player, Location location) {
        this.teleportAccess = true;
        player.teleport(location);
    }

    public boolean isShowBar() {
        return showBar;
    }
    public void setShowBar(boolean showBar) {
        this.showBar = showBar;
    }

    public Set<RPGAdvancement> getAdvancements() {
        return advancements;
    }
    public void addAdvancement(RPGAdvancement advancement) {
        this.advancements.add(advancement);
    }
    public void removeAdvancement(RPGAdvancement advancement) {
        this.advancements.remove(advancement);
    }
    public void completeAdvancement(Player player, RPGAdvancement advancement) {
        if (this.advancements.contains(advancement)) return;
        this.advancements.add(advancement);
        AdvancementProgress progress = player.getAdvancementProgress(advancement.resolve());
        for (String criterion : progress.getRemainingCriteria()) {
            progress.awardCriteria(criterion);
        }
    }

    public static void teleportHome(Player player) {
        Location loc = player.getRespawnLocation();
        player.playSound(player, Sound.BLOCK_GLASS_BREAK, 3, 1);
        player.teleport(loc == null ? player.getWorld().getSpawnLocation() : loc);
    }
}
