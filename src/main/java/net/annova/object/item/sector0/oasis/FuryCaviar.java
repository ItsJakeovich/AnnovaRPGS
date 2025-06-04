package net.annova.object.item.sector0.oasis;

import net.annova.object.RPGRarity;
import net.annova.object.person.RPGPerson;
import net.annova.object.item.base.RPGItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;

import static net.annova.object.item.base.Item.FURY_CAVIAR;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.CAVIAR_DAMAGE_BUFF;

public class FuryCaviar extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(13f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fСодержит в себе мощь тысяч боевых инстинктов&3. &fКажется что после каждого употребления её в пищу&3, &fусиливает вас");
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!FURY_CAVIAR.equals(event.getItem())) return;

        RPGPerson person = getRPGPerson(event.getPlayer());
        person.getStatusManager().addStatus(CAVIAR_DAMAGE_BUFF);
    }
}
