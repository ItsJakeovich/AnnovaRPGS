package net.annova.object.item.equipment.prison;

import net.annova.object.item.base.RPGArmor;
import net.annova.object.item.base.RPGArmorActivity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PrisonChestplateItem extends RPGItem implements RPGArmor, RPGArmorActivity {
    @Override
    public Material type() {
        return Material.CHAINMAIL_CHESTPLATE;
    }

    @Override
    public List<Float> modelData() {
        return List.of(3f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&fБыл снят с охранника во время бунта"
        );
    }

    @Override
    public void activity(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0));
    }
    @Override
    public long repeating() {
        return 400;
    }
}
