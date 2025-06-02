package net.annova.object.person.quest;

import org.bukkit.inventory.ItemStack;

import static net.annova.object.item.base.RPGItem.getItemId;

public class Reward {
    public enum Type {
        MONEY {
            @Override
            public String resolve(Object obj) {
                return String.valueOf(obj);
            }
        },
        ITEM {
            @Override
            public String resolve(Object obj) {
                ItemStack item = (ItemStack) obj;
                String id = getItemId(item);
                return id == null ? item.getType().name() : id;
            }
        },
        EXP {
            @Override
            public String resolve(Object obj) {
                return String.valueOf(obj);
            }
        };

        public abstract String resolve(Object obj);
    }

    private final Type type;
    private final Object reward;

    public Reward(Type type, Object reward) {
        this.type = type;
        this.reward = reward;
    }

    public Type getType() {
        return type;
    }

    public Object getReward() {
        return reward;
    }

    /** Information can be split by '-'
     * 1. is reward type
     * 2. is reward (EXP, MONEY, ITEM)
     */
    public String serialize() {
        return STR."\{type.name()}-\{type.resolve(reward)}";
    }
    public static Reward deserialize(String serialized) {
        String[] data = serialized.split("-");
        Type type = Type.valueOf(data[0]);
        Object reward = switch (type) {
            case MONEY, ITEM, EXP -> Integer.parseInt(data[1]);
        };
        return new Reward(type, reward);
    }
}
