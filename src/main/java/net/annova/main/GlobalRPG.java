package net.annova.main;

import net.annova.environment.civilization.Civilization;
import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.person.Person;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.npc.RPGNPC;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.quest.QuestBase;
import net.annova.environment.structure.Structure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.ChunkPos;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class GlobalRPG {
    private static final Map<String, RPGPerson> persons = new HashMap<>();
    private static final Map<UUID, RPGPerson> personsUid = new HashMap<>();

    private static final Map<UUID, RPGNPC> npc = new HashMap<>();

    private static final Map<UUID, RPGEntity> entities = new HashMap<>();

    private static final Map<Integer, DungeonTemplate> dTemplates = new HashMap<>();
    private static final Map<String, Structure> structures = new HashMap<>();
    private static final Map<String, QuestBase> quests = new HashMap<>();

    private static final Map<String, Civilization> civilizations = new HashMap<>();
    private static final Map<String, Town> towns = new HashMap<>();
    private static final Map<ServerLevel, Map<ChunkPos, AreaChunk>> areaChunks = new HashMap<>();

    @Nullable
    public static RPGPerson getRPGPerson(Entity entity) {
        if (entity instanceof Player) return getRPGPerson(entity.getUniqueId());
        return null;
    }
    @Nullable
    public static RPGPerson getRPGPerson(net.minecraft.world.entity.player.Player player) {
        RPGPerson person = personsUid.get(player.getUUID());
        return person == null
                ? npc.get(player.getUUID()).getPerson()
                : person;
    }
    @Nullable
    public static RPGPerson getRPGPerson(String name) {
        return persons.get(name.toLowerCase());
    }
    @Nullable
    public static RPGPerson getRPGPerson(UUID uid) {
        return personsUid.get(uid);
    }
    @Nullable
    public static RPGPerson getRPGPerson(Person person) {
        return personsUid.get(person.getUid());
    }
    public static Collection<RPGPerson> getRPGPersons() {
        return persons.values();
    }

    public static RPGPerson addRPGPerson(Person person) {
        RPGPerson rpg = new RPGPerson(person);
        persons.put(person.getName().toLowerCase(), rpg);
        personsUid.put(person.getUid(), rpg);
        return rpg;
    }
    public static void addRPGPerson(RPGPerson person) {
        persons.put(person.getPerson().getName().toLowerCase(), person);
        personsUid.put(person.getPerson().getUid(), person);
    }

    public static boolean containsRPGPerson(Entity player) {
        return personsUid.containsKey(player.getUniqueId());
    }
    public static boolean containsRPGPerson(String name) {
        return persons.containsKey(name.toLowerCase());
    }

    public static void addNPC(UUID key, RPGNPC npc) {
        GlobalRPG.npc.put(key, npc);
    }
    public static RPGNPC getNPC(net.minecraft.world.entity.player.Player player) {
        return getNPC(player.getUUID());
    }
    public static RPGNPC getNPC(UUID key) {
        return npc.get(key);
    }

    @Nullable
    public static RPGEntity getRPGEntity(Entity entity) {
        return entities.get(entity.getUniqueId());
    }
    @Nullable
    public static RPGEntity getRPGEntity(net.minecraft.world.entity.Entity entity) {
        return entities.get(entity.getUUID());
    }
    @Nullable
    public static RPGEntity getRPGEntity(UUID uuid) {
        return entities.get(uuid);
    }
    @Nullable
    public static RPGEntity getRPGEntity(LivingEntity entity) {
        return entities.get(entity.getUniqueId());
    }
    public static void addEntity(RPGEntity entity) {
        entities.put(entity.getEntity().getUUID(), entity);
    }
    public static void removeEntity(Entity entity) {
        entities.remove(entity.getUniqueId());
    }
    public static boolean containEntity(Entity entity) {
        return entities.containsKey(entity.getUniqueId());
    }
    public static int getAmountByTypeEntity(RPGEntityType type) {
        int amount = 0;
        for (RPGEntity entity : entities.values()) {
            if (type.equals(entity)) amount++;
        }
        return amount;
    }
    public static Collection<RPGEntity> getEntities() {
        return entities.values();
    }

    public static DungeonTemplate getDTemplate(int id) {
        return dTemplates.get(id);
    }
    public static void addDTemplate(DungeonTemplate dungeonTemplate) {
        dTemplates.put(dungeonTemplate.getId(), dungeonTemplate);
    }

    public static Structure getStructure(String key) {
        return structures.get(key);
    }
    public static void addStructure(Structure structure) {
        structures.put(structure.getConstruction().getStructure().getName(), structure);
    }
    public static void removeStructure(String key) {
        structures.remove(key);
    }
    public static Collection<Structure> getStructures() {
        return structures.values();
    }
    public static Map<String, Structure> getStructuresMap() {
        return structures;
    }

    public static QuestBase getQuest(String key) {
        return quests.get(key);
    }
    public static void addQuest(QuestBase quest) {
        quests.put(quest.getKey(), quest);
    }
    public static void removeQuest(String key) {
        quests.remove(key);
    }
    public static Collection<QuestBase> getQuests() {
        return quests.values();
    }
    public static Map<String, QuestBase> getQuestsMap() {
        return quests;
    }

    public static Collection<Civilization> getCivilizations() {
        return civilizations.values();
    }
    public static Civilization getCivilization(String name) {
        return civilizations.get(name.toLowerCase());
    }
    public static Civilization addCivilization(String civName, RPGPerson leader) {
        Civilization civilization = new Civilization(civName, leader);
        civilizations.put(civName.toLowerCase(), civilization);
        return civilization;
    }
    public static void addCivilization(Civilization civilization) {
        civilizations.put(civilization.getName().toLowerCase(), civilization);
    }

    public static Collection<Town> getTowns() {
        return towns.values();
    }
    public static Town getTown(String name) {
        return towns.get(name.toLowerCase());
    }
    public static void removeTown(Town town) {
        towns.remove(town.getName().toLowerCase());
        Map<ChunkPos, AreaChunk> chunks = areaChunks.get(town.getLevel());
        for (AreaChunk chunk : town.getChunks()) {
            chunks.remove(chunk.getPosition());
        }
    }
    public static void addTown(Town town) {
        towns.put(town.getName().toLowerCase(), town);
    }


    public static void addChunkCollection(ServerLevel level) {
        areaChunks.put(level, new HashMap<>());
    }
    public static Map<ChunkPos, AreaChunk> getAreaChunks(ServerLevel level) {
        return areaChunks.get(level);
    }
    public static AreaChunk getAreaChunk(RPGPerson person) {
        return getAreaChunk(person.getServerPlayer());
    }
    public static AreaChunk getAreaChunk(ServerPlayer player) {
        return getAreaChunk(player.serverLevel(), player.chunkPosition());
    }
    public static AreaChunk getAreaChunk(ServerLevel level, ChunkPos pos) {
        return areaChunks.get(level).get(pos);
    }
    public static void addAreaChunk(Town town, AreaChunk areaChunk) {
        for (ServerLevel level : areaChunks.keySet()) {
            System.out.println("есть мир: " + level.serverLevelData.getLevelName());
        }
        areaChunks.get(town.getLevel()).put(areaChunk.getPosition(), areaChunk);
    }

    public static SoundEvent getSoundFromString(String string) {
        return SoundEvent.createVariableRangeEvent(ResourceLocation.parse(string));
    }
}
