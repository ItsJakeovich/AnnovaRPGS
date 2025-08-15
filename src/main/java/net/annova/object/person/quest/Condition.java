package net.annova.object.person.quest;

import net.annova.object.entity.core.RPGEntity;
import net.annova.object.person.Person;
import net.annova.lang.AISString;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import static net.annova.object.item.base.RPGItem.getItemId;
import static net.annova.main.AnnovaRPG.getInstance;
public class Condition {
    public enum Type {
        LOCATION {
            @Override
            public String resolve(Object obj) {
                Location loc = (Location) obj;
                return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
            }
        },
        ITEMS {
            @Override
            public String resolve(Object obj) {
                ItemStack item = (ItemStack) obj;
                String id = getItemId(item);
                return id == null ? item.getType().name() : id;
            }
        },
        ELIMINATE {
            @Override
            public String resolve(Object obj) {
                return String.valueOf(((RPGEntity) obj).getId());
            }
        };

        public abstract String resolve(Object obj);
    }

    private final Type type;
    private final Object objective;
    private final int amount;

    public Condition(Type type, Object objective) {
        this(type, objective, 1);
    }
    public Condition(Type type, Object objective, int amount) {
        this.type = type;
        this.objective = objective;
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public Object getObjective() {
        return objective;
    }

    public int getAmount() {
        return amount;
    }

    public AISString getDesctiption(Person person) {
        return person.getAISString(getInstance(), type.toString()).replaceTag("{obj}", type.resolve(objective));
    }

    /** Information can be split by '-'
     * 1. is enum Type
     * 2. is objective
     * 3. is amount in objective (TEN zombies, ...)
     */
    public String serialize() {
        return type.name() + "-" + type.resolve(objective) + "-" + amount;
    }
    public static Condition deserialize(String serialized) {
        String[] data = serialized.split("-");
        return new Condition(Type.valueOf(data[0]), data[1], Integer.parseInt(data[2]));
    }
}
