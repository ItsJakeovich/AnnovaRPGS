package net.annova.environment.dungeon.type;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.person.RPGPerson;
import net.annova.object.SimpleStructure;
import net.annova.object.animation.Animation;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.*;

public class Prison implements DungeonBase {
    private final DungeonTemplate template;
    private Dungeon dungeon;
    private int maxDistanceSquared;
    private Vec3i dungeonCenter;
    public Vec3i bossLoc = null;

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
        this.dungeonCenter = dungeon.getPosition().offset(x, y, z);

        Vec3i position = dungeon.getPosition();
        this.bossLoc = position.offset(template.getStructure().getPositionWithPoint(template.getEntities().get(101).getFirst().getPoint()));

        ServerLevel level = dungeon.getLevel();
        template.getAnimation("gate", 8).start(level, position, structure);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (dungeon.isClose()) this.cancel();
                else {
                    if (!Prison.this.mechanic()) return;

                    template.getAnimation("boss_gate", 0).start(level, position, structure);
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
        Vec3i position = dungeon.getPosition();
        ServerLevel level = dungeon.getLevel();
        animation.start(level, position, template.getStructure());

        for (RPGEntity type : template.getEntities().get(key)) {
            int point = type.getPoint();
            dungeon.spawnEntity(type, position.offset(template.getStructure().getPositionWithPoint(point)));
        }
        // -1 because its starts from 0, -1 because 8 is a welcome-gate.
        return key++ == template.getAnimations().get("gate").size()-2;
    }

    public void trigger() {
        boolean end = true;
        boolean spawnBoss = true;
        for (RPGPerson person : this.dungeon.getPlayers()) {
            ServerPlayer player = person.getPerson().getServerPlayer();
            if (player == null) continue;
            Vec3 position = player.position();
            if (bossLoc == null || player.gameMode() == GameType.SPECTATOR || bossLoc.distToCenterSqr(position) > 100)
                spawnBoss = false;

            SimpleStructure structure = template.getStructure();

            if (dungeonCenter.distToCenterSqr(position) > maxDistanceSquared) {
                dungeon.teleportPerson(person);
            }
        }
        if (bossLoc != null && spawnBoss) {
            template.getEntities().get(101).forEach(ent -> dungeon.spawnEntity(ent, bossLoc));
            template.getAnimation("boss_gate", 1).start(dungeon.getLevel(), dungeon.getPosition(), template.getStructure());
            bossLoc = null;
        }
    }

    @Override
    public Inventory merchant() {
        return Bukkit.createInventory(null, 9);
    }
}
