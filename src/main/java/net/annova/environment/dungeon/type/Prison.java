package net.annova.environment.dungeon.type;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.person.RPGPerson;
import net.annova.object.SimpleStructure;
import net.annova.object.animation.Animation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.*;

public class Prison implements DungeonBase {
    private final DungeonTemplate template;
    private Dungeon dungeon;
    private int maxDistanceSquared;
    private Location dungeonCenter;
    public Location bossLoc = null;

    public Prison() {
        template = getDTemplate(0).clone();
    }

    @Override
    public DungeonTemplate getTemplate() {
        return template;
    }

    @Override
    public void start(Dungeon dungeon) {
        this.dungeon = dungeon;

        SimpleStructure structure = template.getStructure();
        int x = structure.getMaxX() >> 1, y = structure.getMaxY() >> 1, z = structure.getMaxZ() >> 1;
        this.maxDistanceSquared = (int) Math.pow(Math.max(Math.max(x, z), y) * 1.25, 2);
        this.dungeonCenter = dungeon.getLocation().clone().add(x, y, z);

        Location location = dungeon.getLocation();
        this.bossLoc = location.clone().add(template.getStructure().getLocationWithPoint(getDungeonWorld(), template.getEntities().get(101).getFirst().getPoint()));

        template.getAnimation("gate", 8).start(location, structure);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (dungeon.isClose()) this.cancel();
                else {
                    if (!Prison.this.mechanic()) return;

                    template.getAnimation("boss_gate", 0).start(location, structure);
                    this.cancel();
                }
            }
        }.runTaskTimer(getInstance(), 0L, 600L);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (dungeon.isClose()) this.cancel();
                else Prison.this.trigger();
            }
        }.runTaskTimer(getInstance(), 0L, 100L);
    }

    public int key = 0;
    /**
     * @return true when mechanic is end.
     */
    public boolean mechanic() {
        Animation animation = template.getAnimation("gate", key);
        Location location = dungeon.getLocation();
        animation.start(location, template.getStructure());

        for (RPGEntity type : template.getEntities().get(key)) {
            int point = type.getPoint();
            dungeon.spawnEntity(type, location.clone().add(template.getStructure().getLocationWithPoint(location.getWorld(), point)));
        }
        // -1 because its starts from 0, -1 because 8 is a welcome-gate.
        return key++ == template.getAnimations().get("gate").size()-2;
    }

    public void trigger() {
        boolean end = true;
        boolean spawnBoss = true;
        for (RPGPerson person : this.dungeon.getPlayers()) {
            Player player = person.getPerson().getPlayer();
            if (player == null) continue;
            Location location = player.getLocation();
            if (bossLoc == null || player.getGameMode().equals(GameMode.SPECTATOR) || location.distanceSquared(bossLoc) > 100) spawnBoss = false;

            SimpleStructure structure = template.getStructure();

            if (location.distanceSquared(dungeonCenter) > maxDistanceSquared) {
                dungeon.teleportPerson(person);
            }
        } /*if (end) {
            this.dungeon.endDungeon(true);
            return;
        }*/ if (bossLoc != null && spawnBoss) {
            template.getEntities().get(101).forEach(ent -> dungeon.spawnEntity(ent, bossLoc));
            template.getAnimation("boss_gate", 1).start(dungeon.getLocation(), template.getStructure());
            bossLoc = null;
        }
    }

    @Override
    public Inventory merchant() {
        return Bukkit.createInventory(null, 9);
    }
}
