package net.annova.object.person;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.bukkit.scheduler.BukkitRunnable;

import static net.annova.main.AnnovaRPG.getInstance;

public enum RPGScene {
    S0_FIRST {
        @Override
        public void start(RPGPerson rpg) {
            rpg.setScene(true);
            ServerPlayer player = rpg.getServerPlayer();
            player.addEffect(EFFECT_INVISIBILITY);
            new BukkitRunnable() {
                private int it = 0;
                private final Person person = rpg.getPerson();
                @Override
                public void run() {
                    switch (it) {
                        case 0 -> person.send("1");
                        case 1 -> person.send("2");
                        case 2 -> person.send("3");
                        default -> {
                            player.removeEffect(MobEffects.INVISIBILITY);
                            rpg.setScene(false);
                            person.send("4");
                            this.cancel();
                        }
                    } it++;
                }
                
            }.runTaskTimer(getInstance(), 40L, 20L);
        }
    };
    private static final MobEffectInstance EFFECT_INVISIBILITY = new MobEffectInstance(MobEffects.INVISIBILITY, 100000, 0, false, false);

    abstract void start(RPGPerson person);
}
