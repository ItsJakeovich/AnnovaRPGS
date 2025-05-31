package net.annova.item.world0.oasis;

import net.annova.object.person.status.Status;
import net.annova.item.base.RPGItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;

import static net.annova.item.base.Item.ELK_FISH;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.ELK_FISH_DAMAGE_BUFF;

public class ElkFish extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(1f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fКрупная и горделивая&3, &fэта рыба будто сочетает в себе дух северных зверей&3. &fЕё мясо насыщает даже самых прожорливых");
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!ELK_FISH.equals(event.getItem())) return;

        getRPGPerson(event.getPlayer()).getStatusManager().addStatus(ELK_FISH_DAMAGE_BUFF);
    }
}
