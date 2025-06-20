package net.annova.object.person.equipment;

import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.ValueType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

import java.util.EnumMap;
import java.util.Map;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.item.base.ItemType.getItemType;
import static net.annova.util.BukkitObjects.addItems;

public class Equipment {
    private final RPGPerson person;
    private final ItemStack[] equipment = new ItemStack[24];
    private final Map<ValueType, Double> finalValue = new EnumMap<>(ValueType.class) {{
        for (ValueType type : ValueType.values()) {
            this.put(type, 0.0);
        }
    }};

    public Equipment(RPGPerson person) {
        this.person = person;
    }

    /**
     * @param index ItemStack[] array
     * @return ItemStack / null if slot is empty
     */
    @Nullable
    public ItemStack getEquipment(int index) {
        return equipment[index];
    }
    public ItemStack[] getAllEquipment() {
        return equipment;
    }

    public void addEquipment(Player player, ItemStack item, int slot) {
        if (item == null) return;
        ItemType itemType = getItemType(item);
        if (itemType != null && itemType.getItem() instanceof ItemEquipment equip) {
            int availableIndex = this.getAvailableIndex(equip.getEquipmentType().getIndices());
            if (availableIndex == -1) {
                person.getPerson().send(getInstance(), "equipment-full");
                return;
            }

            this.equipment[availableIndex] = item.clone();
            player.getInventory().setItem(slot, null);
            InventoryView view = player.getOpenInventory();
            if (view.getTitle().equals("Equipment")) {
                EquipType type = this.getEquipType(item);
                view.setItem(this.getSlotByIndex(type, availableIndex), item);
            }
            player.playSound(player, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);

            this.update(player);
        }
    }

    public void removeEquipment(Player player, ItemStack item, int slot) {
        EquipType type = this.getEquipType(item);
        int index = this.getIndexBySlot(type, slot);
        if (index == -1) return;
        this.equipment[index] = null;
        if (player.getOpenInventory().getTitle().equals("Equipment")) {
            player.getOpenInventory().setItem(slot, type.getIcon());
        }
        this.update(player);
        addItems(player, item);
    }

    private int getAvailableIndex(int[] slots) {
        for (int slot = slots[0]; slot <= slots[slots.length-1]; slot++) {
            if (equipment[slot] == null) return slot;
        }
        return -1;
    }

    private void update(Player player) {
        for (ValueType type : ValueType.values()) {
            finalValue.put(type, 0.0);
        }
        for (ItemStack item : this.equipment) {
            if (item == null) continue;
            ItemEquipment equip = (ItemEquipment) getItemType(item).getItem();
            Map<ValueType, Double> values = equip.values(player);
            for (ValueType type : values.keySet()) {
                finalValue.merge(type, values.get(type), Double::sum);
            }
        }
    }

    public double getFinalValue(ValueType type) {
        return this.finalValue.get(type);
    }

    /** (e - belt)
     * R R R - A - R R R   7
     * R R - - C - - R R   5
     * G B - - P - - B G   5
     * - - - - e - - - -   1
     * - - - - - - - - -   0
     * S K - - - - - K S   4
     */
    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "Equipment");

        int iconIndex = 0;
        for (EquipType equip : EquipType.values()) {
            int[] slots = equip.getSlots();
            int[] indices = equip.getIndices();
            for (int i = 0; i < equip.getIndices().length; i++) {
                ItemStack stack = equipment[indices[i]];
                if (stack == null) stack = equip.getIcon();
                inv.setItem(slots[i], stack);
            }
            iconIndex++;
        }

        player.closeInventory();
        player.openInventory(inv);
    }

    public EquipType getEquipType(ItemStack itemStack) {
        EquipType type = null;
        if (getItemType(itemStack).getItem() instanceof ItemEquipment equip) {
            type = equip.getEquipmentType();
        }
        return type;
    }

    public int getIndexBySlot(EquipType type, int slot) {
        if (type == null) return -1;
        int[] slots = type.getSlots();
        int[] indices = type.getIndices();
        for (int i = 0; i < indices.length; i++) {
            if (slots[i] == slot) return indices[i];
        }
        return -1;
    }
    public int getSlotByIndex(EquipType type, int index) {
        if (type == null) return -1;
        int[] slots = type.getSlots();
        int[] indices = type.getIndices();
        for (int i = 0; i < slots.length; i++) {
            if (indices[i] == index) return slots[i];
        }
        return -1;
    }
}
