package net.annova.object.magic.physic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.annova.main.AnnovaRPG.getInstance;

public class EntRoots {
    public static AttributeModifier MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("annovas", "root_debuff")
            , -0.70f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
    );
    public static Map<UUID, Long> entities = new HashMap<>();

    public static void addPhysicDebuff(LivingEntity target) {
        long current = System.currentTimeMillis();
        UUID uuid = target.getUUID();
        if (entities.getOrDefault(uuid, 0L) > current) return;
        AttributeInstance movementInstance = target.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementInstance.hasModifier(MODIFIER.id())) return;

        movementInstance.addTransientModifier(MODIFIER);
        // + Jump strength debuff
        AttributeInstance jumpInstance = target.getAttribute(Attributes.JUMP_STRENGTH);
        jumpInstance.addTransientModifier(MODIFIER);

        entities.put(uuid, current+3_000); // длительность
        Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
            if (entities.get(uuid) > System.currentTimeMillis()) return;
            movementInstance.removeModifier(MODIFIER);
            jumpInstance.removeModifier(MODIFIER);
            entities.put(uuid, System.currentTimeMillis()+4000); // перезарядка
        }, 80L);
    }
}