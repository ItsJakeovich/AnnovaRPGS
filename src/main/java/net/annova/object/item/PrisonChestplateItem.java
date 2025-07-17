package net.annova.object.item;

import net.annova.object.item.base.attribute.RPGArmor;
import net.annova.object.item.base.attribute.RPGArmorActivity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PrisonChestplateItem extends RPGItem implements RPGArmor, RPGArmorActivity {
    @Override
    public Item type() {
        return Items.CHAINMAIL_CHESTPLATE;
    }

    @Override
    public List<Float> modelData() {
        return List.of(3f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public void activity(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0));
    }
    @Override
    public long repeating() {
        return 400;
    }

    @Override
    public float armor() {
        return 6;
    }

    @Override
    public float toughness() {
        return 0;
    }

    @Override
    public float speed() {
        return 0;
    }
}
