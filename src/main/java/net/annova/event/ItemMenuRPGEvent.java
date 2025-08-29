package net.annova.event;

import net.annova.event.custom.ItemMenuCreateCE;
import net.annova.event.custom.MenuClickItemCE;
import net.annova.event.custom.PersonConfirmCE;
import net.annova.main.AnnovaAPI;
import net.annova.object.SimpleObject;
import net.annova.object.item.base.ItemModulit;
import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.Confirm;
import net.annova.object.person.Person;
import net.annova.object.person.RPGPerson;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.getItemType;
import static net.annova.object.item.base.ItemType.updateItemLocal;
import static net.annova.util.BukkitObjects.*;
import static net.minecraft.core.component.DataComponents.CUSTOM_DATA;

public class ItemMenuRPGEvent implements Listener {
    @EventHandler
    public void create(ItemMenuCreateCE event) {
        Person person = event.getPerson();
        event.getContainer().setItem(12, createItem(Items.BOOK, person.getAISString(getInstance(), "item-menu-modulit").getDefined(person),
                -1, "add-modulit"));
    }

    @EventHandler
    public void clickKey(MenuClickItemCE event) {
        String key = event.getKey();
        if (key.equals("add-modulit")) {
            Person person = event.getPerson();

            ItemStack item = person.getServerPlayer().getMainHandItem();
            if (item.isEmpty()) {
                person.send(AnnovaAPI.getInstance(), "item-menu-modulut-item");
                person.closeMenu(true);
                return;
            }
            Confirm confirm = person.getConfirm();
            confirm.setConfirmType("item-add-modulit");
            confirm.addData(new SimpleObject("item", item));
            person.closeMenu(true);

            person.send(getInstance(), "item-menu-confirm");
        }
    }

    @EventHandler
    public void confirm(PersonConfirmCE event) {
        Person person = event.getPerson();

        Confirm confirm = person.getConfirm();
        switch (confirm.getConfirmType()) {
            case "item-add-modulit" -> {
                RPGPerson pers = getRPGPerson(person.getUid());
                if (pers.getTeam() != null) {
                    person.send(getInstance(), "team-invite-request-already", "{team}", pers.getTeam().getName());
                    break;
                }
                ServerPlayer player = person.getServerPlayer();
                ItemStack inHand = player.getMainHandItem();
                ItemType type = getItemType(inHand);
                ItemModulit modulit;
                switch (type) {
                    case SEASMITH -> modulit = ItemModulit.SEASMITH_PICKAXE;
                    default -> {
                        person.send(getInstance(), "item-confirm-modulit-item");
                        return;
                    }
                }
                ItemStack item = (ItemStack) confirm.getData().get("item");
                if (!item.has(CUSTOM_DATA)) item.set(CUSTOM_DATA, CustomData.of(new CompoundTag()));
                CompoundTag tag = item.get(CUSTOM_DATA).getUnsafe();
                if (modulit.hasModulit(tag)) {
                    person.send(getInstance(), "item-confirm-modulit-exist");
                    person.resetConfirm();
                    return;
                }
                RPGItem.subtractItem(player, inHand);
                modulit.addModulit(tag);
                updateItemLocal(person, getItemType(item), item);
            }
            default -> {
                return;
            }
        }
        person.resetConfirm();
    }
}
