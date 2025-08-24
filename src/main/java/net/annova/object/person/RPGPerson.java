package net.annova.object.person;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import com.google.common.base.Preconditions;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.annova.environment.civilization.Civilization;
import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.environment.dungeon.Dungeon;
import net.annova.lang.AISString;
import net.annova.object.person.equipment.ArmorSet;
import net.annova.object.person.advancement.RPGAdvancement;
import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.WorldSector;
import net.annova.main.AnnovaAPI;
import net.annova.main.AnnovaGlobal;
import net.annova.main.AnnovaSQL;
import net.annova.object.person.equipment.Equipment;
import net.annova.object.person.quest.QuestManager;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
import net.annova.object.person.status.ValueType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Set;

import static io.papermc.paper.event.entity.EntityKnockbackEvent.Cause.ENTITY_ATTACK;
import static net.annova.environment.world.GlobalWorld.getWorldSector;
import static net.annova.environment.world.GlobalWorld.worldSectors;
import static net.annova.event.ScoreboardRPGEvent.getInfo;
import static net.annova.lang.MessageObjects.sendForAll;
import static net.annova.main.AnnovaAPI.getMinecraftServer;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.addRPGPerson;
import static net.annova.main.GlobalRPG.getAreaChunk;
import static net.annova.util.AISUtil.joinList;
import static net.annova.util.AISUtil.loadList;
import static net.minecraft.world.entity.ai.attributes.Attributes.*;

public class RPGPerson {
    private final Person person;
    private Dungeon dungeon;
    private int dungeonPoints = 0;
    private Team team;
    private ArmorSet armorSet;
    private boolean ready = false;
    private final Equipment equipment = new Equipment(this);
    private final StatusManager statusManager = new StatusManager(this);
    private final List<ItemStack> pendingItems = new ArrayList<>();
    private final QuestManager questManager = new QuestManager();
    private boolean teleportAccess = false;

    private Town town;

    private SectorLocation sectorLocation;
    private Town currentArea;

    private TeleportTransition lastPosition;

    private final EnumSet<RPGAdvancement> advancements = EnumSet.noneOf(RPGAdvancement.class);
    private boolean scene = false;
    private final EnumSet<RPGScene> scenes = EnumSet.noneOf(RPGScene.class);

    private double health = 20;
    private double maxHealth = 20;
    private float baseJump = 0.41999998688697815f;

    public int minutesInDarkForest = 0;
    public int catchedTreasures = 0;

    private static final String TABLE = "RPGPERSON";
    public static void init() {
        AnnovaSQL sql = AnnovaAPI.getSQL();
        sql.initTable(TABLE,
                "NAME VARCHAR(24)",
                "QUEST_MANAGER MEDIUMTEXT",
                "POSITION VARCHAR(64)",
                "EQUIPMENT TEXT",
                "ADVANCEMENTS TEXT",
                "SCENES TEXT",

                "PRIMARY KEY (NAME)"
        );
        sql.loadFromDB(TABLE, RPGPerson.class);
    }
    public void save() {
        Map<String, Object> save = new HashMap<>();
        save.put("NAME", this.person.getName());
        save.put("QUEST_MANAGER", this.questManager.serializeQuests());
        if (lastPosition != null) {
            Vec3 pos = lastPosition.position();
            save.put("POSITION", lastPosition.newLevel().dimension().location().getPath() + "," + (int) (pos.x() * 100) / 100 + "," + (int) (pos.z() * 100) / 100 + "," + (int) (pos.y() * 100) / 100);
        }
        save.put("EQUIPMENT", this.equipment.serialize());
        save.put("ADVANCEMENTS", joinList(this.advancements, RPGAdvancement::name, ","));
        save.put("SCENES", joinList(this.scenes, RPGScene::name, ","));

        AnnovaAPI.getSQL().saveObject(TABLE, "NAME", save);
    }
    
    public RPGPerson(Person person) {
        this.person = person;
    }
    public RPGPerson(ResultSet rs) throws SQLException {
        this.person = AnnovaGlobal.getPerson(rs.getString("NAME"));
        this.questManager.deserialize(rs.getString("QUEST_MANAGER"));
        String[] position = rs.getString("POSITION").split(",");
        if (position.length > 1) {
            this.lastPosition = new TeleportTransition(getMinecraftServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.withDefaultNamespace(position[0].toLowerCase()))),
                    new Vec3(Integer.parseInt(position[1])+0.5, Integer.parseInt(position[2]), Integer.parseInt(position[3])+0.5),
                    Vec3.ZERO, 0, 0,
                    Set.of(), TeleportTransition.DO_NOTHING,
                    PlayerTeleportEvent.TeleportCause.PLUGIN
            );
        }
        this.equipment.deserialize(rs.getString("EQUIPMENT"));
        loadList(this.advancements, rs.getString("ADVANCEMENTS"), RPGAdvancement::valueOf, ",");
        loadList(this.scenes, rs.getString("SCENES"), RPGScene::valueOf, ",");

        addRPGPerson(this);
    }

    public Person getPerson() {
        return person;
    }
    public Player getPlayer() {
        return this.person.getPlayer();
    }
    public ServerPlayer getServerPlayer() {
        return this.person.getServerPlayer();
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

        this.person.sendBar(person.getAISString(getInstance(), "dungeon-points")
                .replaceTag("{plus}", String.valueOf(newPoints))
                .replaceTag("{points}", String.valueOf(dungeonPoints))
        );
        Player player = this.getPerson().getPlayer();
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
    }
    public void reduceDungeonPoints(int points) {
        this.dungeonPoints -= points;
    }

    public void damageWithCooldown(float damage) {
        this.damageWithCooldown(null, damage);
    }
    public void damageWithCooldown(Entity damager, float damage) {
        Player player = this.getPlayer();
        if (player.getNoDamageTicks() != 0) return;
        player.setNoDamageTicks(10);
        this.damage(damager, damage);
    }
    public void damage(double damage) {
        this.damage(null, (float) damage);
    }
    public void damage(float damage) {
        this.damage(null, damage);
    }

    public void damage(Entity damager, float damage) {
        if (this.scene) return;
        ServerPlayer server = this.getPerson().getServerPlayer();
        GameType mode = server.gameMode();
        if (mode == GameType.CREATIVE || mode == GameType.SPECTATOR) return;
        ServerLevel level = server.serverLevel();
        if (!worldSectors.containsKey(level)) return;

        ClientboundHurtAnimationPacket packet = new ClientboundHurtAnimationPacket(server.getId(), 90.0F);
        level.playSound(null, server.blockPosition(), SoundEvents.PLAYER_HURT, SoundSource.HOSTILE, 1, 1);
        for (ServerPlayer toSend : level.players()) toSend.connection.send(packet);

        this.knockback(damager, damage);

        this.setHealth(this.health - damage);
    }
    public void knockback(Entity damager, float damage) {
        if (damager == null) return;

        Player player = this.getPlayer();
        ServerPlayer server = ((CraftPlayer) player).getHandle();
        float force = (float) (0.6 * (1.0 - server.getAttributeValue(KNOCKBACK_RESISTANCE)));
        if (damage < 1) force*= damage;

        double dirX, dirZ;
        if (damager instanceof Projectile projectile) {
            DoubleDoubleImmutablePair pair = projectile.calculateHorizontalHurtKnockbackDirection(server, null);
            dirX = -pair.leftDouble();
            dirZ = -pair.rightDouble();
        } else {
            Vec3 vec3 = damager.position();
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
        EntityKnockbackByEntityEvent event = new EntityKnockbackByEntityEvent(player, damager.getBukkitEntity(),
                ENTITY_ATTACK, force,
                new Vector(
                        vec3d.x / 2.0 - knockback.x,
                        server.onGround ? Math.min(0.4, vec3d.y / 2.0 + force) : vec3d.y,
                        vec3d.z / 2.0 - knockback.z
                )
        );
        event.callEvent();
        if (event.isCancelled()) return;

        EntityKnockbackEvent finalEvent = new EntityKnockbackEvent(player, ENTITY_ATTACK, event.getKnockback());
        finalEvent.callEvent();
        if (finalEvent.isCancelled()) return;

        Vector vector = finalEvent.getKnockback();
        server.setDeltaMovement(new Vec3(vector.getX(), vector.getY(), vector.getZ()));
        server.hurtMarked = true;
    }

    public void regen() {
        Player player = this.getPlayer();

        float regeneration = 0;
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
    public void setHealth(double health) {
        if (health <= 0) {
            this.death();
            health = maxHealth;
        }
        if (health > maxHealth) health = maxHealth;
        this.health = health;
        this.updateStatusBar();
    }
    public void updateHealth() {
        Player player = person.getPlayer();
        if (player == null) return;

        this.maxHealth = 20 + this.getFinalValue(ValueType.HEALTH);
        if (this.maxHealth < health) this.health = maxHealth;
        this.updateStatusBar();
    }
    public void updateStatusBar() {
        if (person.isHideUI()) return;
        AISString message = this.person.getAISString(getInstance(), "bar-health")
                .replaceTag("{h}", String.valueOf((int) this.health))
                .replaceTag("{m}", String.valueOf((int) this.maxHealth));
        Player player = this.person.getPlayer();
        player.setHealth(this.person.getMaxHealth(player) * health/maxHealth);
        this.person.sendBar(message);
    }

    public void death() {
        Player player = this.person.getPlayer();
        player.setFireTicks(0);
        player.setFreezeTicks(0);
        player.setNoDamageTicks(60);
        if (dungeon != null) {
            player.setGameMode(GameMode.SPECTATOR);
            sendForAll(getInstance(), "death-dungeon", "{name}", this.person.getName());
            this.startDungeonRespawn();
        } else {
            teleportHome(player);
            sendForAll(getInstance(), "death-base", "{name}", this.person.getName());
        }
    }
    private void startDungeonRespawn() {
        Player player = this.getPlayer();
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

    @NotNull
    public ArmorSet getArmorSet() {
        return armorSet;
    }
    public ArmorSet updateArmorSet() {
        return this.armorSet = ArmorSet.getArmorSet(this.getPlayer());
    }

    public void updateSpeed(float speed) {
        float multiplier = 1;
        if (statusManager.hasStatus(StatusType.PHANTOM_FEAR)) multiplier += 0.05f;
        this.person.setSpeed(speed * multiplier, this.baseJump * multiplier);
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
    public void addStatus(StatusType type) {
        this.statusManager.addStatus(type);
    }
    public void addStatus(StatusType type, int durationOffset) {
        this.statusManager.addStatus(type, durationOffset);
    }

    public double getFinalValue(ValueType type) {
        return this.equipment.getFinalValue(type) + this.statusManager.getFinalValue(type);
    }
    public String getStringFinalValue(ValueType type) {
        return colorizeNumber((double) (int) (this.getFinalValue(type) * 100) / 100, false);
    }
    public String getStringFinalPercent(ValueType type) {
        return this.getStringFinalPercent(type, false);
    }
    public String getStringFinalPercent(ValueType type, boolean invert) {
        return colorizeNumber(this.getFinalValue(type)*100, invert) + '%';
    }
    public static String colorizeNumber(double number) {
        return colorizeNumber(number, false);
    }
    public static String colorizeNumber(double number, boolean invert) {
        if (invert) number = -number;
        if (number == 0)     return "{sc}" + number;
        else if (number > 0) return (invert ? "{fc}+" : "{tc}+") + number;
        else                 return (invert ? "{tc}" : "{fc}") + number;
    }
    private AttributeInstance blockInteraction, entityInteraction, movementSpeed, sneakingSpeed;
    private static final ResourceLocation blockRangeLocation = ResourceLocation.fromNamespaceAndPath("annovas", "block_range");
    private static final ResourceLocation entityRangeLocation = ResourceLocation.fromNamespaceAndPath("annovas", "entity_range");
    private static final ResourceLocation speedLocation = ResourceLocation.fromNamespaceAndPath("annovas", "entity_speed");
    public void updateAttributes() {
        if (blockInteraction == null) {
            ServerPlayer server = ((CraftPlayer) this.person.getPlayer()).getHandle();
            this.blockInteraction = server.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
            this.entityInteraction = server.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
            this.movementSpeed = server.getAttribute(MOVEMENT_SPEED);
            this.sneakingSpeed = server.getAttribute(SNEAKING_SPEED);
        }
        blockInteraction.addOrUpdateTransientModifier(new AttributeModifier(
                blockRangeLocation, this.getFinalValue(ValueType.BLOCK_RANGE), AttributeModifier.Operation.ADD_VALUE
        ));
        entityInteraction.addOrUpdateTransientModifier(new AttributeModifier(
                entityRangeLocation, this.getFinalValue(ValueType.ENTITY_RANGE), AttributeModifier.Operation.ADD_VALUE
        ));
        double finalSpeed = this.getFinalValue(ValueType.SPEED);
        movementSpeed.addOrUpdateTransientModifier(new AttributeModifier(
                speedLocation, finalSpeed, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ));
        sneakingSpeed.addOrUpdateTransientModifier(new AttributeModifier(
                speedLocation, finalSpeed, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
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

    public double getHealth() {
        return health;
    }
    public double getMaxHealth() {
        return maxHealth;
    }

    public Town getTown() {
        return town;
    }
    public Town getSelectedTown() {
        return town;
    }
    public void setTown(Town town) {
        this.town = town;
        if (this.person.isOnline()) {
            Scoreboard scoreboard = this.person.getScoreboard();
            scoreboard.initScoreBoard();
        }
    }

    @Nullable
    public SectorLocation getSectorLocation() {
        return sectorLocation;
    }
    public String getStringSectorLocation() {
        return sectorLocation == null
                ? "{sc}" + this.person.getAISString(getInstance(), "wasteland").getDefined().getString()
                : "{mc}" + sectorLocation.getClass().getSimpleName().toLowerCase()
                ;
    }
    public void updateSectorLocation() {
        this.sectorLocation = null;

        ServerPlayer player = this.getServerPlayer();
        WorldSector sector = getWorldSector(player.serverLevel());
        if (sector == null) return;
        BlockPos blockPos = player.blockPosition();
        for (SectorLocation sectorLocation : sector.zones()) {
            if (sectorLocation.isOutside(blockPos)) continue;
            this.sectorLocation = sectorLocation;
            break;
        }

        // Movement event
        if (sectorLocation == null) return;
        sectorLocation.movement(this, player);
    }

    public Town getCurrentArea() {
        return currentArea;
    }
    public void updateCurrentArea() {
        ServerLevel level = this.getServerPlayer().serverLevel();
        if (!worldSectors.containsKey(level)) return;
        AreaChunk newArea = getAreaChunk(level, this.getServerPlayer().chunkPosition());
        Town town = newArea == null ? null : newArea.getTown();
        if (town == this.currentArea) return;
        AISString message;
        if (newArea == null) {
            message = this.person.getAISString(getInstance(), "enter-wasteland");
        } else { message = this.person.getAISString(getInstance(), "enter-culture");
            Civilization civilization = town.getCivilization();
            message.replaceTag("{color}", civilization.getCitizens().contains(this) ? "{tc}" : "{fc}");
            message.replaceTag("{town}", town.getName());
            message.replaceTag("{civ}", civilization.getName());
        }
        this.person.sendTitle(new AISString(""), message, 10, 20, 5);
        this.currentArea = town;

        this.person.getScoreboard().updateScoreboard("culture", getInfo(this, "culture"));
    }

    public void tryLastPosition() {
        if (lastPosition != null && !this.scene) this.getServerPlayer().teleport(this.lastPosition);
        this.updateLastPosition();
    }
    public void updateLastPosition() {
        ServerPlayer player = this.getServerPlayer();
        this.lastPosition = new TeleportTransition(player.serverLevel(), player.position(),
                Vec3.ZERO, 0, 0,
                Set.of(), TeleportTransition.DO_NOTHING,
                PlayerTeleportEvent.TeleportCause.PLUGIN
        );
    }

    public boolean isTeleportAccess() {
        return teleportAccess;
    }
    public void setTeleportAccess(boolean teleportAccess) {
        this.teleportAccess = teleportAccess;
    }

    public void teleport(Location location) {
        this.teleportAccess = true;
        this.getPlayer().teleport(location);
    }
    public void teleport(Vec3i position, ServerLevel level) {
        this.teleport(Vec3.atCenterOf(position), level);
    }
    public void teleport(Vec3 position, ServerLevel level) {
        ServerPlayer player = this.getPerson().getServerPlayer();
        player.stopRiding();
        if (player.level() != level) {
            Preconditions.checkState(!player.generation, "Cannot teleport entity to an other world during world generation");
            player.teleport(new TeleportTransition(level, position, Vec3.ZERO, 0, 0, Set.of(), TeleportTransition.DO_NOTHING, PlayerTeleportEvent.TeleportCause.PLUGIN));
        } else {
            player.absSnapTo(position.x, position.y, position.z, 0, 0);
            player.setYHeadRot(0);
        }
    }

    public Set<RPGAdvancement> getAdvancements() {
        return advancements;
    }
    public boolean hasAdvancement(RPGAdvancement advancement) {
        return this.advancements.contains(advancement);
    }
    public void addAdvancement(RPGAdvancement advancement) {
        this.advancements.add(advancement);
    }
    public void removeAdvancement(RPGAdvancement advancement) {
        this.advancements.remove(advancement);
    }
    public void completeAdvancement(RPGAdvancement advancement) {
        if (this.advancements.contains(advancement)) return;
        this.advancements.add(advancement);
        AdvancementProgress progress = this.getPlayer().getAdvancementProgress(advancement.resolve());
        for (String criterion : progress.getRemainingCriteria()) {
            progress.awardCriteria(criterion);
        }

        advancement.getCategory().getActions().onAdvancement(this);
    }

    public boolean isScene() {
        return scene;
    }
    public void setScene(boolean scene) {
        this.scene = scene;
        this.person.setHideMessages(scene);
    }

    public EnumSet<RPGScene> getScenes() {
        return scenes;
    }
    public void triggerScene(RPGScene scene) {
        if (this.scenes.add(scene)) scene.start(this);
    }
    public void removeScene(RPGScene scene) {
        this.scenes.remove(scene);
    }

    public static void teleportHome(Player player) {
        Location loc = player.getRespawnLocation();
        player.playSound(player, Sound.BLOCK_GLASS_BREAK, 3, 1);
        player.teleport(loc == null ? player.getWorld().getSpawnLocation().toCenterLocation() : loc);
    }
}
