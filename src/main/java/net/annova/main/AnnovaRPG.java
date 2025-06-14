package net.annova.main;

import net.annova.config.RPGConfig;
import net.annova.environment.world.GlobalWorld;
import net.annova.object.entity.core.RPGEntity;
import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.environment.structure.Structure;
import net.annova.environment.structure.StructureBase;
import net.annova.object.person.RPGPerson;
import net.annova.thread.*;
import net.minecraft.world.entity.Entity;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static net.annova.command.AnnovaCommandInit.initCommands;
import static net.annova.main.AnnovaAPI.initEvents;
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
    }

    @Override
    public void onDisable() {
        for (RPGEntity entity : getEntities()) {
            entity.entity.remove(Entity.RemovalReason.DISCARDED);
        }
        for (Dungeon dungeon : getDungeons()) {
            dungeon.endDungeon(true);
        }
        for (Structure structure : getStructures()) {
            StructureBase base = structure.getBase();
            if (base.isSave()) {
                base.save();
            }
        }
        for (RPGPerson person : getAllRPGPersons()) {
            person.save();
        }
    }

    private void initThreads() {
        syncTimer(this, new PersonUpdateTimer(), 100); // 10s
        // PersonStaminaTimer - SecondTickEvent
        syncTimer(this, new UpdateSpawnTimer(), 1200); // 60sec
        // PersonStatusTimer - SecondTickEvent
    }
    private void initialization() {
        // SQL
        RPGPerson.init();
        RPGEntity.init();
        StructureBase.init();

        // World
        GlobalWorld.init();
    }

    public static JavaPlugin getInstance() {
        return plugin;
    }
}
