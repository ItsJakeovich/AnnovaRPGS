package net.annova.object.magic.physic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;

import java.util.HashMap;
import java.util.Map;

import static net.annova.main.AnnovaRPG.getInstance;

public class EntRoots {
    public static AttributeModifier modifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("annovas", "root_debuff")
            , -0.70f, AttributeModifier.Operation.ADD_VALUE
    );
    public static Map<Entity, Long> entities = new HashMap<>();

    public static void addPhysicDebuff(LivingEntity target) {
        AttributeInstance movementInstance = target.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementInstance.hasModifier(modifier.id())) return;

        movementInstance.addTransientModifier(modifier);
        // + Jump strength debuff
        AttributeInstance jumpInstance = target.getAttribute(Attributes.JUMP_STRENGTH);
        jumpInstance.addTransientModifier(modifier);

        entities.put(target, System.currentTimeMillis()+3_000);
        Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
            if (entities.get(target) > System.currentTimeMillis()) return;
            movementInstance.removeModifier(modifier);
            jumpInstance.removeModifier(modifier);
        }, 80L);
    }
}
