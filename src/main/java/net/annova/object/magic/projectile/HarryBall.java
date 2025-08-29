package net.annova.object.magic.projectile;

import com.google.common.base.Predicates;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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
    public void onHit(Vec3 position, ServerLevel level) {
        for (Entity target : level.getEntities((Entity) null, new AABB(position, position).inflate(2.25), Predicates.instanceOf(LivingEntity.class))) {
            LivingEntity entity = (LivingEntity) target;
            switch (effect.getRandom()) {
                case TELEPORT -> {
                    Vec3 newPos = getChorusLocation(position, level, 16);
                    entity.teleportTo(newPos.x, newPos.y, newPos.z);
                }
                case DAMAGE -> {
                    entity.hurtServer(level, level.damageSources().generic(), 10);
                    entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0)); entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 200, 0));
                }
                case COMBUST -> {
                    entity.igniteForSeconds(15);
                    float maxHealth = (float) summoner.getAttribute(Attributes.MAX_HEALTH).getValue();
                    summoner.setHealth(Math.min(summoner.getHealth()+20, maxHealth));
                }
                case FREEZE -> {
                    entity.igniteForSeconds(25);
                    if (entity instanceof Player player) {
                        getRPGPerson(player).getStatusManager().addStatus(HARRY_DEFENCE_DEBUFF);
                    }
                }
            }
        }
    }
    private enum effect {
        TELEPORT, // телепортирует на случайное место
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
    public Packet<?>[] particles(Vec3 position) {
        return new Packet[] {
            new ClientboundLevelParticlesPacket(ParticleTypes.FLAME, true, true, position.x, position.y, position.z, 0.25f, 0.25f, 0.25f, 0, 2),
            new ClientboundLevelParticlesPacket(ParticleTypes.ENCHANTED_HIT, true, false, position.x, position.y, position.z, 0.1f, 0.1f, 0.1f, 0, 4),
            new ClientboundLevelParticlesPacket(ParticleTypes.CHERRY_LEAVES, true, true, position.x, position.y, position.z, 0.1f, 0.1f, 0.1f, 0, 8)
        };
    }

    @Override
    public boolean isGravity() {
        return true;
    }
}
