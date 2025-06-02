package net.annova.object.item.equipment.prison;

import net.annova.object.item.base.RPGSet;
import org.bukkit.event.Listener;

public class PrisonSet extends RPGSet implements Listener {
    @Override
    public int health() {
        return 40;
    }

    @Override
    public float damage() {
        return 8;
    }

    @Override
    public float speed() {
        return -0.4f;
    }

    @Override
    public float regeneration() {
        return 0;
    }
}
