package net.annova.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;

public enum AMaterial {
    bw(Blocks.BLACKSTONE_WALL),
    black_conctr(Blocks.BLACK_CONCRETE)
    ;
    private final BlockState material;

    AMaterial(Block material) {
        this.material = material.defaultBlockState();
    }

    public static BlockState getMaterial(String id) {
        try {
            return AMaterial.valueOf(id).material;
        } catch (IllegalArgumentException e) {
            try {
                return BlockStateParser.parseForBlock(
                        BuiltInRegistries.BLOCK,
                        new StringReader("minecraft:"+id), false).blockState();
            } catch (CommandSyntaxException | IllegalArgumentException ex) {
                Bukkit.getLogger().warning("Не найден: " + id);
                return Blocks.REDSTONE_BLOCK.defaultBlockState();
            }
        }
    }
}
