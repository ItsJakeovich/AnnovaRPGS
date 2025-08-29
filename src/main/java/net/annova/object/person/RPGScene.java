package net.annova.object.person;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.bukkit.scheduler.BukkitRunnable;

import static net.annova.environment.world.RPGPortal.RPG_ENTER;
import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaRPG.getInstance;

public enum RPGScene {
    S0_FIRST {
        @Override
        public void start(RPGPerson rpg) {
            rpg.setScene(true);
            ServerPlayer player = rpg.getServerPlayer();
            player.teleport(S0_FIRST_TRANSITION);
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1));
            player.playNotifySound(SoundEvents.SNOW_FALL, SoundSource.BLOCKS, 3.0f, 1f);
            new BukkitRunnable() {
                private int it = 0;
                private final Person person = rpg.getPerson();
                @Override
                public void run() {
                    if (!rpg.isScene() || !person.isOnline()) {
                        rpg.removeScene(S0_FIRST);
                        rpg.setScene(false);
                        this.cancel();
                        return;
                    }
                    int monolog = switch (it) {
                        case 0 -> 0;
                        case 1 -> 1;
                        case 2 -> 2;
                        case 3 -> 3;
                        case 4 -> 4;
                        case 5 -> {
                            person.sendBar(getInstance(), "S0_FIRST_H-0", true);
                            yield -1;
                        }
                        case 6 -> 5;
                        case 7 -> 6;
                        case 8 -> 7;
                        case 9 -> 8;
                        case 10 -> 9;
                        case 11 -> 10;
                        case 12 -> 11;
                        case 13 -> {
                            person.sendBar(getInstance(), "S0_FIRST_H-1", true);
                            yield -1;
                        }
                        case 14 -> 12;
                        case 15 -> 13;
                        case 16 -> 14;
                        case 17 -> 15;
                        case 18 -> 16;
                        case 19 -> 17;
                        case 20 -> 18;
                        case 21 -> 19;
                        case 22 -> 20;
                        case 23 -> {
                            person.sendBar(getInstance(), "S0_FIRST_H-2", true);
                            yield -1;
                        }
                        case 24 -> 21;
                        case 25 -> 22;
                        case 26 -> 23;
                        case 27 -> 24;
                        case 28 -> 25;
                        case 29 -> 26;
                        case 30 -> 27;
                        case 31 -> 28;
                        case 32 -> {
                            person.sendBar(getInstance(), "S0_FIRST_H-3", true);
                            yield -1;
                        }
                        case 33 -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0f, 1f);
                            yield 29;
                        }
                        case 34 -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.5f, 1f);
                            yield 30;
                        }
                        case 35 -> 31;
                        case 36 -> 32;
                        case 37 -> 33;
                        case 38 -> 34;
                        case 39 -> 35;
                        case 40 -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 2.0f, 1f);
                            yield 36;
                        }
                        case 41 -> 37;
                        case 42 -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 3f, 1f);
                            yield 38;
                        }
                        default -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 4f, 1f);
                            player.removeEffect(MobEffects.INVISIBILITY);
                            player.teleport(RPG_ENTER.getTransition());
                            rpg.setScene(false);
                            this.cancel();
                            yield 39;
                        }
                    }; it++;
                    if (monolog != -1) person.sendForce(getInstance(), "S0_FIRST_M-" + monolog);
                }
            }.runTaskTimer(getInstance(), 40L, 140L);
        }
    },
    S0_FIRST_BASE {
        @Override
        public void start(RPGPerson rpg) {
            rpg.setScene(true);
            ServerPlayer player = rpg.getServerPlayer();
            player.teleport(S0_FIRST_TRANSITION);
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1, 0, false, false));
            player.playNotifySound(SoundEvents.SNOW_FALL, SoundSource.BLOCKS, 3.0f, 1f);
            new BukkitRunnable() {
                private int it = 0;
                private final Person person = rpg.getPerson();
                @Override
                public void run() {
                    if (!rpg.isScene() || !person.isOnline()) {
                        rpg.removeScene(S0_FIRST_BASE);
                        rpg.setScene(false);
                        this.cancel();
                        return;
                    }
                    int monolog = switch (it) {
                        case 0 -> 0;
                        case 1 -> 1;
                        case 2 -> 2;
                        case 3 -> 3;
                        case 4 -> 4;
                        case 5 -> {
                            person.sendBar(getInstance(), "S0_FIRST_BASE_H-0", true);
                            yield -1;
                        }
                        case 6 -> 5;
                        case 7 -> 6;
                        case 8 -> 7;
                        case 9 -> 8;
                        case 10 -> 9;
                        case 11 -> {
                            person.sendBar(getInstance(), "S0_FIRST_BASE_H-1", true);
                            yield -1;
                        }
                        case 12 -> 10;
                        case 13 -> 11;
                        case 14 -> 12;
                        case 15 -> 13;
                        case 16 -> 14;
                        case 17 -> {
                            person.sendBar(getInstance(), "S0_FIRST_BASE_H-2", true);
                            yield -1;
                        }
                        case 18 -> 15;
                        case 19 -> 16;
                        case 20 -> 17;
                        case 21 -> 18;
                        case 22 -> 19;
                        case 23 -> {
                            person.sendBar(getInstance(), "S0_FIRST_BASE_H-3", true);
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 2f, 1f);
                            yield -1;
                        }
                        case 24 -> 20;
                        case 25 -> 21;
                        case 26 -> 22;
                        case 27 -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 3f, 1f);
                            yield 23;
                        }
                        case 28 -> 24;
                        default -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 4f, 1f);
                            player.removeEffect(MobEffects.INVISIBILITY);
                            player.teleport(RPG_ENTER.getTransition());
                            rpg.setScene(false);
                            this.cancel();
                            yield 25;
                        }
                    }; it++;
                    if (monolog != -1) person.sendForce(getInstance(), "S0_FIRST_BASE_M-" + monolog);
                }
            }.runTaskTimer(getInstance(), 40L, 140L);
        }
    },
    S0_FIRST_CHERNU {
        @Override
        public void start(RPGPerson rpg) {
            rpg.setScene(true);
            ServerPlayer player = rpg.getServerPlayer();
            player.teleport(S0_FIRST_TRANSITION);
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, -1, 0, false, false));
            player.playNotifySound(SoundEvents.SNOW_FALL, SoundSource.BLOCKS, 3.0f, 1f);
            new BukkitRunnable() {
                private int it = 0;
                private final Person person = rpg.getPerson();
                @Override
                public void run() {
                    if (!rpg.isScene() || !person.isOnline()) {
                        rpg.removeScene(S0_FIRST_CHERNU);
                        rpg.setScene(false);
                        this.cancel();
                        return;
                    }
                    int monolog = switch (it) {
                        case 0 -> 0;
                        case 1 -> 1;
                        case 2 -> 2;
                        case 3 -> 3;
                        case 4 -> 4;
                        case 5 -> {
                            person.sendBar(getInstance(), "S0_FIRST_H-0", true);
                            yield -1;
                        }
                        case 6 -> 5;
                        case 7 -> 6;
                        case 8 -> 7;
                        case 9 -> 8;
                        case 10 -> 9;
                        case 11 -> 10;
                        case 12 -> 11;
                        case 13 -> {
                            person.sendBar(getInstance(), "S0_FIRST_H-1", true);
                            yield -1;
                        }
                        case 14 -> 12;
                        case 15 -> 13;
                        case 16 -> 14;
                        case 17 -> 15;
                        case 18 -> 16;
                        case 19 -> 17;
                        case 20 -> 18;
                        case 21 -> 19;
                        case 22 -> 20;
                        case 23 -> {
                            person.sendBar(getInstance(), "S0_FIRST_H-2", true);
                            yield -1;
                        }
                        case 24 -> 21;
                        case 25 -> 22;
                        case 26 -> 23;
                        case 27 -> 24;
                        case 28 -> 25;
                        case 29 -> 26;
                        case 30 -> 27;
                        case 31 -> 28;
                        case 32 -> {
                            person.sendBar(getInstance(), "S0_FIRST_H-3", true);
                            yield -1;
                        }
                        case 33 -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0f, 1f);
                            yield 29;
                        }
                        case 34 -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.5f, 1f);
                            yield 30;
                        }
                        case 35 -> 31;
                        case 36 -> 32;
                        case 37 -> 33;
                        case 38 -> 34;
                        case 39 -> 35;
                        case 40 -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 2.0f, 1f);
                            yield 36;
                        }
                        case 41 -> 37;
                        case 42 -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 3f, 1f);
                            yield 38;
                        }
                        case 43 -> {
                            player.playNotifySound(SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 4f, 1f);
                            player.removeEffect(MobEffects.INVISIBILITY);
                            player.teleport(S0_FIRST_CHERNU_TRANSITION);
                            yield 39;
                        }
                        case 44 -> {
                            person.sendBar(getInstance(), "S0_FIRST_CHERNU_H-0", true);
                            yield -1;
                        }
                        case 45 -> {
                            person.sendForce(getInstance(), "S0_FIRST_CHERNU_M-0");
                            yield -1;
                        }
                        case 46 -> {
                            person.sendForce(getInstance(), "S0_FIRST_CHERNU_M-1");
                            yield -1;
                        }
                        default -> {
                            rpg.setScene(false);
                            person.sendForce(getInstance(), "S0_FIRST_CHERNU_M-2");
                            player.removeEffect(MobEffects.BLINDNESS);
                            this.cancel();
                            yield -1;
                        }
                    }; it++;
                    if (monolog != -1) person.sendForce(getInstance(), "S0_FIRST_M-" + monolog);
                }
            }.runTaskTimer(getInstance(), 40L, 140L);
        }
    }
    ;
    private static final TeleportTransition S0_FIRST_TRANSITION = new TeleportTransition(world0, new Vec3(20.5, 300, -194.5), Vec3.ZERO, 0, 0, TeleportTransition.DO_NOTHING);
    private static final TeleportTransition S0_FIRST_CHERNU_TRANSITION = new TeleportTransition(world0, new Vec3(-61.5, 64, 433), Vec3.ZERO, -90, 0, TeleportTransition.DO_NOTHING);

    abstract void start(RPGPerson person);
}
