package net.annova.object.entity.mobs.sector0.mines;

import net.annova.object.CustomRunnable;
import net.annova.object.Person;
import net.annova.object.RPGRarity;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.CustomType;
import net.annova.object.entity.core.attribute.EntityActivity;
import net.annova.object.entity.core.attribute.EntitySpawn;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.mobs.custom.MinesGolemBoss;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;

import static net.annova.environment.dungeon.Loot.createItem;
import static net.annova.main.AnnovaAPI.random;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.MAGIC_SCRAP;
import static net.annova.object.item.base.ItemType.PURPLE_CRYSTAL_FRAGMENT;
import static net.annova.util.BukkitObjects.sendPackets;

public class GolemBoss extends RPGEntity implements CustomType, EntitySpawn, EntityActivity {
    @Override
    public Class<?> clazz() {
        return MinesGolemBoss.class;
    }

    @Override
    public EntityType<?> type() {
        return EntityType.IRON_GOLEM;
    }

    @Override
    public String name() {
        return "GolemBoss";
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.ELITE;
    }

    @Override
    public int health() {
        return 400;
    }

    @Override
    public double damage() {
        return 12;
    }

    @Override
    public float speed() {
        return 0.24f;
    }

    @Override
    public boolean small() {
        return false;
    }

    @Override
    public List<ItemStack> loot(Person person) {
        return List.of(
                MAGIC_SCRAP.create(person, random.nextInt(5)+4),
                PURPLE_CRYSTAL_FRAGMENT.create(person, 0.03),
                createItem(Items.COBBLESTONE, random.nextInt(329)+16),
                createItem(Items.IRON_ORE, random.nextInt(16)+1),
                createItem(Items.COAL_ORE, random.nextInt(32)+1),
                createItem(Items.GOLD_ORE, random.nextInt(8)+1),
                createItem(Items.DIAMOND_ORE, random.nextInt(4)+1),
                createItem(Items.COPPER_ORE)
        );
    }

    @Override
    public Set<RPGAbility> abilities() {
        return Set.of();
    }

    @Override
    public void onSpawn(ServerLevel level, Vec3 position) {
        for (Player player : level.getNearbyPlayers(TargetingConditions.DEFAULT, null,
                new AABB(BlockPos.containing(position)).inflate(20, 12, 20))) {
            getRPGPerson(player).addStatus(StatusType.SHAKING, +6_000);
        }
    }

    @Override
    public List<CustomRunnable> runnables() {
        return List.of(
                new CustomRunnable() {
                    int summonCooldown = 7; // 35s
                    int massiveAttackCooldown = 12; // 1m

                    @Override
                    public boolean activity() {
                        LivingEntity target = entity.getTarget();
                        if (target == null) return true;
                        this.summonCooldown--;
                        this.massiveAttackCooldown--;

                        int summoned = 4;
                        Vec3 pos = entity.position();
                        if (entity.getHealth() / entity.getMaxHealth() <= 0.2) {
                            summoned = 8;

                            entity.heal(20);
                            sendPackets(entity.level().getWorld(),
                                    new ClientboundLevelParticlesPacket(ParticleTypes.HEART, false, false,
                                            pos.x, pos.y, pos.z, 0.8f, 1.2f, 0.8f, 0, 6)
                            );
                        }
                        if (summonCooldown == 0) {
                            for (int i = 0; i < summoned; i++) {
                                new Guardian().createWithTarget(GolemBoss.this, target, 1);
                            }
                            this.summonCooldown = 6;
                        }
                        if (massiveAttackCooldown == 0) {
                            sendPackets(entity.level().getWorld(),
                                    new ClientboundLevelParticlesPacket(ParticleTypes.FLAME, false, false,
                                            pos.x, pos.y, pos.z, 8f, 3f, 8f, 0, 50)
                            );
                            for (net.minecraft.world.entity.Entity entity : entity.level().getEntities(entity, entity.getBoundingBox().inflate(8, 3, 8))) {
                                if (entity instanceof ServerPlayer player && player.gameMode().equals(GameType.SURVIVAL)) {
                                    RPGPerson person = getRPGPerson(player.getUUID());
                                    person.addStatus(StatusType.SHAKING, +2_000);
                                    person.addStatus(StatusType.PARALYSIS); // default +2_000
                                }
                            }
                            this.massiveAttackCooldown = 10;
                        }
                        return true;
                    }

                    @Override
                    public long repeating() {
                        return 100;
                    }
                }
        );
    }
}
