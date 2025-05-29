package net.annova.event;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.annova.item.component.ForiumFragmentItem;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.Team;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.*;
import static net.annova.util.BukkitObjects.chance;

public class DeathRPGEvent implements Listener {
    @EventHandler
    public void death(PlayerDeathEvent event) {
        Player player = event.getEntity();
        RPGPerson person = getRPGPerson(player);
        new BukkitRunnable() {
            private final Location loc = player.getLocation();
            public void run() {
                if (event.getEntity().isValid()) return;
                event.getEntity().spigot().respawn();
                Dungeon dungeon = person.getDungeon();
                if (dungeon != null) {
                    person.teleport(loc);
                    player.setGameMode(GameMode.SPECTATOR);
                    event.setDeathMessage(person.getPerson().getString(getInstance(), "death-dungeon"));
                    updateRespawn(person, player);
                } else {
                    event.setDeathMessage(person.getPerson().getString(getInstance(), "death-base"));
                }

                cancel();
            }
        }.runTaskTimer(getInstance(), 1L, 0L);
    }
    private void updateRespawn(RPGPerson person, Player player) {
        new BukkitRunnable() {
            private int delay = 12;
            private final Person pers = person.getPerson();
            @Override
            public void run() {
                if (delay == 0) {
                    player.setGameMode(GameMode.SURVIVAL);
                    Dungeon dungeon = person.getDungeon();
                    if (dungeon == null) {
                        Location loc = player.getRespawnLocation();
                        person.teleport(loc == null ? Bukkit.getWorld("world").getSpawnLocation() : loc);
                    } else {
                        dungeon.teleportPerson(person);
                        player.playSound(player, Sound.BLOCK_ANVIL_BREAK, 1, 1);
                    }
                    cancel();
                }
                pers.sendBar(pers.getString(getInstance(), "dungeon-respawn", "{delay}", String.valueOf(delay)));
                player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
                delay--;
            }
        }.runTaskTimer(getInstance(), 0L, 20L);
    }

    @EventHandler
    public void drop(EntityDeathEvent event) {
        LivingEntity ent = event.getEntity();
        RPGEntity entity = getEntity(ent);
        if (entity == null) return;
        List<ItemStack> drop = event.getDrops();
        event.getDrops().clear();
        drop.addAll(entity.loot());

        Player player = ent.getKiller();
        if (player == null) return;
        RPGPerson person = getRPGPerson(player);
        Set<Ability> abilities = entity.abilities();
        if (abilities.contains(Ability.DROP_FORIUM)) drop.add(new ForiumFragmentItem().create(person, 0.1));
    }
    @EventHandler
    public void death(EntityDeathEvent event) {
        LivingEntity ent = event.getEntity();
        RPGEntity entity = getEntity(ent);
        if (entity == null) return;
        removeEntity(ent);

        Dungeon dungeon = entity.getDungeon();
        if (dungeon == null) return;

        RPGPerson person = getRPGPerson(ent.getKiller());
        if (person == null) person = (RPGPerson) dungeon.getPlayers().toArray()[0];

        switch (entity.getId()) {
            case 0,11 -> person.addDungeonPoints(2);
            case 1,2,3,4,5,6,7,8 -> person.addDungeonPoints(200);
            // zero
            case 9 -> {
                person.addDungeonPoints(1000);
                getDungeonWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, ent.getLocation(), 3);
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
            // major
            case 10 -> {
                person.addDungeonPoints(1000);
                dungeon.endDungeon(120);
                Team team = person.getTeam();
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    Person pr = getPerson(pl);
                    if (team == null) {
                        pr.send(pr.getString(getInstance(), "dungeon-killed-major-person",
                                "{person}", person.getPerson().getName()));
                    } else {
                        pr.send(pr.getString(getInstance(), "dungeon-killed-major-team")
                                .replace("{team}", team.getName())
                                .replace("{leader}", team.getLeader().getPerson().getName())
                        );
                    }
                }
            }
        }
    }
}
