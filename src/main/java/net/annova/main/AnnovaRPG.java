package net.annova.main;

import net.annova.config.AnnovaConfig;
import net.annova.config.RPGConfig;
import net.annova.environment.civilization.Civilization;
import net.annova.environment.civilization.Town;
import net.annova.environment.world.GlobalWorld;
import net.annova.object.entity.core.RPGEntity;
import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.environment.structure.Structure;
import net.annova.environment.structure.StructureBase;
import net.annova.object.person.RPGPerson;
import net.annova.thread.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static net.annova.command.AnnovaCommandInit.initCommands;
import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.environment.dungeon.GlobalDungeon.getDungeons;
import static net.annova.event.summon.EntityEvent.indicators;
import static net.annova.main.AnnovaAPI.getSQL;
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
        GlobalWorld.initSectors();

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

        loadAllStructures();
        DungeonTemplate.createDungeonWorld();

        this.initialization();

        for (Player player : Bukkit.getOnlinePlayers()) {
            RPGPerson person = getRPGPerson(player);
            if (person == null) {
                person = addRPGPerson(getPerson(player));
            }
            person.getPerson().getScoreboard().initScoreBoard();
        }
        loadInfo.add("{pc}| {mc}Игроки {sc}: {ac}" + getRPGPersons().size());
        loadInfo.add("{pc}| {mc}Мобики {sc}: {ac}" + getEntities().size());
        loadInfo.add("{pc}| {mc}Города {sc}: {ac}" + getTowns().size());
        loadInfo.add("{pc}| {mc}Цивилизации {sc}: {ac}" + getCivilizations().size());

        loadInfo.forEach(AnnovaConfig::sendInfo);
        loadInfo = null;

        this.initThreads();

        sendInfo("{pc}❖ {mc}Плагин {ac}AnnovaRPG {mc}запущен{sc}!");
    }

    @Override
    public void onDisable() {
        for (ArmorStand indicator : indicators) {
            indicator.remove(Entity.RemovalReason.DISCARDED);
        }
        for (Dungeon dungeon : getDungeons()) {
            dungeon.endDungeon(true);
        }
        AnnovaRPG.saveAll();
        for (RPGEntity entity : getEntities()) {
            entity.entity.remove(Entity.RemovalReason.DISCARDED, EntityRemoveEvent.Cause.DISCARD);
        }
    }

    public static void saveAll() {
        for (Structure structure : getStructures()) {
            StructureBase base = structure.getBase();
            if (base.isSave()) base.save();
        }
        for (RPGPerson person : getRPGPersons())
            person.save();

        getSQL().truncateTable(RPGEntity.TABLE);
        for (RPGEntity entity : getEntities()) {
            entity.save();
        }

        for (Town town : getTowns()) town.save();
        for (Civilization civilization : getCivilizations()) civilization.save();
    }

    private void initThreads() {
        // CivilizationTickTimer - TenMinutesTickEvent;
        syncTimer(this, new FiveSecondsTimer(), 100); // 5s
        syncTimer(this, new WorldTickTimer(), 10); // 0.5s
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

        Town.init();
        Civilization.init();
    }

    public static AnnovaRPG getInstance() {
        return (AnnovaRPG) plugin;
    }

    private final String name = this.getName().toLowerCase();
    public String getNamespaceName() {
        return name;
    }
}
