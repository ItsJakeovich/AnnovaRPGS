package net.annova.object.item.base;

import net.annova.object.person.status.ValueType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;

import static net.annova.object.person.status.ValueType.FALL_MULTIPLIER;
import static net.annova.util.RPGUtil.makeValueTypeMap;

public enum ArmorSet {
    PRISON(makeValueTypeMap(
        ValueType.HEALTH, 15.0,
        ValueType.DAMAGE, 2.0,
        ValueType.SPEED, -0.4
    ), ItemType.PRISON_BOOTS, ItemType.PRISON_LEGGINGS, ItemType.PRISON_CHESTPLATE, ItemType.PRISON_HELMET),
    CHITIN(makeValueTypeMap(
        ValueType.HEALTH, 8.0,
            FALL_MULTIPLIER, -0.1
    ), ItemType.CHITIN_BOOTS, ItemType.CHITIN_LEGGINGS, ItemType.CHITIN_CHESTPLATE, ItemType.CHITIN_HELMET),
    ;

    private final EnumMap<ValueType, Double> values;
    private final ItemType[] armor;

    ArmorSet(EnumMap<ValueType, Double> values, ItemType... armor) {
        this.values = values;
        this.armor = armor;
    }

    public EnumMap<ValueType, Double> getValues() {
        return values;
    }
    public ItemType[] getArmor() {
        return armor;
    }

    public static ArmorSet getArmorSet(Player player) {
        ItemStack[] contents = player.getEquipment().getArmorContents();
        loop: for (ArmorSet armorSet : ArmorSet.values()) {
            for (int i = 0; i < 4; i++) {
                ItemStack content = contents[i];
                if (content == null) return null;
                if (armorSet.armor[i].equals(content)) continue;
                continue loop;
            }
            return armorSet;
        }
        return null;
    }
}
