package net.annova.command.recipe;

import net.annova.config.crafts.ConfigCrafts;
import net.annova.main.AnnovaAPI;
import net.annova.main.AnnovaRPG;
import net.annova.object.person.Person;
import net.annova.util.menu.PagedMenu;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.config.RPGConfig.getAllCrafts;
import static net.annova.config.RPGConfig.getCraft;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.createItem;

public class RecipeInterface extends PagedMenu {
//    public static final ItemStack GLASS = getNewItem(Items.WHITE_STAINED_GLASS_PANE);
//    Думаю при показе предмет по краям стекло, но не уверен пока что
    private ConfigCrafts current = null;

    private static final int[] WORKBENCH_SLOTS = new int[]{
            11, 12, 13, 20, 21, 22, 29, 30, 31
    };
    @Override
    public MenuProvider inventory(Person person, Map<String, Object> data) {
        return new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return person.getAISString(getInstance(), "recipe-interface-name")
                        .replaceTag("{current}", current == null ? "" : current.getItemStack().getCustomName().getString())
                        .getDefined(person);
            }

            @Override
            public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
                current = getCraft((String) data.get("item_id"));
                SimpleContainer container;

                if (current == null) { // открывает меню с выбором предметов
                    container = new SimpleContainer(54);
                    page = (int) data.get("page");
                    ConfigCrafts[] crafts = getAllCrafts().toArray(new ConfigCrafts[0]);
                    int maxPage = (crafts.length+44) / 45;
                    if (page > maxPage) page = maxPage;
                    if (page < 1) page = 1;

                    int startIndex = 45 * (page-1);
                    for (int it = 0; it < 45 && it < crafts.length; it++) {
                        container.setItem(it, crafts[startIndex+it].getItemStack());
                    }
                    if (page > 1) { // Кнопка назад
                        container.setItem(45, createItem(Items.NAUTILUS_SHELL,
                                person.getAISString(getInstance(), "interface-previous").getDefined(person),
                                -1,
                                "previous"
                        ));
                    }
                    if (crafts.length > 45*page) {
                        container.setItem(53, createItem(Items.NAUTILUS_SHELL,
                                person.getAISString(AnnovaAPI.getInstance(), "interface-next").getDefined(person),
                                -1,
                                "next"
                        ));
                    }
                } else { // Открывает менюшку с крафтом предмета
                    container = new SimpleContainer(45);
                    container.setItem(36, createItem(Items.NAUTILUS_SHELL,
                            person.getAISString(getInstance(), "interface-previous").getDefined(person),
                            -1,
                            "return"
                    ));
                    container.setItem(24, current.getItemStack());
                    Recipe<?> holder = current.getRecipe().value();
                    int slot = 0;
                    ItemStack icon;
                    switch (holder) {
                        case ShapedRecipe recipe -> {
                            for (Optional<Ingredient> ing : recipe.getIngredients()) {
                                for (ItemStack stack : ing.get().itemStacks()) {
                                    container.setItem(WORKBENCH_SLOTS[slot], stack);
                                    slot++;
                                }
                            }
                            icon = createItem(Items.CRAFTING_TABLE,
                                    person.getAISString(getInstance(), "recipe-shaped").getDefined(person),
                                    -1,
                                    null
                            );
                        }
                        case ShapelessRecipe recipe -> {
                            for (Ingredient ing : recipe.placementInfo().ingredients()) {
                                for (ItemStack stack : ing.itemStacks()) {
                                    container.setItem(WORKBENCH_SLOTS[slot], stack);
                                    slot++;
                                }
                            }
                            icon = createItem(Items.CRAFTING_TABLE,
                                    person.getAISString(getInstance(), "recipe-shapeless").getDefined(person),
                                    -1,
                                    null
                            );
                            icon.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                        }
                        case SmeltingRecipe recipe -> {
                            for (ItemStack stack : recipe.input().itemStacks()) container.setItem(22, stack);
                            icon = createItem(Items.FURNACE, person.getAISString(getInstance(), "recipe-furnace").getDefined(person),
                                    -1,
                                    null
                            );
                        }
                        default -> {
                            icon = ItemStack.EMPTY;
                            sendInfo("{y}" + holder.getClass().getName() + " unknown recipe");
                        }
                    }
                    container.setItem(36, icon);
                }
                return ChestMenu.sixRows(i, inventory, container);
            }
        };
    }

    @Override
    public boolean takeable() {
        return false;
    }

    public ConfigCrafts getCurrent() {
        return current;
    }
}
