package net.annova.object.item.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.equipment.EquipType;
import net.annova.object.person.status.ValueType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static net.annova.environment.world.GlobalWorld.isNight;

public class CreatingNightAmulet extends RPGItem implements ItemEquipment {
    @Override
    public Item type() {
        return Items.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(414f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    @Override
    public EquipType getEquipmentType() {
        return EquipType.AMULET;
    }

    @Override
    public Map<ValueType, Double> values(RPGPerson person) {
        Map<ValueType, Double> values = new EnumMap<>(ValueType.class);
        Player player = person.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 300, 0));
        if (isNight(player.getWorld())) { // night
            values.put(ValueType.DAMAGE, 6.0);
            values.put(ValueType.SPEED, 0.2);
        } else { // day
            values.put(ValueType.DAMAGE, 4.0);
            values.put(ValueType.SPEED, 0.15);
            for (Entity entity : player.getNearbyEntities(7, 5, 7)) {
                if (entity instanceof LivingEntity living) living.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 300, 0));
            }
        }
        return values;
    }
}
