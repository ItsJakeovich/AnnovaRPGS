package net.annova.command.civ.cmdBuild;

import net.annova.environment.civilization.build.BuildBase;
import net.annova.object.Person;
import net.annova.object.SimpleStructure;
import net.annova.object.person.RPGPerson;
import net.annova.util.MenuBase;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    public MutableComponent name(Person person) {
        return person.getAISString(getInstance(), "build-interface-name")
                .replaceTag("{town}", getRPGPerson(person).getTown().getName())
                .getDefined(person);
    }

    @Override
    public MenuProvider inventory(Person person, Object o) {
        return new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return name(person).append(String.valueOf(o));
            }

            @Override
            public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
                SimpleContainer container = new SimpleContainer(54);
                RPGPerson rpg = getRPGPerson(person);

                if (o instanceof BuildBase base) {
                    container.setItem(45, createItem(Items.NAUTILUS_SHELL,
                            person.getAISString(getInstance(), "build-interface-previous").getDefined(person),
                            -1,
                            "build-style-previous"
                    ));
                    int it = 0;
                    for (SimpleStructure structure : base.getStyles()) {
                        container.setItem(it, createItem(Items.QUARTZ_BLOCK,
                                person.getAISString(getInstance(), "icon-" + structure.getName()).getDefined(person),
                                -1,
                                "preview:" + structure.getName()
                        ));
                        it++;
                    }
                } else {
                    int page = (int) o;
                    if (page < 1) page = 1;
                    List<BuildBase> builds = List.copyOf(rpg.getTown().getAvailableBuilds());
                    while (page * 45 < builds.size()) page--;
                    BuildInterface.this.page = page;

                    int startIndex = 45 * (page - 1);
                    for (int it = 0; it < 45 && it < builds.size(); it++) {
                        container.setItem(it, builds.get(startIndex + it).createItem(person));
                    }
                    if (page > 1) { // Кнопка назад
                        container.setItem(45, createItem(Items.NAUTILUS_SHELL,
                                person.getAISString(getInstance(), "build-interface-previous").getDefined(person),
                                -1,
                                "build-previous"
                        ));
                    }
                    if (builds.size() > 45 * page) { // Кнопка вперед
                        container.setItem(53, createItem(Items.NAUTILUS_SHELL,
                                person.getAISString(getInstance(), "build-interface-next").getDefined(person),
                                -1,
                                "build-next"
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
