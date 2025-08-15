package net.annova.command.civ.cmdBuild;

import net.annova.environment.civilization.build.BuildBase;
import net.annova.main.AnnovaAPI;
import net.annova.object.person.Person;
import net.annova.object.SimpleStructure;
import net.annova.object.person.RPGPerson;
import net.annova.util.menu.MenuBase;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.createItem;
import static net.annova.util.BukkitObjects.getNewItem;

public class BuildInterface implements MenuBase {
    private int page;

    public int getPage() {
        return page;
    }

    @Override
    public MenuProvider inventory(Person person, Map<String, Object> data) {
        return new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return person.getAISString(getInstance(), "build-interface-name")
                        .replaceTag("{town}", getRPGPerson(person).getTown().getName())
                        .getDefined(person)
                        .append(String.valueOf(data.get("page")));
            }

            @Override
            public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
                SimpleContainer container = new SimpleContainer(54);
                RPGPerson rpg = getRPGPerson(person);

                BuildBase build = (BuildBase) data.get("build");
                if (build != null) {
                    container.setItem(45, createItem(Items.NAUTILUS_SHELL,
                            person.getAISString(AnnovaAPI.getInstance(), "interface-previous").getDefined(person),
                            -1,
                            "return"
                    ));
                    int it = 0;
                    for (SimpleStructure structure : build.getStyles()) {
                        container.setItem(it, createItem(Items.QUARTZ_BLOCK,
                                person.getAISString(getInstance(), "icon-" + structure.getName()).getDefined(person),
                                -1,
                                "preview:" + structure.getName()
                        ));
                        it++;
                    }
                } else {
                    int page = (int) data.get("page");
                    BuildBase[] builds = rpg.getTown().getAvailableBuilds().toArray(new BuildBase[0]);
                    int maxPage = (builds.length+44) / 45;
                    if (page > maxPage) page = maxPage;
                    if (page < 1) page = 1;
                    BuildInterface.this.page = page;

                    int startIndex = 45 * (page-1);
                    for (int it = startIndex; it < 45 && it < builds.length; it++) {
                        container.setItem(it, builds[startIndex + it].createItem(person));
                    }
                    if (page > 1) { // Кнопка назад
                        container.setItem(45, createItem(Items.NAUTILUS_SHELL,
                                person.getAISString(AnnovaAPI.getInstance(), "interface-previous").getDefined(person),
                                -1,
                                "previous"
                        ));
                    }
                    if (builds.length > 45 * page) { // Кнопка вперед
                        container.setItem(53, createItem(Items.NAUTILUS_SHELL,
                                person.getAISString(AnnovaAPI.getInstance(), "interface-next").getDefined(person),
                                -1,
                                "next"
                        ));
                    }

                    net.minecraft.world.item.ItemStack glassPane = getNewItem(Items.CYAN_STAINED_GLASS_PANE);
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
