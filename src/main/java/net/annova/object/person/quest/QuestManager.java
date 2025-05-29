package net.annova.object.person.quest;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public class QuestManager {
    private final Set<QuestBase> quests = new HashSet<>();
    private final HashMap<World, Set<QuestBase>> questHashMap = new HashMap<>();

    public void initialization(ResultSet rs) {
        try {
            for (String questData : rs.getString("QUEST_MANAGER").split(",")) {
                QuestBase newQuest = new QuestBase();
                String[] data = questData.split(">");

            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void update() {
        this.questHashMap.clear();
        for (QuestBase base : quests) {
            this.questHashMap.computeIfAbsent(base.getWorld(), k -> new HashSet<>())
                    .add(base);
        }
    }

    public Set<QuestBase> getWorldQuests(World world) {
        return this.questHashMap.get(world);
    }

    public void addQuest(QuestBase quest) {
        this.quests.add(quest);
    }

    /** Information can be split by '>'
     * @return serialized string with quests
     */
    public String serializeQuests() {
        StringJoiner joiner = new StringJoiner(",");
        for (QuestBase quest : this.quests) joiner.add(quest.serialize());
        return joiner.toString();
    }
    public void deserialize(String serializedManager) {
        for (String serialQuest : serializedManager.split(",")) {
            QuestBase quest = new QuestBase();
            String[] questArray = serialQuest.split(">");
            quest.setKey(questArray[0]);
            quest.setGiver(questArray[1]);
            quest.setWorld(Bukkit.getWorld(questArray[2]));
            Set<Condition> conditions = quest.getConditions();
            Set<Reward> reward = quest.getRewards();
            for (String serialCondition : questArray[3].split("`")) conditions.add(Condition.deserialize(serialCondition));
            for (String serialReward : questArray[4].split("`")) reward.add(Reward.deserialize(serialReward));
            quest.setReturnable(Boolean.parseBoolean(questArray[5]));

            this.addQuest(quest);
        }
    }
}
