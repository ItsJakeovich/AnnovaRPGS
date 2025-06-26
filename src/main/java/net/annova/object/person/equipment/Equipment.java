package net.annova.object.person.equipment;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.ValueType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_21_R4.CraftServer;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

import java.util.*;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.item.base.ItemType.getItemType;
import static net.annova.object.item.base.RPGItem.getItemId;
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

    public void addEquipment(int index, ItemStack item) {
        if (item == null) return;
        this.equipment[index] = item.clone();
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

            this.equipment[availableIndex] = item.clone();
            Player player = person.getPlayer();
            if (slot != -1) player.getInventory().setItem(slot, null);
            InventoryView view = player.getOpenInventory();
            if (view.getTitle().equals("Equipment")) {
                EquipType type = this.getEquipType(item);
                view.setItem(this.getSlotByIndex(type, availableIndex), item);
            }
            player.playSound(player, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);

            this.update();
        }
    }

    public void removeEquipment(ItemStack item, int slot) {
        EquipType type = this.getEquipType(item);
        int index = this.getIndexBySlot(type, slot);
        if (index == -1) return;
        this.equipment[index] = null;
        Player player = person.getPlayer();
        if (player.getOpenInventory().getTitle().equals("Equipment")) {
            player.getOpenInventory().setItem(slot, type.getIcon());
        }
        this.update();
        addItems(player, item);
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
                double value = values.get(type);
                finalValue.merge(type, values.get(type) * coefficient, Double::sum);
            }
            idList.add(id);
        }
        this.person.updateAttributes();
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
    private static RegistryAccess.Frozen getOpsTag() {
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

                joiner.add(CraftItemStack.asNMSCopy(item).save(getOpsTag()).toString());
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
                        CraftItemStack.asBukkitCopy(
                                net.minecraft.world.item.ItemStack.CODEC.parse(NbtOps.INSTANCE, TagParser.parseCompoundFully(serialItem))
                                .getOrThrow()
                        )
                );
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
