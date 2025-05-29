package net.annova.environment.dungeon;

import net.annova.entity.core.RPGEntity;
import net.annova.entity.core.RPGEntityType;
import net.annova.object.SimpleStructure;
import net.annova.object.animation.Animation;
import net.annova.object.generator.VoidGenerator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import java.util.*;

import static net.annova.util.BukkitObjects.chance;

public class DungeonTemplate {
    List<SimpleStructure> structures;
    private int id;
    private int spawn;
    private int merchantPoint;
    private Map<Integer, List<RPGEntity>> entities = new HashMap<>();
    // Map<Type, Map<id, animation>> == [type = "gate", id = 0]
    private Map<String, Map<Integer, Animation>> animations = new HashMap<>();
    // mobs
    private Map<Integer, Loot> loot = new HashMap<>(); // point, rarity

    public DungeonTemplate clone() {
        DungeonTemplate template = new DungeonTemplate();
        template.setStructures(structures);
        template.setId(id);
        template.setSpawn(spawn);
        template.setMerchantPoint(merchantPoint);

        template.setEntities(new HashMap<>());
        for (int key : entities.keySet()) {
            for (RPGEntity ent : entities.get(key)) {
                RPGEntity entity = RPGEntityType.getEntity(ent.getId());

                entity.setPoint(ent.getPoint());
                template.addEntity(key, entity);
            }
        }

        // Глубокое клонирование карты animations
        template.animations = new HashMap<>();
        for (Map<Integer, Animation> animations : this.animations.values()) {
            for (Animation animation : animations.values()) {
                template.addAnimation(animation.clone());
            }
        }

        template.setLoot(new HashMap<>());
        for (int key : this.loot.keySet()) {
            template.addLootChest(key, loot.get(key).rarity);
        }

        return template;
    }

    public SimpleStructure getStructure() {
        return this.getStructure(0);
    }
    public SimpleStructure getStructure(int index) {
        return structures.get(index);
    }

    public void setStructures(List<SimpleStructure> structures) {
        this.structures = structures;
    }
    public void setStructure(int index, SimpleStructure structure) {
        int size = structures.size();
        if (size < index) {
            for (int i = size; i < index; i++) {
                this.structures.set(i, null);
            }
        }
        this.structures.set(index, structure);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, Map<Integer, Animation>> getAnimations() {
        return animations;
    }
    public Animation getAnimation(String type, int id) {
        return animations.get(type).get(id);
    }
    public void addAnimation(Animation animation) {
        String type = animation.getName(); // also type
        int id = animation.getId();

        this.animations.putIfAbsent(type, new HashMap<>());
        this.animations.get(type).putIfAbsent(id, animation);
    }
    public void setAnimations(Map<String, Map<Integer, Animation>> animations) {
        this.animations = animations;
    }

    public Map<Integer, List<RPGEntity>> getEntities() {
        return entities;
    }
    public List<RPGEntity> getEntities(int id) {
        return entities.get(id);
    }
    public void addEntity(int id, RPGEntity entity) {
        this.entities.putIfAbsent(id, new ArrayList<>());
        this.entities.get(id).add(entity);
    }
    public void setEntities(Map<Integer, List<RPGEntity>> entities) {
        this.entities = entities;
    }


    public Map<Integer, Loot> getLootChests() {
        return loot;
    }
    public void addLootChest(int point, int rarity) {
        this.loot.put(point, new Loot(rarity));
    }
    public void setLoot(Map<Integer, Loot> loot) {
        this.loot = loot;
    }

    /**
     * @return a point, you can get location when
     */
    public int getSpawn() {
        return spawn;
    }
    public void setSpawn(int spawn) {
        this.spawn = spawn;
    }

    public int getMerchantPoint() {
        return merchantPoint;
    }
    public void setMerchantPoint(int merchantPoint) {
        this.merchantPoint = merchantPoint;
    }

    public void updateAnimations(SimpleStructure structure, Location createLocation) {
        for (Map<Integer, Animation> animations : this.getAnimations().values()) {
            for (Animation animation : animations.values()) {
                animation.update(createLocation, structure);
            }
        }
    }
    public void updateLootChests(SimpleStructure structure, Location location) {
        for (int point : this.loot.keySet()) {
            if (!chance(0.5)) continue;
            Loot loot = this.loot.get(point);

            World world = location.getWorld();
            Location spawn = location.clone().add(structure.getLocationWithPoint(world, point));

            Block block = spawn.getBlock();
            block.setBlockData(Material.CHEST.createBlockData());

            Chest chest = ((Chest) block.getState());
            chest.getInventory().setContents(loot.getLoot().getContents());
        }
    }

    public static void createDungeonWorld() {
        WorldCreator creator = new WorldCreator("dungeon_world");
        creator.generator(new VoidGenerator());

        creator.createWorld();
    }
}
