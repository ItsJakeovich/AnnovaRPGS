package net.annova.environment.dungeon;

import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.SimpleStructure;
import net.annova.object.animation.Animation;
import net.annova.object.generator.VoidGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.bukkit.*;

import java.util.*;

import static net.annova.util.BukkitObjects.chance;

public class DungeonTemplate {
    List<SimpleStructure> structures = new ArrayList<>();
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
                RPGEntity entity = RPGEntityType.getType(ent).getEntity();

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
            template.addLootChest(key, loot.get(key).getRarity());
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
        for (int i = structures.size(); i <= index; i++) {
            this.structures.add(null);
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

    public void updateAnimations(SimpleStructure structure, Vec3i createLocation, ServerLevel level) {
        System.out.println(this.getAnimations().size());
        for (Map<Integer, Animation> animations : this.getAnimations().values()) {
            for (Animation animation : animations.values()) {
                animation.update(level, createLocation, structure);
            }
        }
    }
    public void updateLootChests(SimpleStructure structure, Vec3i position, ServerLevel level) {
        for (int point : this.loot.keySet()) {
            if (!chance(0.5)) continue;
            Loot loot = this.loot.get(point);

            BlockPos pos = new BlockPos(position.offset(structure.getPositionWithPoint(point))); // (spawn)

            level.setBlock(pos, Blocks.CHEST.defaultBlockState(), 3);

            ChestBlockEntity block = (ChestBlockEntity) level.getBlockEntity(pos);
            List<ItemStack> items = new ArrayList<>(block.getContents());
            items.clear();
            items.addAll(loot.getLoot().getContents());
//            block.setItems().
            block.setChanged();
        }
    }

    public static void createDungeonWorld() {
        WorldCreator creator = new WorldCreator("dungeon_world");
        creator.generator(new VoidGenerator());

        creator.createWorld();
    }
}
