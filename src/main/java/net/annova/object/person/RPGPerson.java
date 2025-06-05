package net.annova.object.person;

import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.world.RPGAdvancement;
import net.annova.object.item.base.*;
import net.annova.main.AnnovaAPI;
import net.annova.main.AnnovaGlobal;
import net.annova.main.AnnovaSQL;
import net.annova.object.Person;
import net.annova.object.item.base.RPGSet;
import net.annova.object.person.quest.QuestManager;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.ValueType;
import org.bukkit.*;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Set;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.addRPGPerson;
import static net.annova.util.MessageObjects.sendForAll;

public class RPGPerson {
    private final Person person;
    private Dungeon dungeon;
//    private Location location =
    private int dungeonPoints = 0;
    private Team team;
    private RPGSet set;
    private final RPGItem[] armor = new RPGItem[4];
    private boolean ready = false;
    private final StatusManager statusManager = new StatusManager(this);
    private final List<ItemStack> pendingItems = new ArrayList<>();
    private final Map<String, Long> speedModifiers = new HashMap<>();
    private final QuestManager questManager = new QuestManager();
    private boolean teleportAccess = false;

    private final EnumSet<RPGAdvancement> advancements = EnumSet.noneOf(RPGAdvancement.class);

    private float health;
    private float maxHealth = 20;
    private float regeneration = 0f;
    private float additionalHealth = 0;

    private static final String TABLE = "RPGPERSON";
    public static void init() {
        AnnovaSQL sql = AnnovaAPI.getSQL();
        sql.initTable(TABLE,
                "NAME VARCHAR(24)",
                "QUEST_MANAGER MEDIUMTEXT",

                "PRIMARY KEY (NAME)"
        );
        sql.loadFromDB(TABLE, RPGPerson.class)
                .forEach(person -> addRPGPerson((RPGPerson) person));
    }
    public void save() {
        Map<String, Object> save = new HashMap<>();
        save.put("NAME", this.person.getName());
        save.put("QUEST_MANAGER", this.questManager.serializeQuests());
        AnnovaAPI.getSQL().saveObject(TABLE, "NAME", save);
    }
    
    public RPGPerson(Person person) {
        this.person = person;
    }
    public RPGPerson(ResultSet rs) throws SQLException {
        this.person = AnnovaGlobal.getPerson(rs.getString("NAME"));
        this.questManager.deserialize(rs.getString("QUEST_MANAGER"));
    }

    public Person getPerson() {
        return person;
    }

    @Nullable
    public Dungeon getDungeon() {
        return dungeon;
    }
    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public int getDungeonPoints() {
        return dungeonPoints;
    }
    public void setDungeonPoints(int points) {
        this.dungeonPoints = points;
    }
    public void addDungeonPoints(int points) {
        if (this.dungeon == null) return;
        int newPoints = (int) (points * (1 + ((dungeon.getPlayers().size()-1)*0.3)));
        this.dungeonPoints = Math.min(dungeonPoints + newPoints, 10000);

        this.person.sendBar(person.getString(getInstance(), "dungeon-points")
                .replace("{plus}", String.valueOf(newPoints))
                .replace("{points}", String.valueOf(dungeonPoints))
        );
        Player player = this.getPerson().getPlayer();
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
    }
    public void reduceDungeonPoints(int points) {
        this.dungeonPoints -= points;
    }

    public void damage(int damage) {
        this.setHealth(this.health - damage);
    }
    public void regen() {
        float regeneration = this.regeneration;
        float food = this.person.getPlayer().getFoodLevel();
        if (food == 0) regeneration -= 6;
        else           regeneration += 3 * food/20;

        this.regen(regeneration);
    }
    public void regen(float regen) {
        this.setHealth(this.health + regen);
    }
    public void setHealth(float health) {
        if (health <= 0) {
            this.death();
            health = maxHealth;
        }
        if (health > maxHealth) health = maxHealth;
        this.health = health;
        this.updateStatusBar();
    }
    public void updateHealth() {
        float newMaxHealth = 20;
        Player player = person.getPlayer();
        if (player == null) return;

        newMaxHealth += additionalHealth
                + (float) (statusManager.getFinalValue(ValueType.HEALTH_BUFF) - statusManager.getFinalValue(ValueType.HEALTH_DEBUFF));

        this.setHealth(newMaxHealth * health / maxHealth);
        this.maxHealth = newMaxHealth;
    }
    public void updateStatusBar() {
        String message = this.person.getString(getInstance(), "bar-health")
                .replace("{h}", String.valueOf((int) this.health))
                .replace("{m}", String.valueOf((int) this.maxHealth));
        Player player = this.person.getPlayer();
        player.setHealth(this.person.getMaxHealth(player) * health/maxHealth);
        this.person.sendBar(player, message);
    }

    public void death() {
        Player player = this.person.getPlayer();
        if (dungeon != null) {
            player.setGameMode(GameMode.SPECTATOR);
            sendForAll(getInstance(), "death-dungeon", "{name}", this.person.getName());
            this.startDungeonRespawn(player);
        } else {
            teleportHome(player);
            sendForAll(getInstance(), "death-base", "{name}", this.person.getName());
        }
    }
    private void startDungeonRespawn(Player player) {
        new BukkitRunnable() {
            private int delay = 12;
            @Override
            public void run() {
                if (delay == 0) {
                    player.setGameMode(GameMode.SURVIVAL);
                    if (dungeon == null) {
                        Location loc = player.getRespawnLocation();
                        RPGPerson.this.teleport(loc == null ? Bukkit.getWorld("world").getSpawnLocation() : loc);
                    } else {
                        dungeon.teleportPerson(RPGPerson.this);
                        player.playSound(player, Sound.BLOCK_ANVIL_BREAK, 1, 1);
                    }
                    this.cancel();
                }
                person.sendBar(getInstance(), "dungeon-respawn", "{delay}", String.valueOf(delay));
                player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
                delay--;
            }
        }.runTaskTimer(getInstance(), 0L, 20L);
    }

    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }

    @Nullable
    public RPGSet getSet() {
        return set;
    }
    public void setSet(net.annova.object.item.base.Set base) {
        RPGSet set = null;
        if (base == null) {
            if (this.set == null) return;
            additionalHealth = 0;
            regeneration = 0f;
        } else {
            set = base.getSet();
            if (this.set == set) return;

            additionalHealth = set.health();
            regeneration = set.regeneration();
        }

        this.updateHealth();

        this.set = set;
    }
    public void updateSet() {
        Player player = this.getPerson().getPlayer();
//        this.setSet(Set.getSet(player));
    }
    public void updateArmor() {
//        Player player = this.getPerson().getPlayer();
//        ItemStack[] armorContent = player.getEquipment().getArmorContents();
//        for (int i = 0; i < 4; i++) {
//            RPGItem piece = (RPGItem) getArmor(armorContent[i], i);
//            if (this.armor[i] == piece) continue;
//
//            this.armor[i] = piece;
//
//            if (!(piece instanceof RPGArmorActivity activity)) continue;
//            if (activity.repeating() == 0) continue;
//
//            int slot = i;
//            new BukkitRunnable() {
//                private final Player pl = RPGPerson.this.getPerson().getPlayer();
//
//                @Override
//                public void run() {
//                    if (armor[slot] != piece) this.cancel();
//                    if (!pl.isOnline()) return;
//
//                    activity.activity(pl);
//                }
//            }.runTaskTimer(getInstance(), 0, activity.repeating());
//        }
    }

    public RPGItem[] getRPGArmor() {
        return armor;
    }

    public void updateSpeed(float speed) {
        float plus = 0;
        if (speed != 0) {
            Material type = person.getPlayer().getLocation().add(0, -1, 0).getBlock().getType();
            for (String modifier : speedModifiers.keySet()) {
                String[] attribute = modifier.split(":");
                if (attribute.length > 2) if (type != Material.valueOf(attribute[2])) continue;
                plus += Float.parseFloat(attribute[1]);
            }
            plus += set == null ? 0 : set.speed();
        }
        this.person.setAdditionalSpeed(plus);

        this.person.setSpeed(speed);
    }

    public boolean isReady() {
        return ready;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public StatusManager getStatusManager() {
        return statusManager;
    }

    public List<ItemStack> getPendingItems() {
        return pendingItems;
    }
    public void addPendingItem(ItemStack toGive) {
        if (toGive == null || toGive.getType().equals(Material.AIR)) return;
        this.pendingItems.add(toGive);
    }
    public void clearPending() {
        this.pendingItems.clear();
    }

    public Map<String, Long> getSpeedModifiers() {
        return speedModifiers;
    }
    public long getSpeedModifier(String modifier) {
        return speedModifiers.get(modifier);
    }
    public void addSpeedModifier(String modifier) {
        speedModifiers.put(modifier, System.currentTimeMillis());
    }
    public void removeSpeedModifier(String modifier) {
        speedModifiers.remove(modifier);
    }

    public float getHealth() {
        return health;
    }
    public float getMaxHealth() {
        return maxHealth;
    }
    public float getRegeneration() {
        return regeneration;
    }
    public float getAdditionalHealth() {
        return additionalHealth;
    }

    public boolean isTeleportAccess() {
        return teleportAccess;
    }
    public void setTeleportAccess(boolean teleportAccess) {
        this.teleportAccess = teleportAccess;
    }
    public void teleport(Player player) {
        this.teleport(person.getPlayer(), player.getLocation());
    }

    public void teleport(Location location) {
        this.teleport(person.getPlayer(), location);
    }
    public void teleport(Player player, Location location) {
        this.teleportAccess = true;
        player.teleport(location);
    }

    public Set<RPGAdvancement> getAdvancements() {
        return advancements;
    }
    public void addAdvancement(RPGAdvancement advancement) {
        this.advancements.add(advancement);
    }
    public void removeAdvancement(RPGAdvancement advancement) {
        this.advancements.remove(advancement);
    }
    public void completeAdvancement(Player player, RPGAdvancement advancement) {
        if (this.advancements.contains(advancement)) return;
        this.advancements.add(advancement);
        AdvancementProgress progress = player.getAdvancementProgress(advancement.resolve());
        for (String criterion : progress.getRemainingCriteria()) {
            progress.awardCriteria(criterion);
        }
    }

    public static void teleportHome(Player player) {
        Location loc = player.getRespawnLocation();
        player.playSound(player, Sound.BLOCK_GLASS_BREAK, 3, 1);
        player.teleport(loc == null ? player.getWorld().getSpawnLocation() : loc);
    }
}
