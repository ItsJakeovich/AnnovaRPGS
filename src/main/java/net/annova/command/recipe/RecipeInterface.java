package net.annova.command.recipe;

import net.annova.config.crafts.ConfigCrafts;
import net.annova.main.AnnovaAPI;
import net.annova.object.person.Person;
import net.annova.util.menu.PagedMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static net.annova.config.RPGConfig.getAllCrafts;
import static net.annova.config.RPGConfig.getCraft;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.createItem;
import static net.annova.util.BukkitObjects.getNewItem;

public class RecipeInterface extends PagedMenu {
//    public static final ItemStack GLASS = getNewItem(Items.WHITE_STAINED_GLASS_PANE);
//    Думаю при показе предмет по краям стекло, но не уверен пока что
    private ConfigCrafts current = null;

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
            public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
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
                    if (current.getRecipe().value() instanceof )
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
