package net.annova.environment.world.sector0.location.event;

import net.annova.environment.world.sector0.location.Oasis;
import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.annova.environment.world.GlobalWorld.isNight;
import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.environment.world.sector0.location.Oasis.bubble;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.*;
import static net.annova.object.person.advancement.RPGAdvancement.*;
import static net.annova.util.BukkitObjects.*;
import static net.annova.util.BukkitObjects.chance;

public class OasisEvent implements Listener {
    // bottleclickbubble теперь в самом CustomBubble;

    public static final Map<ItemType, Double> OASIS_FISHS = new HashMap<>();
    static {
        OASIS_FISHS.put(ANGEL_FISH, 0.007);
        OASIS_FISHS.put(ANGLER_FISH_SPAWN_EGG, 0.005);
        OASIS_FISHS.put(AZURE_FISH, 0.01);
        OASIS_FISHS.put(BARK_BEETLE_FISH, 0.04);
        OASIS_FISHS.put(BLOB_FISH_BLUE, 0.01);
        OASIS_FISHS.put(BLOB_FISH_GRAY, 0.01);
        OASIS_FISHS.put(BLOB_FISH_YELLOW, 0.01);
        OASIS_FISHS.put(CRAB_CLAW, 0.01);
        OASIS_FISHS.put(ELK_FISH, 0.025);
        OASIS_FISHS.put(FIRE_FISH, 0.037);
        OASIS_FISHS.put(FURY_CAVIAR, 0.012);
        OASIS_FISHS.put(KOI_FISH, 0.02);
        OASIS_FISHS.put(NALIM_FISH, 0.0184);
        OASIS_FISHS.put(OASIS_FISH, 0.005);
        OASIS_FISHS.put(PEARL, 0.008);
        OASIS_FISHS.put(ROYAL_PUFFERFISH, 0.004);
        OASIS_FISHS.put(SEASMITH, 0.04);
        OASIS_FISHS.put(SILVER_PUMP, 0.01);
        OASIS_FISHS.put(VOLCANIC_STREAMS_MALMA, 0.03);
        OASIS_FISHS.put(WHALE_FISH, 0.0121);

        OASIS_FISHS.put(TIDE_BLADE, 0.008);
    }
    public static final List<ItemType> FISHS = List.copyOf(OASIS_FISHS.keySet());
    @EventHandler
    public void fishing(PlayerFishEvent event) {
        RPGPerson person = getRPGPerson(event.getPlayer());
        if (person.getSectorLocation() instanceof Oasis) {
            if (chance(0.7)) return; // 70% на обычную рыбу

            ItemType finalFish;
            while (true) {
                ItemType fish = FISHS.get(random.nextInt(FISHS.size()));
                if (chance(OASIS_FISHS.get(fish))) {
                    finalFish = fish;
                    break;
                }
            }
            Item item = (Item) event.getCaught();
            item.setItemStack(CraftItemStack.asBukkitCopy(finalFish.create(person, 1)));

            person.completeAdvancement(GOOD_BITE);
            if (finalFish.equals(OASIS_FISH)) person.completeAdvancement(THATS_LUCK);
            if (isNight(world0)) {
                person.completeAdvancement(MOON_FISHERMAN);
            }
            if (finalFish.getItem() instanceof ItemEquipment) {
                person.catchedTreasures++;
                person.completeAdvancement(WOW_BEAUTIFUL);
                if (person.catchedTreasures >= 10) person.completeAdvancement(GOOD_DAY);
                if (person.catchedTreasures >= 100) person.completeAdvancement(GOLD_MANIA);
            }
        }
    }
}
