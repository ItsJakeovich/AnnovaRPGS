package net.annova.entity.traits.status;

import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import static net.annova.main.AnnovaRPG.getInstance;

public abstract class RPGStatus {
    public abstract boolean activity(RPGPerson person, Player player, int max);
    private BukkitTask runnable;
    private boolean active = false;

    public void setRunnable(BukkitTask runnable) {
        this.runnable = runnable;
    }
    public BukkitTask getRunnable() {
        return runnable;
    }

    public boolean isActive() {
        return active;
    }

    public void start(Skill skill, RPGPerson person, Status.Type type, RPGItem item, int max, long l) {
        this.active = true;
        this.runnable = new BukkitRunnable() {
            private final Player player = person.getPerson().getPlayer();
            @Override
            public void run() {
                Status status = person.getStatus(type);
                if (status == null || !player.isOnline() || !item.equalItem(player.getInventory().getItemInMainHand())) {
                    person.removeStatus(type);
                    return;
                }

                if (this.activity(person, player, max)) {
                    if (skill != null) {
                        skill.skill();
                        person.removeStatus(type);
                    }
                }
            }
        }.runTaskTimer(getInstance(), 0L, l);
    }

    public void stop(RPGPerson person, Status.Type type) {
        if (runnable != null) {
            this.runnable.cancel();
            this.runnable = null;
        }
        this.active = false;
    }
}
