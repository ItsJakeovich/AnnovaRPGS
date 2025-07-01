package net.annova.environment.dungeon;

import net.annova.environment.dungeon.type.DungeonBase;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.item.base.ItemType;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import net.annova.object.SimpleStructure;
import net.annova.object.person.Team;
import net.annova.object.person.status.StatusManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.GlobalRPG.*;
import static net.annova.object.person.status.StatusType.CB_DAMAGE_DEBUFF;
import static net.annova.object.person.status.StatusType.HARRY_DEFENCE_DEBUFF;
import static net.annova.util.BukkitObjects.addItems;
import static net.annova.util.GlobalTasks.importBuild;
import static net.annova.util.GlobalTasks.removeImportedBuild;
import static net.annova.main.AnnovaRPG.getInstance;

public class Dungeon {
    private int key;
    private final DungeonBase base;
    private final Location summon;
    private Location location;
    private final Set<RPGPerson> players = new HashSet<>();
    private final Set<LivingEntity> entity = new HashSet<>();
    private boolean close = false;

    // null if dungeon summoned with key.
    // как нибудь позже было бы круто создавать портал
    public Dungeon(DungeonBase base) {
        this(base, null);
    }
    public Dungeon(DungeonBase base, Location summon) {
        this.base = base;
        this.summon = summon;
    }

    public int getKey() {
        return key;
    }

    public DungeonBase getBase() {
        return base;
    }

    @Nullable
    public Location getSummon() {
        return summon;
    }

    public Location getLocation() {
        return location;
    }

    public void addPlayers(Set<RPGPerson> persons) {
        this.players.addAll(persons);
    }
    public void addPlayer(RPGPerson person) {
        this.players.add(person);
    }
    public Set<RPGPerson> getPlayers() {
        return players;
    }

    public boolean isClose() {
        return close;
    }

    public boolean startIfTrue() {
        for (RPGPerson person : players) {
            if (!person.isReady()) return false;
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
        Dungeon.this.teleportAll();
        Dungeon.this.base.start(Dungeon.this);
        return true;
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
            if (!ent.valid) continue;
            ent.remove(Entity.RemovalReason.DISCARDED);
        }

        for (RPGPerson person : this.players) {
            StatusManager manager = person.getStatusManager();
            manager.removeStatus(CB_DAMAGE_DEBUFF);
            manager.removeStatus(HARRY_DEFENCE_DEBUFF);

            Player player = person.getPerson().getPlayer();

            int points = person.getDungeonPoints();
            ItemStack cont = points > 5 ? container.create(player, 1 + points/1000) : new ItemStack(Material.AIR);

            if (player == null) {
                person.addPendingItem(cont);
                continue;
            } else {
                Location location = player.getRespawnLocation();
                if (location == null) location = Bukkit.getWorld("world").getSpawnLocation();
                person.teleport(location);
                addItems(player, cont);
            }

            person.setDungeonPoints(0);
            person.setDungeon(null);
        }

        int i = 0;
        int y = (int) this.location.getY();
        for (SimpleStructure structure : base.getTemplate().structures) {
            Location loc = this.location.clone();
            loc.setY(y+80*i);
            removeImportedBuild(structure, loc);
            i++;
        }
        removeDungeon(this.key);

        if (force) return;

        RPGPerson person = (RPGPerson) players.toArray()[0];
        Team team = person.getTeam();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Person pl = getPerson(player);
            if (team == null) {
                pl.sendAndBar(pl.getString(getInstance(), "dungeon-complete-person",
                        "{person}", person.getPerson().getName()));
            } else {
                pl.sendAndBar(pl.getString(getInstance(), "dungeon-complete-team")
                        .replace("{team}", team.getName())
                        .replace("{leader}", team.getLeader().getPerson().getName())
                );
            }
        }
    }

    public void createDungeon() {
        World world = getDungeonWorld();

        DungeonTemplate dungeonTemplate = base.getTemplate();
        if (getDungeons().isEmpty()) {
            location = new Location(world, 0, 0, 0);
        } else {
            int key = 0;
            for (Dungeon dungeon : getDungeons()) {
                if (dungeon.getKey() > key) key = dungeon.getKey();
            }
            location = getDungeon(key).getLocation().clone()
                    .add(dungeonTemplate.getStructure().getMaxX()+25, 0, 0);
        }

        SimpleStructure structure = dungeonTemplate.getStructure();
        importBuild(structure, Person.CardinalDirection.NORTH, location);
        dungeonTemplate.updateLootChests(structure, location);
        dungeonTemplate.updateAnimations(structure, location);

        this.key = getDungeons().size();
        addDungeon(key, this);
    }

    public void teleportPerson(RPGPerson person) {
        DungeonTemplate dungeonTemplate = this.base.getTemplate();
        person.teleport(this.location.clone().add(
                dungeonTemplate.getStructure().getLocationWithPoint(getDungeonWorld(), dungeonTemplate.getSpawn())));
    }
    public void teleportAll() {
        this.players.forEach(this::teleportPerson);
    }

    public void send(String url, String from, String to) {
        for (RPGPerson pers : this.players) {
            Person person = pers.getPerson();
            String message = person.getString(getInstance(), url);
            person.sendAndBar(message.replace(from, to));
        }
    }
    public void send(String url) {
        for (RPGPerson pers : this.players) {
            Person person = pers.getPerson();
            person.sendAndBar(person.getString(getInstance(), url));
        }
    }

    public void spawnEntity(RPGEntity entity, Location location) {
        float value = players.size()-1;
        this.entity.add(entity.create(this, location, false, value, (float) (1 + (value*0.3)), (float) (1 + (value*0.02))));
    }
}
