package net.annova.object.entity.mobs;

import net.annova.object.CustomRunnable;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.person.Person;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.person.Person.getCardinalDirection;
import static net.annova.util.BukkitObjects.random;

public class Boar extends RPGEntity implements EntityActivity {
    @Override
    public EntityType<?> type() {
        return EntityType.PIG;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public int health() {
        return 24;
    }

    @Override
    public double damage() {
        return 6.5;
    }

    @Override
    public float speed() {
        return 0.25f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(
                createItem(Items.PORKCHOP, Component.literal("Кабанятина"), 1, random.nextInt(5), 1.0)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

//    @EventHandler
//    public void spawn(CreatureSpawnEvent event) {
//        if (!(event.getEntity() instanceof CraftPig craftPig)) return;
//        if (craftPig.getHandle().getCustomName() != null) return;
//        if (!chance(0.25)) return;
//
//        event.setCancelled(true);
//        Location loc = event.getLocation();
//        for (int i = 0; i < 3; i++) {
//            new Boar().create(loc);
//        }
//    }

    private final float followRange = 30;
    private boolean canAttack = true;
    private double speed = 1.75; // Начальная скорость
    private Person.CardinalDirection direction;
    private ServerPlayer targetPlayer = null; // Текущая цель

    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        if (!entity.valid || !entity.isAlive()) return false;
                        Mob mob = Boar.this.entity;
                        if (targetPlayer == null || !targetPlayer.valid) {
                            for (Entity entity : mob.level().getEntities(entity, entity.getBoundingBox().inflate(16, 8, 16))) {
                                if (entity instanceof ServerPlayer player && player.gameMode().equals(GameType.SURVIVAL)) {
                                    targetPlayer = player;
                                    break;
                                }
                            }
                        }
                        if (targetPlayer == null) return true;
                        double minSpeed = 1.75;
                        double distance = mob.distanceToSqr(targetPlayer);
                        if (distance > followRange * followRange) {
                            targetPlayer = null;
                            speed = minSpeed;
                            return true;
                        }
                        mob.getNavigation().moveTo(targetPlayer, speed);

                        Player player = targetPlayer.getBukkitEntity();
                        org.bukkit.entity.Mob pig = (org.bukkit.entity.Mob) mob.getBukkitEntity();
                        if (pig.isOnGround() && player.getLocation().getY() - pig.getLocation().getY() > 1) {
                            pig.setVelocity(player.getLocation()
                                    .toVector()
                                    .subtract(mob.getBukkitEntity().getLocation().toVector())
                                    .normalize().multiply(1.2).setY(0.6)
                            );
                        }

                        if (direction != getCardinalDirection(mob.getBukkitEntity())) {
                            Boar.this.speed = Math.max(speed - 0.35, minSpeed);
                        }
                        direction = getCardinalDirection(mob.getBukkitEntity());

                        // Если кабан близко, бей!
                        if (distance > 2.0 || !canAttack) return true;

                        targetPlayer.hurtServer(mob.level().getMinecraftWorld(), mob.damageSources().mobAttack(mob), (float) Boar.this.damage());

                        // Отталкивание игрока
                        Vec3 pigPosition = mob.position();
                        Vec3 playerPosition = targetPlayer.position();
                        Vec3 knockback = new Vec3(
                                playerPosition.x - pigPosition.x,
                                playerPosition.y - pigPosition.y,
                                playerPosition.z - pigPosition.z
                        ).normalize().scale(-2.0);

                        knockback = new Vec3(knockback.x, 0.5, knockback.z);
                        targetPlayer.setDeltaMovement(knockback);

                        // Сброс агрессии после удара
                        canAttack = false;
                        speed = minSpeed;
                        targetPlayer = null;

                        // Таймер восстановления атаки
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                canAttack = true;
                            }
                        }.runTaskLater(getInstance(), 40L); // 2 seconds
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 20;
                    }
                },
                new CustomRunnable() {
                    @Override
                    public boolean activity() {
                        if (targetPlayer == null) return true;
                        if (!canAttack || Boar.this.entity.distanceToSqr(targetPlayer) > followRange*followRange) return true;

                        // Увеличиваем скорость до максимума
                        speed = Math.min(speed + 0.05, 2.5);
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 2;
                    }
                }
        );
    }
}
