package net.annova.object.person.quest;

import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class QuestBase {
    private String key = "???";
    private String giver = "???";
    private World world = null;
    private final Set<Condition> conditions = new HashSet<>();
    private final Set<Reward> rewards = new HashSet<>();
    private boolean returnable = false;

    /**
     * Key - Unique key for identifier quest, getting quest, getting quest name from locale.
     * Giver - quest giver.
     * Conditions - quest tasks for complete him.
     * Reward - reward after completing.
     * returning - is need to go back to the giver ?
     */

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getGiver() {
        return giver;
    }
    public void setGiver(String giver) {
        this.giver = giver;
    }

    public World getWorld() {
        return world;
    }
    public void setWorld(World world) {
        this.world = world;
    }

    public Set<Condition> getConditions() {
        return conditions;
    }

    public Set<Reward> getRewards() {
        return rewards;
    }

    public boolean isReturnable() {
        return returnable;
    }
    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }

    /** Information can be split by '>'
     *  0. is 'key'
     *  1. is 'giver'
     *  2. is 'worldName'
     *  3. is serialized 'Conditions' (split it by '-')
     *  4. is 'rewards' (split it by '-')
     *  5. is 'returnable'
     */
    public static int getBaseLength() {
        return 135; // 8 + 10 + 3 + 20*2 + 12*2 + 50
    }
    public String serialize() {
        StringBuilder builder = new StringBuilder(key.length()+
                giver.length()+
                world.getName().length()+
                conditions.size()*20+
                rewards.size()*12
                +50
        );
        builder.append(STR."\{key}>\{giver}>\{world.getName()}>");
        boolean first = true;
        for (Condition condition : conditions) {
            if (!first) builder.append('`');
            builder.append(STR.">\{condition.serialize()}");
            first = false;
        }
        builder.append('>');
        first = true;
        for (Reward reward : rewards) {
            if (!first) builder.append('`');
            builder.append(STR.">\{reward.serialize()}");
            first = false;
        }
        return builder.toString();
    }
}
