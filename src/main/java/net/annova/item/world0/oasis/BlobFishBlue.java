package net.annova.item.world0.oasis;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

public class BlobFishBlue extends RPGItem {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(5f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fМифическая рыба&3, &fпоявляющаяся только при рассеивании бурь&3. &fИспользуется в ритуалах очищения неба");
    }
}
