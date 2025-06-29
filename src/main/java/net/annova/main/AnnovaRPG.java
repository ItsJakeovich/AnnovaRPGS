package net.annova.main;

import net.annova.config.RPGConfig;
import net.annova.object.entity.core.RPGEntity;
import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.environment.structure.Structure;
import net.annova.environment.structure.StructureBase;
import net.annova.object.person.RPGPerson;
import net.annova.thread.*;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static net.annova.command.AnnovaCommandInit.initCommands;
import static net.annova.event.summon.EntityEvent.indicators;
import static net.annova.main.AnnovaAPI.initEvents;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaGlobal.loadAllStructures;
import static net.annova.main.GlobalRPG.*;
import static net.annova.thread.TaskMaster.syncTimer;

public final class AnnovaRPG extends JavaPlugin {
    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        try {
            new RPGConfig().init();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        try {
            initEvents(getInstance(), "net.annova");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        try {
            initCommands("RPGCommand", "net.annova.command");
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.initThreads();
        loadAllStructures();
        DungeonTemplate.createDungeonWorld();

        this.initialization();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!containsRPGPerson(player)) {
                addRPGPerson(getPerson(player));
            }
        }
    }

    @Override
    public void onDisable() {
        for (RPGEntity entity : getEntities()) {
            entity.entity.remove(Entity.RemovalReason.DISCARDED);
        }
        for (ArmorStand indicator : indicators) {
            indicator.remove();
        }
        for (Dungeon dungeon : getDungeons()) {
            dungeon.endDungeon(true);
        }
        AnnovaRPG.saveAll();
    }

    public static void saveAll() {
        for (Structure structure : getStructures()) {
            StructureBase base = structure.getBase();
            if (base.isSave()) {
                base.save();
            }
        }
        for (RPGPerson person : getRPGPersons()) {
            person.save();
        }
    }

    private void initThreads() {
        syncTimer(this, new PersonUpdateTimer(), 100); // 5s
        // PersonStaminaTimer - SecondTickEvent
        syncTimer(this, new UpdateSpawnTimer(), 1200); // 60sec
        // UpdateSectorTimer - SecondTickEvent
        // PersonStatusTimer - SecondTickEvent
    }
    private void initialization() {
        // SQL
        RPGPerson.init();
        RPGEntity.init();
        StructureBase.init();
    }

    public static JavaPlugin getInstance() {
        return plugin;
    }
}
