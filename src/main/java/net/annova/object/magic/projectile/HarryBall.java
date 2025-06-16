package net.annova.object.magic.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.annova.environment.world.SectorLocation.getChorusLocation;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.HARRY_DEFENCE_DEBUFF;
import static net.annova.util.BukkitObjects.random;

public class HarryBall extends ParticleProjectile {
    public final LivingEntity summoner;

    public HarryBall(LivingEntity summoner) {
        this.summoner = summoner;
    }

    public LivingEntity getSummoner() {
        return summoner;
    }

    @Override
    public void onHit(Location location) {
        for (Entity ent : location.getWorld().getNearbyEntities(location, 2, 2, 2, entity -> entity instanceof LivingEntity)) {
            LivingEntity entity = (LivingEntity) ent;
            switch (effect.getRandom()) {
                case TELEPORT -> entity.teleport(getChorusLocation(location, 16));
                case DAMAGE -> {
                    entity.damage(10);
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 0));
                }
                case COMBUST -> {
                    entity.setFireTicks(300);
                    double maxHealth = summoner.getAttribute(Attribute.MAX_HEALTH).getValue();
                    summoner.setHealth(Math.min(summoner.getHealth()+20, maxHealth));
                }
                case FREEZE -> {
                    entity.setFreezeTicks(500);
                    if (entity instanceof Player player) {
                        getRPGPerson(player).getStatusManager().addStatus(HARRY_DEFENCE_DEBUFF);
                    }
                }
            }
        }
    }
    private enum effect {
        TELEPORT, // телепортирует на рандом место
        DAMAGE, // накладывает слепоту и замедление + 10 урона
        COMBUST, // поджигает + восстанавливает себе 20 здоровья
        FREEZE // замораживает и снижает защиту на 50%
        ;
        private static effect getRandom() {
            effect[] effects = effect.values();
            return effects[random.nextInt(effects.length)];
        }
    }

    @Override
    public double speed() {
        return 20;
    }

    @Override
    public Packet<?>[] particles(Location location) {
        return new Packet[] {
            new ClientboundLevelParticlesPacket(ParticleTypes.FLAME, true, true, location.getX(), location.getY(), location.getZ(), 0.25f, 0.25f, 0.25f, 0, 2),
            new ClientboundLevelParticlesPacket(ParticleTypes.ENCHANTED_HIT, true, false, location.getX(), location.getY(), location.getZ(), 0.1f, 0.1f, 0.1f, 0, 4),
            new ClientboundLevelParticlesPacket(ParticleTypes.CHERRY_LEAVES, true, true, location.getX(), location.getY(), location.getZ(), 0.1f, 0.1f, 0.1f, 0, 8)
        };
    }
}
