package net.annova.item.oasis;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

public class WhaleFish extends RPGItem {
    @Override
    public Material type() {
        return Material.SALMON;
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
    public List<String> lore() {
        return List.of("&3= &fНе настоящий кит&3, &fно имя своё она заслужила по праву&3. &fОбладательница огромной пасти и эластичного мешка&3, &fэта рыба охотится без усилий &3— &fпросто раскрывает рот и втягивает всё&3, &Fчто плывёт мимо");
    }
}
