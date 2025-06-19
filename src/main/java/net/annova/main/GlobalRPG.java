package net.annova.main;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.dungeon.DungeonTemplate;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.Person;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.npc.RPGNPC;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.quest.QuestBase;
import net.annova.environment.structure.Structure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.annova.main.Keys.NK_KEY;
import static net.annova.util.BukkitObjects.getWorld;

public class GlobalRPG {
    private static final Map<String, RPGPerson> persons = new HashMap<>();
    private static final Map<UUID, RPGPerson> personsUid = new HashMap<>();

    private static final Map<String, RPGNPC> npc = new HashMap<>();

    private static final Map<Entity, RPGEntity> entities = new HashMap<>();

    private static final World dungeonWorld = getWorld("dungeon_world");
    private static final Map<Integer, DungeonTemplate> dTemplates = new HashMap<>();
    private static final Map<Integer, Dungeon> dungeons = new HashMap<>();
    private static final Map<String, Structure> structures = new HashMap<>();
    private static final Map<String, QuestBase> quests = new HashMap<>();

    @Nullable
    public static RPGPerson getRPGPerson(Player player) {
        if (player == null) return null;
        RPGPerson person = personsUid.get(player.getUniqueId());
        if (person == null) return npc.get(player.getPersistentDataContainer().get(NK_KEY, PersistentDataType.STRING)).getPerson();
        return personsUid.get(player.getUniqueId());
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
    public static Collection<RPGPerson> getAllRPGPersons() {
        return persons.values();
    }

    public static void addRPGPerson(Person person) {
        RPGPerson rpgPerson = new RPGPerson(person);
        persons.put(person.getName().toLowerCase(), rpgPerson);
        personsUid.put(person.getUid(), rpgPerson);
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

    public static void addNPC(String key, RPGNPC npc) {
        GlobalRPG.npc.put(key, npc);
    }
    public static RPGNPC getNPC(Player player) {
        return getNPC(player.getPersistentDataContainer().get(NK_KEY, PersistentDataType.STRING));
    }
    public static RPGNPC getNPC(String key) {
        return npc.get(key);
    }

    @Nullable
    public static RPGEntity getEntity(Entity entity) {
        return entities.get(entity);
    }
    @Nullable
    public static RPGEntity getEntity(LivingEntity entity) {
        return entities.get(entity);
    }
    public static void addEntity(RPGEntity entity) {
        entities.put(entity.getEntity().getBukkitEntity(), entity);
    }
    public static void removeEntity(Entity entity) {
        entities.remove(entity);
    }
    public static boolean containEntity(Entity entity) {
        return entities.containsKey(entity);
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

    public static World getDungeonWorld() {
        return dungeonWorld;
    }

    public static DungeonTemplate getDTemplate(int id) {
        return dTemplates.get(id);
    }
    public static void addDTemplate(DungeonTemplate dungeonTemplate) {
        dTemplates.put(dungeonTemplate.getId(), dungeonTemplate);
    }

    public static Dungeon getDungeon(int key) {
        return dungeons.get(key);
    }
    public static void addDungeon(int key, Dungeon dungeon) {
        dungeons.put(key, dungeon);
    }
    public static void removeDungeon(int key) {
        dungeons.remove(key);
    }
    public static Collection<Dungeon> getDungeons() {
        return dungeons.values();
    }

    public static Structure getStructure(String key) {
        return structures.get(key);
    }
    public static void addStructure(Structure structure) {
        structures.put(structure.getConstruction().getKey(), structure);
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

    public static SoundEvent getSoundFromString(String string) {
        return SoundEvent.createVariableRangeEvent(ResourceLocation.parse(string));
    }
}
