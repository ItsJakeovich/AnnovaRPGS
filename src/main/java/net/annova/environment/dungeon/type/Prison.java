package net.annova.environment.dungeon.type;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.entity.core.RPGEntity;
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

        Location location = dungeon.getLocation();
        SimpleStructure structure = template.getStructure();
        template.getAnimation("gate", 8).start(location, structure);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (dungeon.isClose()) cancel();
                if (!mechanic()) return;

                template.getAnimation("boss_gate", 0).start(location, structure);
                cancel();
            }
        }.runTaskTimer(getInstance(), 0L, 600L);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (dungeon.isClose()) cancel();
                trigger();
            }
        }.runTaskTimer(getInstance(), 0L, 100L);

        bossLoc = location.clone().add(template.getStructure().getLocationWithPoint(getDungeonWorld(), template.getEntities().get(101).getFirst().getPoint()));
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
        for (RPGPerson person : this.dungeon.getPlayers()) {
            Player player = person.getPerson().getPlayer();
            if (player == null) continue;
            if (player.getWorld().equals(getDungeonWorld())) {
                end = false;
                break;
            }
        } if (end) {
            this.dungeon.endDungeon(true);
            return;
        }

        for (RPGPerson person : this.dungeon.getPlayers()) {
            Player player = person.getPerson().getPlayer();
            if (player == null) continue;
            SimpleStructure structure = template.getStructure();

            int x = structure.getMaxX() >> 1, y = structure.getMaxY() >> 1, z = structure.getMaxZ() >> 1;
            int distance = (int) (Math.max(Math.max(x, z), y) * 1.25);
            if (player.getLocation().distanceSquared(dungeon.getLocation().clone().add(x, y, z)) > distance*distance) {
                dungeon.teleportPerson(person);
            }
        }

        if (bossLoc == null) return;

        for (RPGPerson person : dungeon.getPlayers()) {
            Player player = person.getPerson().getPlayer();
            if (player == null) continue;
            if (player.getGameMode().equals(GameMode.SPECTATOR)) return;
            if (player.getLocation().distanceSquared(bossLoc) > 100) return;
        }

        template.getEntities().get(101).forEach(ent -> dungeon.spawnEntity(ent, bossLoc));
        template.getAnimation("boss_gate", 1).start(dungeon.getLocation(), template.getStructure());
        bossLoc = null;
    }

    @Override
    public Inventory merchant() {
        Inventory inv = Bukkit.createInventory(null, 9);
        return inv;
    }
}
