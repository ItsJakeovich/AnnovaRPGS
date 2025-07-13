package net.annova.command.cmdRPG;

import net.annova.command.register.CommandArg;
import net.annova.environment.world.WorldSector;
import net.annova.environment.world.sector0.location.Oasis;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.item.base.ItemType;
import net.annova.object.magic.projectile.*;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

import static net.annova.environment.world.GlobalWorld.worldSectors;
import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.addItems;

public class RPGCode {
    @CommandArg(name = "ball")
    public void test(Player player, String[] args) {
        Location spawn = player.getEyeLocation().add(0, -0.5, 0);
        spawn.add(spawn.getDirection().multiply(2.5));
        switch (Integer.parseInt(args[1])) {
            case 0 -> new FireBall().create(spawn);
            case 1 -> new HarryBall(player).create(spawn);
            case 2 -> new IceBall().create(spawn);
            case 3 -> new MelanoxRay().create(spawn);
            case 4 -> new MeteoriteBall().create(spawn);
            case 5 -> new WebBall().create(spawn);
        }
    }

    @CommandArg(name = "heal")
    public void heal(Player player, String[] args) {
        RPGPerson person = getRPGPerson(player);
        RPGPerson target = args.length == 1 ? person : getRPGPerson(args[1]);
        if (target == null) {
            person.getPerson().send(getInstance(), "player-not-found", "{name}", args[1]);
            return;
        }

        target.setHealth(target.getMaxHealth());
        person.getPerson().send(getInstance(), "rpg-heal-success", "{target}", target.getPerson().getName());
    }

    private static final List<Inventory> itemsInterface = new ArrayList<>();
    static {
        int i = 0;
        Inventory inventory = null;
        for (ItemType itemType : ItemType.values()) {
            if (i % 54 == 0) {
                inventory = Bukkit.createInventory(null, 54, "Items - page: " + 1 + i/54);
                itemsInterface.add(inventory);
            }
            inventory.addItem(CraftItemStack.asBukkitCopy(itemType.create(1)));
            i++;
        }
    }
    @CommandArg(name = "items")
    public void items(Player player, String[] args) {
        String wrong = "Pages: " + Math.ceil((double)(ItemType.values().length)/54) + " (/rpg items {page})";
        if (args.length == 1) {
            player.sendMessage(wrong);
            return;
        }
        int page = Integer.parseInt(args[1]);
        if (page > itemsInterface.size()) {
            player.sendMessage(wrong);
            return;
        }
        Inventory template = itemsInterface.get(page-1);
        Inventory toOpen = Bukkit.createInventory(null, 54, "Items - page: " +page);
        toOpen.setContents(template.getContents());
        player.openInventory(toOpen);
    }

    @CommandArg(name = "shaking")
    public void shaking(Player player) {
        RPGPerson person = getRPGPerson(player);
        StatusManager manager = person.getStatusManager();
        if (manager.hasStatus(StatusType.SHAKING)) {
            manager.removeStatus(StatusType.SHAKING);
        } else manager.addStatus(StatusType.SHAKING);
    }

    @CommandArg(name = "border")
    public void border(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage("Usage: /rpg border {1-6}");
            return;
        }
        WorldSector sector = worldSectors.get(player.getWorld());
        if (sector == null) return;
        int location = Integer.parseInt(args[1]);
        sector.zones()[location].showTerritory(world0);
    }

    @CommandArg(name = "oasis-bubble")
    public void oasisBubble(Player player) {
        player.sendMessage(Oasis.spawnOasisBubble().toString());
    }

    @CommandArg(name = "summon")
    public void summon(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage("Usage: /rpg summon {entity-name}");
            return;
        }
        RPGEntityType type = RPGEntityType.valueOf(args[1]);
        type.getEntity().create(player.getLocation());
    }

    @CommandArg(name = "item", aliases = {"предмет", "шеуь", "ghtlvtn"})
    public void item(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage("Usage: /rpg item {item-name}");
            return;
        }
        ItemType type = ItemType.valueOf(args[1]);
        addItems(player, CraftItemStack.asBukkitCopy(type.create(1)));
    }

    @CommandArg(name = "status")
    public void status(Player player, String[] args) {
        ServerPlayer server = ((CraftPlayer) player).getHandle();

//        server.sendSystemMessage(main);

        if (args.length == 1) {
            player.sendMessage("Usage: /rpg status {status-name}");
            return;
        }
        StatusType type = StatusType.valueOf(args[1]);
        getRPGPerson(player).getStatusManager().addStatus(type);
    }
}
