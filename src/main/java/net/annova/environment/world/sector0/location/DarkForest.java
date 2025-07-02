package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.GlobalRPG.getSoundFromString;
import static net.annova.object.entity.core.RPGEntityType.*;
import static net.annova.object.item.base.ArmorSet.CHITIN;
import static net.annova.util.BukkitObjects.random;

public class DarkForest extends SectorLocation implements Listener {
    private final Map<Player, Long> forestMSGCooldown = new HashMap<>();
    private final String[] MSGKeys = {
            "s0-forest-msg-0",
            "s0-forest-msg-1",
            "s0-forest-msg-2",
            "s0-forest-msg-3",
            "s0-forest-msg-4",
            "s0-forest-msg-5",
            "s0-forest-msg-6",
            "s0-forest-msg-7",
            "s0-forest-msg-8",
            "s0-forest-msg-9",
    };
    private final Map<Player, Long> soundCooldown = new HashMap<>();
    private final String[] soundKeys = {
            "location.forest.ambient.ambient1",
            "location.forest.ambient.ambient2",
            "location.forest.ambient.ambient3"
    };
    private final PotionEffect darkness = new PotionEffect(PotionEffectType.DARKNESS, 300, 1);

    private int i = 60; // 60 minutes
    @Override
    public void activity() {
        if (i != 0) {
            i--;
            return;
        } i = 60;
        for (Player player : world0.getPlayers()) {
            RPGPerson person = getRPGPerson(player);
            if (person.getSectorLocation() instanceof DarkForest) {
                player.playSound(getSoundFromString(нету), 1, 1);
            }
        }
    }

    @Override
    public void movement(RPGPerson person, Player player) {
        if (player.getGameMode().equals(GameMode.SURVIVAL)) {
            player.spawnParticle(Particle.DRAGON_BREATH, player.getLocation().add(0, 0.1, 0), 100, 15, 0, 15, 0);
            if (!person.getStatusManager().hasStatus(StatusType.ABSORB_NIGHT_BUFF)) player.addPotionEffect(darkness);
        }

        long millis = System.currentTimeMillis();
        if (!soundCooldown.containsKey(player) || millis > soundCooldown.get(player)) {
            soundCooldown.put(player, millis + 300_000);
            player.playSound(player, soundKeys[random.nextInt(soundKeys.length)], 1, 1);
        }
        if (forestMSGCooldown.containsKey(player) &&  millis < forestMSGCooldown.get(player)) return;
        forestMSGCooldown.put(player, millis + 1800_000); // 30 min
        getPerson(player).send(getInstance(), MSGKeys[random.nextInt(MSGKeys.length)]);
    }

    private static final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_FOREST, OLDER_ENT, DARK_ELF);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    private static final Location[] points = new Location[] {
        new org.bukkit.Location(world0, -280, 69, -351)
    };
    @Override
    public org.bukkit.Location[] points() {
        return points;
    }

    @Override
    public int squaredRadius() {
        return 17781;
    }

    @EventHandler
    public void fire(BlockIgniteEvent event) {
        if (this.isOutside(event.getBlock().getLocation())) return;
        event.setCancelled(true);
    }
}