package net.annova.object.entity.event;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.environment.world.SectorLocation;
import net.annova.object.entity.core.RPGEntity;
import net.annova.event.custom.RPGEntityDeathCE;
import net.annova.object.Person;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.Team;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.*;
import static net.annova.util.BukkitObjects.chance;

public class DeathEvent implements Listener {
    @EventHandler
    public void death(EntityDeathEvent event) {
        LivingEntity ent = event.getEntity();
        RPGEntity entity = getEntity(ent);
        if (entity == null) return;
        // Loot event
        List<ItemStack> drop = event.getDrops();
        event.getDrops().clear();
        for (net.minecraft.world.item.ItemStack item : entity.loot(getPerson(ent.getKiller()))) {
            drop.add(CraftItemStack.asBukkitCopy(item));
        }

        if (entity instanceof EntityAction action) {
            action.onDeath();
        }
        if (entity.getRPGType().isBoss()) {
            SectorLocation sectorLocation = entity.getSectorLocation();
            if (sectorLocation != null) sectorLocation.setBoss(null);
        }

        RPGEntityDeathCE ev = new RPGEntityDeathCE(entity, ent.getKiller(), event);
        Bukkit.getPluginManager().callEvent(ev);
        removeEntity(ent);

        RPGEntityType type = RPGEntityType.valueOf(entity.getId());
        this.checkDungeonDeath(entity, ent, type);
    }

    private void checkDungeonDeath(RPGEntity entity, LivingEntity living, RPGEntityType type) {
        Dungeon dungeon = entity.getDungeon();
        if (dungeon == null) return;

        RPGPerson person = getRPGPerson(living.getKiller());
        if (person == null) person = (RPGPerson) dungeon.getPlayers().toArray()[0];
        if (person == null) return;

        switch (type) {
            case PRISONER, SECURITY -> person.addDungeonPoints(2);
            case ZERO -> {
                person.addDungeonPoints(1000);
                getDungeonWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, entity.getLocation(), 3);
                if (chance(0.125)) {
                    DungeonTemplate dungeonTemplate = dungeon.getBase().getTemplate();
                    dungeonTemplate.getAnimation("boss_gate", 1).start(dungeon.getLocation(), dungeonTemplate.getStructure());
                    dungeonTemplate.getAnimation("mb_gate", 0).start(dungeon.getLocation(), dungeonTemplate.getStructure());

                    List<RPGEntity> entities = dungeonTemplate.getEntities().get(102);
                    entities.forEach(major -> dungeon.spawnEntity(major, dungeon.getLocation().clone()
                            .add(dungeonTemplate.getStructure().getLocationWithPoint(getDungeonWorld(), entities.getFirst().getPoint()))));

                    dungeon.send("dungeon-prison-major");
                } else dungeon.endDungeon(240);
            }
            case MAJOR -> {
                person.addDungeonPoints(1000);
                dungeon.endDungeon(120);
                Team team = person.getTeam();
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    Person pr = getPerson(pl);
                    if (team == null) {
                        pr.send(getInstance(), "dungeon-killed-major-person",
                                "{person}", person.getPerson().getName());
                    } else {
                        pr.send(pr.getAISString(getInstance(), "dungeon-killed-major-team")
                                .replaceTag("{team}", team.getName())
                                .replaceTag("{leader}", team.getLeader().getPerson().getName())
                        );
                    }
                }
            }
            default -> person.addDungeonPoints(200);
        }
    }
}
