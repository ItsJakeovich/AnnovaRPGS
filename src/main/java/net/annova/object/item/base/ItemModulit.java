package net.annova.object.item.base;

import net.annova.object.person.Person;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import static net.annova.object.item.base.ItemType.getItemType;
import static net.annova.object.item.base.ItemType.updateItemLocal;
import static net.minecraft.core.component.DataComponents.CUSTOM_DATA;

public enum ItemModulit {
    MELANOX_POISON("annovas:mod-mel-poison"),
    SEASMITH_PICKAXE("annovas:mod-seasmith-p")
    ;

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
    public void addModulit(Person person, ItemStack item) {
        if (!item.has(CUSTOM_DATA)) item.set(CUSTOM_DATA, CustomData.of(new CompoundTag()));
        this.addModulit(item.get(CUSTOM_DATA).getUnsafe());
        if (person != null) updateItemLocal(person, getItemType(item), item);
    }
    public void addModulit(CompoundTag tag) {
        tag.putByte(key, (byte) 0);
    }
}