package net.annova.command.cmdRPG;

import net.annova.command.register.CommandArg;
import net.annova.object.entity.mobs.custom.Crab;
import net.annova.object.item.base.Item;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class RPGCode {
    private static final List<Inventory> itemsInterface = new ArrayList<>();
    static {
        int i = 0;
        Inventory inventory = null;
        for (Item item : Item.values()) {
            if (i % 54 == 0) {
                inventory = Bukkit.createInventory(null, 54, STR."Items - page:\{1 + (i/54)}");
                itemsInterface.add(inventory);
            }
            inventory.addItem(item.create(1));
            i++;
        }
    }
    @CommandArg(name = "items")
    public void items(Player player, String[] args) {
        String wrong = STR."Pages: \{Math.ceil((double)(Item.values().length)/54)} (/rpg items {page})";
        if (args.length == 1) {
            player.sendMessage(wrong);
            return;
        }
        int page = Integer.parseInt(args[1]);
        if (page > itemsInterface.size()) {
            player.sendMessage(wrong);
            return;
        }
        player.openInventory(itemsInterface.get(1+page));
    }
    @CommandArg(name = "testt")
    public void testt(Player player) {
        player.sendMessage("cave_spider должен был бы быть");
        ServerLevel level = ((CraftWorld) player.getWorld()).getHandle();
        Location location = player.getLocation();
        CaveSpider spider = new CaveSpider(EntityType.CAVE_SPIDER, level);
        spider.setPos(location.getX(), location.getY(), location.getZ());
        level.tryAddFreshEntityWithPassengers(spider);

        Crab crab = new Crab(EntityType.CAVE_SPIDER, level);
        crab.setPos(location.getX(), location.getY(), location.getZ());
        level.tryAddFreshEntityWithPassengers(crab);
    }
//    @CommandArg(name = "items", aliases = {"предметы", "ghtlvtn", "шеуьы"})
//    public void items(Player player) {
//        Inventory inv = Bukkit.createInventory(null, 54);
//
//        RPGItem[] items = new RPGItem[]{
//                new EndAxeItem(),
//                new EndBootsItem(),
//                new EndChestplateItem(),
//                new EndDaggerItem(),
//                new EndElytraItem(),
//                new EndHelmetItem(),
//                new EndLeggingsItem(),
//                new EndPickaxeItem(),
//                new EndShovelItem(),
//                new EndSwordItem(),
//
//                new LightningSwordItem(),
//                new PrisonSwordItem(),
//
//                new AwakenedNetheriteIngotItem(),
//                new EndIngotItem(),
//                new ControlMovementStickItem(),
//                new ControlStopStickItem(),
//                new DragonScaleItem(),
//                new DungeonKeyItem(),
//                new NetherCatalystItem(),
//                new EndCatalystItem(),
//                new PrisonTokenItem(),
//                new NetherAxeItem(),
//                new NetherBootsItem(),
//                new NetherChestplateItem(),
//                new NetherHelmetItem(),
//                new NetherElytraItem(),
//                new NetherLeggingsItem(),
//                new NetherPickaxeItem(),
//                new NetherShovelItem(),
//                new NetherSwordItem(),
//
//                new PrisonContainerItem(),
//                new PrisonHelmetItem(),
//                new PrisonChestplateItem(),
//                new PrisonLeggingsItem(),
//                new PrisonBootsItem(),
//                new PrisonSteelSwordItem(),
//
//                new CreeperTokenItem(),
//                new DurabilityTokenItem(),
//                new FlyTokenItem(),
//                new SaturationTokenItem(),
//                new TeleportTokenItem(),
//                new RangeTokenItem(),
//
//                new ForiumFragmentItem(),
//                new ForiumIngotItem(),
//                new ForiumSwordItem(),
//                new ForiumPickaxeItem(),
//                new ForiumShovelItem(),
//                new ForiumHelmetItem(),
//                new ForiumChestplateItem(),
//                new ForiumLeggingsItem(),
//                new ForiumBootsItem()
//        };
//
//        int i = 0;
//        for (RPGItem item : items) {
//            inv.setItem(i, item.create());
//            i++;
//        }
//
//        player.openInventory(inv);
//    }
}
