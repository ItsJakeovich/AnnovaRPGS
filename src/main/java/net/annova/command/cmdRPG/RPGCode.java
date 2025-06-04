package net.annova.command.cmdRPG;

import net.annova.command.register.CommandArg;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.entity.mobs.custom.Crab;
import net.annova.object.item.base.Item;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.entity.Player;

import static net.annova.util.BukkitObjects.addItems;

public class RPGCode {
    @CommandArg(name = "test")
    public void test(Player player, String[] args) {
        if (args[0].equals("m")) {
            addItems(player, Item.valueOf(args[1]).create(player));
        }
        if (args[0].equals("mob")) {
            RPGEntityType.valueOf(args[1]).getEntity().create(player.getLocation());
        }
    }
    @CommandArg(name = "testt")
    public void testt(Player player) {
        ServerLevel level = ((CraftWorld) player.getWorld()).getHandle();
        Crab crab = new Crab(EntityType.CAVE_SPIDER, level);
        Location location = player.getLocation();
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
