package net.annova.object.item.base;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import static net.minecraft.core.component.DataComponents.CUSTOM_DATA;

public enum ItemModulit {
    MELANOX_POISON("annovas:mod-mel-poison");

    private final String key;
    ItemModulit(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean hasModulit(CompoundTag tag) {
        return tag.contains(key);
    }
    public void addModulit(ItemStack item) {
        if (!item.has(CUSTOM_DATA)) item.set(CUSTOM_DATA, CustomData.EMPTY);
        this.addModulit(item.get(CUSTOM_DATA).getUnsafe());
    }
    public void addModulit(CompoundTag tag) {
        tag.putByte(key, (byte) 0);
    }
}