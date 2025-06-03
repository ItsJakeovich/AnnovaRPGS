package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.attribute.ItemConfiguration;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;

import java.util.List;

import static net.annova.main.Keys.NK_MILLIS;

public class UnknownSourceWater extends RPGItem implements ItemConfiguration {
    @Override
    public Material type() {
        return Material.POTION;
    }

    @Override
    public List<Float> modelData() {
        return List.of();
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
        return List.of();
    }

    @Override
    public void initConfiguration(ItemMeta itemMeta) {
        ((PotionMeta) itemMeta).setBasePotionType(PotionType.WATER);
        itemMeta.getPersistentDataContainer().set(NK_MILLIS, PersistentDataType.LONG, System.currentTimeMillis());
    }
}
