package net.annova.object.entity.mobs.sector0.mines;

import net.annova.environment.world.sector0.location.Mines;
import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.MinesMagicArmorNeutral;
import net.annova.object.person.RPGPerson;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Set;

import static net.annova.main.AnnovaAPI.random;
import static net.annova.main.GlobalRPG.getRPGEntity;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.MAGIC_SCRAP;

public class Carrier extends RPGEntity implements CustomType, Listener {
    @Override
    public Class<?> clazz() {
        return MinesMagicArmorNeutral.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String name() {
        return "Carrier";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public int health() {
        return 150;
    }

    @Override
    public double damage() {
        return 8;
    }

    @Override
    public float speed() {
        return 0.27f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(
                MAGIC_SCRAP.create(person, random.nextInt(3)+2, 0.059)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);
        if (!(person.getSectorLocation() instanceof Mines)) return;

        for (Entity check : player.getLocation().getNearbyEntities(16, 6, 16)) {
            RPGEntity rpg = getRPGEntity(check);
            if (rpg == null) continue;
            if (rpg.getRPGType() == RPGEntityType.CARRIER) {
                rpg.entity.setTarget(person.getPerson().getServerPlayer());
            }
        }
    }
}
