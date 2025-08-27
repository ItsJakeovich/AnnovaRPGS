package net.annova.main;

import net.annova.object.person.equipment.EquipmentMenu;
import net.annova.util.menu.MenuBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.NamespacedKey;

import static net.annova.main.AnnovaAPI.random;
import static net.annova.main.GlobalRPG.getSoundFromString;

public class Keys {
    public static final MenuBase EQUIPMENT_MENU = new EquipmentMenu();

    public static final SoundEvent FOREST_AMBIENT_1 = getSoundFromString("location.forest.ambient.ambient1");
    public static final SoundEvent FOREST_AMBIENT_2 = getSoundFromString("location.forest.ambient.ambient2");
    public static final SoundEvent FOREST_AMBIENT_3 = getSoundFromString("location.forest.ambient.ambient3");
    public static final SoundEvent POSTAMENT_DRIADASONG = getSoundFromString("location.postament.songdriada.songdriada");

    public static final ResourceLocation RESOURCE_ATTACK_DAMAGE = ResourceLocation.fromNamespaceAndPath("annovas", "attack_damage");
    public static final ResourceLocation RESOURCE_ATTACK_SPEED = ResourceLocation.fromNamespaceAndPath("annovas", "attack_speed");
    public static final ResourceLocation RESOURCE_ARMOR = ResourceLocation.fromNamespaceAndPath("annovas", "armor");
    public static final ResourceLocation RESOURCE_TOUGHNESS = ResourceLocation.fromNamespaceAndPath("annovas", "toughness");
    public static final ResourceLocation RESOURCE_MOVEMENT = ResourceLocation.fromNamespaceAndPath("annovas", "movement");

    public static final NamespacedKey NK_ID = getNamespacedKey("id");
    public static final NamespacedKey NK_KEY = getNamespacedKey("key");
    public static final NamespacedKey NK_BIOME = getNamespacedKey("biome");
    public static final NamespacedKey NK_JUMP_POWER = getNamespacedKey("jump_power");
    public static final NamespacedKey NK_MILLIS = getNamespacedKey("millis");
    public static final NamespacedKey NK_EXPIRE = getNamespacedKey("expire");
    public static final NamespacedKey NK_SOURCE_WATER = getNamespacedKey("source_water");
    public static final NamespacedKey NK_BLOB_FISH = getNamespacedKey("blob_fish");

    public static NamespacedKey getNamespacedKey(String string) {
        return new NamespacedKey("annovas", string);
    }

    public static ResourceLocation getRandomLocation(ResourceLocation location) {
        return ResourceLocation.fromNamespaceAndPath(location.getNamespace(), location.getPath() + random.nextInt(100000000));
    }
}
