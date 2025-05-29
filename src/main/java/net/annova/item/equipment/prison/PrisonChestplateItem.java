package net.annova.item.equipment.prison;

import net.annova.item.base.RPGArmor;
import net.annova.item.base.RPGArmorActivity;
import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

public class PrisonChestplateItem extends RPGItem implements RPGArmor, RPGArmorActivity {
    @Override
    public Material type() {
        return Material.CHAINMAIL_CHESTPLATE;
    }

    @Override
    public String name() {
        return ChatColor.GRAY + "Нагрудник охранника";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{3};
    }

    @Override
    public boolean unstackable() {
        return false;
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

    @Override
    public Set<Perk> perks() {
        return Set.of();
    }
}
