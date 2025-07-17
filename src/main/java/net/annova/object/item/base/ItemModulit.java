package net.annova.object.item.base;

import net.minecraft.nbt.CompoundTag;

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
    public void addModulit(CompoundTag tag) {
        tag.putByte(key, (byte) 0);
    }
}