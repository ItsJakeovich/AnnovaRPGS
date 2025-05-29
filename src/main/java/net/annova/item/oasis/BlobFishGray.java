package net.annova.item.oasis;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

public class BlobFishGray extends RPGItem {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(4f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fБаланс между светом и тьмой&3. &fПризывает дождь не как разрушение&3, &fа как живительную влагу");
    }
}
