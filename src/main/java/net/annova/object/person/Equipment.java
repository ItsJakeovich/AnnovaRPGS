package net.annova.object.person;

import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.attribute.ItemEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

import static net.annova.util.BukkitObjects.getNewIcon;

public class Equipment {
    public enum Type {
        RING(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
        SCARF(10),
        HAIR_CLIP(11),
        BELT(12),
        AMULET(13),
        GLOVE(14, 15),
        BRACELET(16, 17),

        SHOULDER_PADS(18, 19),
        KNEE_PADS(20, 21),
        CLOAK(22),
        COMPANION(23);

        private final int[] slots;

        Type(int... slots) {
            this.slots = slots;
        }

        public int[] getSlots() {
            return slots;
        }
    }

    private final RPGPerson person;
    private final ItemStack[] equipment = new ItemStack[24];

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

    public void addEquipment(Player player, ItemStack item) {
        ItemType itemType = ItemType.getItemType(item);
        if (itemType.getItem() instanceof ItemEquipment equip) {
            int[] slots = equip.getEquipmentType().getSlots();
            int maxSlot = slots[slots.length-1];
            for (int slot = slots[0]; slot <= maxSlot; slot++) {
                if (equipment[slot] != null) continue;
                this.equipment[slot] = item;
                player.getEquipment().setItemInMainHand(null);
                player.playSound(player, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);

                equip.effect(player);
            }
        }
    }

    @EventHandler
    public void inventory(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(" ")) {
            event.setCancelled(true);
            ItemStack current = event.getCurrentItem();
            if (current == null) return;
            if (current.getItemMeta().getDisplayName().equals(" ")) return;

        }
    }
    /**
     * 0 - ring
     * 1 - scarf
     * 2 - hair clip
     * 3 - belt
     * 4 - amulet
     * 5 - glove
     * 6 - bracelet
     * 7 - shoulder pads
     * 8 - knee pads
     * 9 - cloak
     * 10 - companion
     */
    private static final ItemStack[] icons = new ItemStack[] {
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
            getNewIcon(0f),
    };
    /** (e - belt)
     * R R R - A - R R R   7
     * R R - - C - - R R   5
     * G B - - P - - B G   5
     * - - - - e - - - -   1
     * - - - - - - - - -   0
     * S K - - - - - K S   4
     * @return Inventory with equipment slot's
     */
    public Inventory open() {
        Inventory inv = Bukkit.createInventory(null, 54, " ");

        inv.setItem(0, this.getItemSlot(0, 0)); // rings
        inv.setItem(1, this.getItemSlot(1, 0));
        inv.setItem(2, this.getItemSlot(2, 0));
        inv.setItem(6, this.getItemSlot(3, 0));
        inv.setItem(7, this.getItemSlot(4, 0));
        inv.setItem(8, this.getItemSlot(5, 0));
        inv.setItem(9, this.getItemSlot(6, 0));
        inv.setItem(10, this.getItemSlot(7, 0));
        inv.setItem(16, this.getItemSlot(8, 0));
        inv.setItem(17, this.getItemSlot(9, 0));

        inv.setItem(3, this.getItemSlot(10, 1));

        inv.setItem(5, this.getItemSlot(11, 2));

        inv.setItem(31, this.getItemSlot(12, 3)); // belt

        inv.setItem(4, this.getItemSlot(13, 4)); // amulet

        inv.setItem(18, this.getItemSlot(14, 5)); // gloves
        inv.setItem(26, this.getItemSlot(15, 5));

        inv.setItem(19, this.getItemSlot(16, 6)); // bracelets
        inv.setItem(25, this.getItemSlot(17, 6));

        inv.setItem(45, this.getItemSlot(18, 7)); // shoulder pads
        inv.setItem(53, this.getItemSlot(19, 7));

        inv.setItem(46, this.getItemSlot(20, 8)); // knee pads
        inv.setItem(52, this.getItemSlot(21, 8));

        inv.setItem(13, this.getItemSlot(22, 9)); // cloak
        inv.setItem(22, this.getItemSlot(23, 10)); // companion
        return inv;
    }
    private ItemStack getItemSlot(int slot, int iconIndex) {
        ItemStack item = equipment[slot];
        return item == null ? icons[iconIndex] : item;
    }
}
