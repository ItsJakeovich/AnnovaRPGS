package net.annova.environment.dungeon;

import net.annova.environment.dungeon.type.DungeonBase;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.item.base.ItemType;
import net.annova.object.person.Person;
import net.annova.object.person.RPGPerson;
import net.annova.object.SimpleStructure;
import net.annova.object.person.Team;
import net.annova.object.person.status.StatusManager;
import net.annova.lang.AISString;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

import static net.annova.environment.dungeon.GlobalDungeon.*;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.MAGIC_ORE;
import static net.annova.object.person.status.StatusType.*;
import static net.annova.util.BukkitObjects.addItems;
import static net.annova.util.GlobalTasks.importBuild;
import static net.annova.util.GlobalTasks.removeImportedBuild;
import static net.annova.main.AnnovaRPG.getInstance;

public class Dungeon {
    private int key;
    private final DungeonBase base;
    private final Vec3i summon;
    private final ServerLevel from;
    private final TeleportTransition summonTransition;
    private Vec3i position;
    private Vec3 spawnPosition;
    private TeleportTransition spawnTransition;
    private final Set<RPGPerson> players = new HashSet<>();
    private final Set<LivingEntity> entity = new HashSet<>();
    private boolean start = false;
    private boolean close = false;

    // null if dungeon summoned with key.
    // как нибудь позже было бы круто создавать портал
    public Dungeon(DungeonBase base, Vec3i summon, ServerLevel from) {
        this.base = base;
        this.summon = summon;
        this.from = from;

        this.summonTransition = new TeleportTransition(from, Vec3.atBottomCenterOf(summon), Vec3.ZERO, 0, 0, TeleportTransition.PLAY_PORTAL_SOUND);
    }

    public int getKey() {
        return key;
    }

    public DungeonBase getBase() {
        return base;
    }

    public Vec3i getSummonPosition() {
        return summon;
    }
    public ServerLevel getSummonLevel() {
        return from;
    }
    public TeleportTransition getSummonTransition() {
        return summonTransition;
    }

    public Vec3i getPosition() {
        return position;
    }

    public Vec3 getSpawnPosition() {
        return spawnPosition;
    }
    public Vec3i getVec3iSpawnPosition() {
        return new Vec3i(Mth.floor(spawnPosition.x), Mth.floor(spawnPosition.y), Mth.floor(spawnPosition.z));
    }
    public ServerLevel getLevel() {
        return getDungeonWorld();
    }

    public TeleportTransition getSpawnTransition() {
        return spawnTransition;
    }

    public void addPlayers(Set<RPGPerson> persons) {
        this.players.addAll(persons);
    }
    public void addPlayer(RPGPerson person) {
        this.players.add(person);
    }
    public void removePlayer(RPGPerson person) {
        this.players.remove(person);
    }
    public Set<RPGPerson> getPlayers() {
        return players;
    }

    public boolean isClose() {
        return close;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    public boolean isStart() {
        return start;
    }

    public void startIfTrue() {
        for (RPGPerson person : players) {
            if (!person.isReady()) return;
        }

        /*new BukkitRunnable() {
            private int timer = 12;
            @Override
            public void run() {
                Dungeon.this.send("dungeon-starts", "{s}", String.valueOf(timer));
                timer--;
                if (!Dungeon.this.startIfTrue()) {
//                    addItems(player, itemType.create(player));
                    for (RPGPerson ready : Dungeon.this.getPlayers()) {
                        ready.setReady(false);
                    }
                    Dungeon.this.send("dungeon-failstart");
                    this.cancel();
                    return;
                }
                if (timer == 0) {
                    Dungeon.this.players.forEach(player -> player.setReady(false));
                    Dungeon.this.teleportAll();
                    Dungeon.this.base.start(Dungeon.this);
                    this.cancel();
                }
            }
        }.runTaskTimer(getInstance(), 0L, 20);*/
        Dungeon.this.players.forEach(player -> player.setReady(false));
        //Dungeon.this.teleportAll();
        Dungeon.this.base.start(Dungeon.this);
    }
    public void endDungeon() {
        this.endDungeon(false);
    }
    public void endDungeon(long timer) {
        if (timer <= 0) {
            this.endDungeon();
            return;
        }
        for (RPGPerson member : this.players) {
            member.getPerson().sendBar(getInstance(), "dungeon-end-timer", "{timer}", String.valueOf(timer/20));
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(),
                () -> this.endDungeon(timer-20), 20L);
    }
    public void endDungeon(boolean force) {
        this.close = true;
        ItemType container = switch (base.getTemplate().getId()) {
            case 0 -> ItemType.PRISON_CONTAINER;
            default -> throw new IllegalStateException("Unexpected value: " + key);
        };
        for (LivingEntity ent : this.entity) {
            ent.remove(Entity.RemovalReason.DISCARDED, EntityRemoveEvent.Cause.DISCARD);
        }

        for (RPGPerson person : this.players) {
            StatusManager manager = person.getStatusManager();
            manager.removeStatus(CB_DAMAGE_DEBUFF);
            manager.removeStatus(HARRY_DEFENCE_DEBUFF);

            Player player = person.getPerson().getPlayer();

            int points = person.getDungeonPoints();
            ItemStack cont = points > 5 ? CraftItemStack.asBukkitCopy(container.create(person, 1 + points/1000)) : new ItemStack(Material.AIR);

            if (player == null) {
                person.addPendingItem(cont);
            } else {
                ServerPlayer serverPlayer = person.getServerPlayer();
                serverPlayer.teleport(this.summonTransition);
                serverPlayer.invulnerableTime = 100;
                addItems(player, cont);
                addItems(player, MAGIC_ORE.create(16));
            }

            person.setDungeonPoints(0);
            person.setDungeon(null);
        }

        int i = 0;
        int y = this.position.getY();
        for (SimpleStructure structure : base.getTemplate().structures) {
            removeImportedBuild(structure, this.position.above(80*i), getDungeonWorld());
            i++;
        }
        removeDungeon(this);

        if (force) return;

        RPGPerson person = (RPGPerson) players.toArray()[0];
        Team team = person.getTeam();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Person pl = getPerson(player);
            if (team == null) {
                pl.sendAndBar(pl.getAISString(getInstance(), "dungeon-complete-person",
                        "{person}", person.getPerson().getName()));
            } else {
                pl.sendAndBar(pl.getAISString(getInstance(), "dungeon-complete-team")
                        .replaceTag("{team}", team.getName())
                        .replaceTag("{leader}", team.getLeader().getPerson().getName())
                );
            }
        }
    }

    public void createDungeon() {
        ServerLevel level = getDungeonWorld();
        for (Player player : Bukkit.getOnlinePlayers()) {
            getRPGPerson(player).addStatus(SHAKING, 3_000);
        }

        DungeonTemplate dungeonTemplate = base.getTemplate();
        if (getDungeons().isEmpty()) {
            position = new Vec3i(0, 0, 0);
        } else {
            int key = 0;
            for (Dungeon dungeon : getDungeons()) {
                if (dungeon.getKey() > key) key = dungeon.getKey();
            }
            this.position = getDungeon(key).position.north();
            position = getDungeon(key).getPosition().offset(
                    dungeonTemplate.getStructure().getMaxX()+25,
                    0,
                    0
            );
        }
        this.spawnPosition = Vec3.atBottomCenterOf(
                this.position.offset(dungeonTemplate.getStructure().getPositionWithPoint(dungeonTemplate.getSpawn()))
        );

        SimpleStructure structure = dungeonTemplate.getStructure();
        importBuild(structure, Person.CardinalDirection.NORTH, position, level);
        dungeonTemplate.updateLootChests(structure, position, level);
        dungeonTemplate.updateAnimations(structure, position, level);

        this.spawnTransition = new TeleportTransition(level, this.spawnPosition, Vec3.ZERO, 0, 0, TeleportTransition.PLAY_PORTAL_SOUND);

        this.key = getDungeons().size();
        addDungeon(key, this);
    }

    public void teleportPerson(RPGPerson person) {
        person.teleport(this.spawnPosition, getDungeonWorld());
    }
    public void teleportAll() {
        this.players.forEach(this::teleportPerson);
    }

    public void send(String url, String from, String to) {
        for (RPGPerson pers : this.players) {
            Person person = pers.getPerson();
            AISString message = person.getAISString(getInstance(), url);
            person.sendAndBar(message.replaceTag(from, to));
        }
    }
    public void send(String url) {
        for (RPGPerson pers : this.players) {
            Person person = pers.getPerson();
            person.sendAndBar(person.getAISString(getInstance(), url));
        }
    }

    public void spawnEntity(RPGEntity entity, Vec3i position) {
        if (position == null) return;
        this.spawnEntity(entity, Vec3.atBottomCenterOf(position));
    }
    public void spawnEntity(RPGEntity entity, Vec3 position) {
        float value = players.size()-1;
        this.entity.add(entity.create(this, position,getDungeonWorld(), false, value, (float) (1 + (value*0.3)), (float) (1 + (value*0.02))));
    }
}
