package net.annova.object.person.equipment;

import net.annova.object.Person;
import net.annova.util.MenuBase;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class EquipmentMenu implements MenuBase {
    @Override
    public MutableComponent name(Person person) {
        return person.getColorString(getInstance(), "equipment-menu");
    }

    @Override
    public MenuProvider inventory(Person person, Object o) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return EquipmentMenu.this.name(person);
            }

            @Override
            public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                SimpleContainer container = new SimpleContainer(54);
                Equipment equipment = getRPGPerson(person).getEquipment();
                int iconIndex = 0;
                for (EquipType equip : EquipType.values()) {
                    int[] slots = equip.getSlots();
                    int[] indices = equip.getIndices();
                    for (int it = 0; it < equip.getIndices().length; it++) {
                        ItemStack stack = equipment.getEquipment(indices[it]);
                        if (stack.isEmpty()) stack = equip.getIcon();
                        container.setItem(slots[it], stack);
                    }
                    iconIndex++;
                }
                return ChestMenu.sixRows(i, inventory, container);
            }
        };
    }

    @Override
    public boolean takeable() {
        return false;
    }
}
