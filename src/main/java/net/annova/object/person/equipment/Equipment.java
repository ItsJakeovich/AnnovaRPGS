package net.annova.object.person.equipment;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.annova.object.Person;
import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.ValueType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

import javax.annotation.Nullable;

import java.util.*;

import static net.annova.main.AnnovaAPI.getMinecraftServer;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.Keys.EQUIPMENT_MENU;
import static net.annova.object.item.base.ItemType.getItemType;
import static net.annova.object.item.base.RPGItem.getItemId;
import static org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy;

public class Equipment {
    private final RPGPerson person;
    private final ItemStack[] equipment = new ItemStack[24];
    private final Map<ValueType, Double> finalValue = new EnumMap<>(ValueType.class);

    public Equipment(RPGPerson person) {
        this.person = person;
        for (ValueType type : ValueType.values()) {
            finalValue.put(type, 0.0);
        }
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

    public void addEquipment(int index, ItemStack item) {
        if (item == null) return;
        this.equipment[index] = item.copy();
    }
    public void addEquipment(org.bukkit.inventory.ItemStack item, int slot) {
        this.addEquipment(asNMSCopy(item), slot);
    }
    public void addEquipment(ItemStack item, int slot) {
        if (item == null) return;
        ItemType itemType = getItemType(item);
        if (itemType != null && itemType.getItem() instanceof ItemEquipment equip) {
            int availableIndex = this.getAvailableIndex(equip.getEquipmentType().getIndices());
            if (availableIndex == -1) {
                person.getPerson().send(getInstance(), "equipment-full");
                return;
            }

            this.equipment[availableIndex] = item.copy();
            ServerPlayer player = person.getServerPlayer();
            if (slot != -1) player.getInventory().setItem(slot, null);
            AbstractContainerMenu menu = player.containerMenu;
            if (menu.getTitle().equals(EQUIPMENT_MENU.name(person.getPerson()))) {
                EquipType type = this.getEquipType(item);
                menu.getSlot(this.getSlotByIndex(type, availableIndex)).set(item);
            }
            player.playSound(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1, 1);

            this.update();
        }
    }

    public void removeEquipment(org.bukkit.inventory.ItemStack item, int slot) {
        this.removeEquipment(asNMSCopy(item), slot);
    }
    public void removeEquipment(ItemStack item, int slot) {
        EquipType type = this.getEquipType(item);
        int index = this.getIndexBySlot(type, slot);
        if (index == -1) return;
        ServerPlayer player = person.getServerPlayer();
        if (player.getInventory().add(item)) { // Если предмет может быть "снят"
            this.equipment[index] = null;
            AbstractContainerMenu menu = player.containerMenu;
            if (menu.getTitle().equals(EQUIPMENT_MENU.name(person.getPerson()))) {
                menu.getSlot(slot).set(type.getIcon());
            }
            this.update();
        }
    }

    private int getAvailableIndex(int[] slots) {
        for (int slot = slots[0]; slot <= slots[slots.length-1]; slot++) {
            if (equipment[slot] == null) return slot;
        }
        return -1;
    }

    public void update() {
        for (ValueType type : ValueType.values()) {
            finalValue.put(type, 0.0);
        }
        Set<String> idList = new HashSet<>();
        for (ItemStack item : this.equipment) {
            if (item == null) continue;
            ItemEquipment equip = (ItemEquipment) getItemType(item).getItem();
            Map<ValueType, Double> values = equip.values(person);
            String id = getItemId(item);
            double coefficient = idList.contains(id) ? 0.2 : 1.0;
            coefficient *= switch (equip.getEquipmentType()) {
                case RING -> this.person.getFinalValue(ValueType.RING_BUFF);
                default -> 1.0;
            };
            for (ValueType type : values.keySet()) {
                finalValue.merge(type, values.get(type) * coefficient, Double::sum);
            }
            idList.add(id);
        }
        ArmorSet set = this.person.updateArmorSet();
        if (set != null) {
            Map<ValueType, Double> values = set.getValues();
            for (ValueType type : values.keySet()) {
                finalValue.merge(type, values.get(type), Double::sum);
            }
        }
        this.person.updateAttributes();
    }

    public double getFinalValue(ValueType type) {
        return this.finalValue.get(type);
    }

    public void open() {
        this.open(person.getPerson());
    }
    /** (e - belt)
     * R R R - A - R R R   7
     * R R - - C - - R R   5
     * G B - - P - - B G   5
     * - - - - e - - - -   1
     * - - - - - - - - -   0
     * S K - - - - - K S   4
     */
    public void open(Person person) {
        person.openMenu(EQUIPMENT_MENU);
    }

    @Nullable
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

    public static CompoundTag saveAllItems(CompoundTag var0, NonNullList<net.minecraft.world.item.ItemStack> var1, boolean var2, HolderLookup.Provider var3) {
        ListTag var4 = new ListTag();

        for(int var5 = 0; var5 < var1.size(); ++var5) {
            net.minecraft.world.item.ItemStack var6 = (net.minecraft.world.item.ItemStack)var1.get(var5);
            if (!var6.isEmpty()) {
                CompoundTag var7 = new CompoundTag();
                var7.putByte("Slot", (byte)var5);
                var4.add(var6.save(var3, var7));
            }
        }

        if (!var4.isEmpty() || var2) {
            var0.put("Items", var4);
        }

        return var0;
    }

    private static RegistryAccess.Frozen opsTag = null;
    public static RegistryAccess.Frozen getOpsTag() {
        if (opsTag == null) {
            opsTag = ((CraftServer) Bukkit.getServer()).getServer().registries()
                    .compositeAccess();
        }
        return opsTag;
    }
    public String serialize() {
        StringJoiner joiner = new StringJoiner(";");
        for (ItemStack item : this.equipment) {
            if (item == null) joiner.add(" ");
            else {
//                CompoundTag tag = (CompoundTag) nms.save(getOpsTag());

                joiner.add(item.save(getOpsTag()).toString());
            }
        }
        return joiner.toString();
    }
    public void deserialize(String serialized) {
        if (serialized == null) return;
        String[] serialItems = serialized.split(";");

        for (int i = 0; i < serialItems.length; i++) {
            String serialItem = serialItems[i];
            if (serialItem.equals(" ")) continue;
            try {
                this.addEquipment(i, // index
                                net.minecraft.world.item.ItemStack.parse(getMinecraftServer().registryAccess(), TagParser.parseCompoundFully(serialItem)).get()
                );
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
