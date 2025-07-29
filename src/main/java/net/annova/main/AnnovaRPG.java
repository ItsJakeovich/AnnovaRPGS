package net.annova.main;

import net.annova.config.AnnovaConfig;
import net.annova.config.RPGConfig;
import net.annova.object.entity.core.RPGEntity;
import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.environment.structure.Structure;
import net.annova.environment.structure.StructureBase;
import net.annova.object.person.RPGPerson;
import net.annova.thread.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.annova.command.AnnovaCommandInit.initCommands;
import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.event.summon.EntityEvent.indicators;
import static net.annova.main.AnnovaAPI.getMinecraftServer;
import static net.annova.main.AnnovaAPI.initEvents;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaGlobal.loadAllStructures;
import static net.annova.main.GlobalRPG.*;
import static net.annova.thread.TaskMaster.syncTimer;

public final class AnnovaRPG extends JavaPlugin {
    private static JavaPlugin plugin;
    public static List<String> loadInfo = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;
        loadInfo.add("{pc}❖ {mc}Запуск{sc}...");

        try {
            new RPGConfig().init();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        try {
            loadInfo.add("{pc}| {mc}События{sc}: {ac}" + initEvents(getInstance(), "net.annova"));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        try {
            loadInfo.add("{pc}| {mc}Команды{sc}: {ac}" + initCommands("RPGCommand", "net.annova.command"));
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
        loadInfo.add("{pc}| {mc}Игроки {sc}: {ac}" + getRPGPersons().size());

        loadInfo.forEach(AnnovaConfig::sendInfo);
        loadInfo = null;

        sendInfo("{pc}❖ {mc}Плагин {ac}AnnovaRPG {mc}запущен{sc}!");
    }

    @Override
    public void onDisable() {
        for (RPGEntity entity : getEntities()) {
            entity.entity.remove(Entity.RemovalReason.DISCARDED);
        }
        for (ArmorStand indicator : indicators) {
            indicator.remove(Entity.RemovalReason.DISCARDED);
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
        // UpdateSpawnTimer - MinuteTickEvent;
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
