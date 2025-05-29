package net.annova.command.cmdRPG;

import net.annova.command.register.CommandArg;
import net.annova.item.*;
import net.annova.item.component.*;
import net.annova.item.container.PrisonContainerItem;
import net.annova.item.equipment.ControlMovementStickItem;
import net.annova.item.equipment.ControlStopStickItem;
import net.annova.item.equipment.prison.PrisonSwordItem;
import net.annova.item.base.RPGItem;
import net.annova.item.equipment.end.*;
import net.annova.item.equipment.forium.*;
import net.annova.item.equipment.nether.*;
import net.annova.item.equipment.prison.*;
import net.annova.item.token.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class RPGCode {
    @CommandArg(name = "items", aliases = {"предметы", "ghtlvtn", "шеуьы"})
    public void items(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54);

        RPGItem[] items = new RPGItem[]{
                new EndAxeItem(),
                new EndBootsItem(),
                new EndChestplateItem(),
                new EndDaggerItem(),
                new EndElytraItem(),
                new EndHelmetItem(),
                new EndLeggingsItem(),
                new EndPickaxeItem(),
                new EndShovelItem(),
                new EndSwordItem(),

                new LightningSwordItem(),
                new PrisonSwordItem(),

                new AwakenedNetheriteIngotItem(),
                new EndIngotItem(),
                new ControlMovementStickItem(),
                new ControlStopStickItem(),
                new DragonScaleItem(),
                new DungeonKeyItem(),
                new NetherCatalystItem(),
                new EndCatalystItem(),
                new PrisonTokenItem(),
                new NetherAxeItem(),
                new NetherBootsItem(),
                new NetherChestplateItem(),
                new NetherHelmetItem(),
                new NetherElytraItem(),
                new NetherLeggingsItem(),
                new NetherPickaxeItem(),
                new NetherShovelItem(),
                new NetherSwordItem(),

                new PrisonContainerItem(),
                new PrisonHelmetItem(),
                new PrisonChestplateItem(),
                new PrisonLeggingsItem(),
                new PrisonBootsItem(),
                new PrisonSteelSwordItem(),

                new CreeperTokenItem(),
                new DurabilityTokenItem(),
                new FlyTokenItem(),
                new SaturationTokenItem(),
                new TeleportTokenItem(),
                new RangeTokenItem(),

                new ForiumFragmentItem(),
                new ForiumIngotItem(),
                new ForiumSwordItem(),
                new ForiumPickaxeItem(),
                new ForiumShovelItem(),
                new ForiumHelmetItem(),
                new ForiumChestplateItem(),
                new ForiumLeggingsItem(),
                new ForiumBootsItem()
        };

        int i = 0;
        for (RPGItem item : items) {
            inv.setItem(i, item.create());
            i++;
        }

        player.openInventory(inv);
    }
}
