package net.annova.object.person;

import net.annova.environment.dungeon.Dungeon;
import net.annova.entity.traits.status.Status;
import net.annova.entity.traits.status.RPGStatus;
import net.annova.item.base.*;
import net.annova.item.base.Set;
import net.annova.main.AnnovaAPI;
import net.annova.main.AnnovaGlobal;
import net.annova.main.AnnovaSQL;
import net.annova.object.Person;
import net.annova.object.person.quest.QuestManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.addRPGPerson;

public class RPGPerson {
    private final Person person;
    private Dungeon dungeon;
    private int dungeonPoints = 0;
    private Team team;
    private RPGSet set;
    private final RPGItem[] armor = new RPGItem[4];
    private boolean ready = false;
    private final Map<Status.Type, Status> statuses = new HashMap<>();
    private final List<ItemStack> pendingItems = new ArrayList<>();
    private final Map<String, Long> speedModifiers = new HashMap<>();
    private final QuestManager questManager = new QuestManager();
    private boolean teleportAccess = false;

    private int health;
    private int maxHealth = 20;
    private float regeneration = 0f;
    private int additionalHealth = 0;

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
        this.setHealth(this.health + (int) regen);
    }
    public void setHealth(int health) {
        if (health <= 0) {
            teleportHome(this.getPerson().getPlayer());
            this.setHealth(maxHealth);
            return;
        }
        if (health > maxHealth) health = maxHealth;
        this.health = health;
        this.updateStatusBar();
    }
    public void updateHealth() {
        int newMaxHealth = 20;
        Player player = person.getPlayer();
        if (player == null) return;

        newMaxHealth += additionalHealth;
        if (player.getHealth() > newMaxHealth) player.setHealth(newMaxHealth);
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(newMaxHealth);

        this.setHealth(newMaxHealth * health / maxHealth);
        this.maxHealth = newMaxHealth;
    }
    public void updateStatusBar() {
        String message = this.person.getString(getInstance(), "bar-health")
                .replace("{h}", String.valueOf(this.health))
                .replace("{m}", String.valueOf(this.maxHealth));
        Player player = this.person.getPlayer();
        player.setHealth(this.person.getMaxHealth(player) * health/maxHealth);
        this.person.sendBar(player, message);
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
    public void setSet(net.annova.item.base.Set base) {
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
        this.setSet(Set.getSet(player));
    }
    public void updateArmor() {
        Player player = this.getPerson().getPlayer();
        ItemStack[] armorContent = player.getEquipment().getArmorContents();
        for (int i = 0; i < 4; i++) {
            RPGItem piece = (RPGItem) getArmor(armorContent[i], i);
            if (this.armor[i] == piece) continue;

            this.armor[i] = piece;

            if (!(piece instanceof RPGArmorActivity activity)) continue;
            if (activity.repeating() == 0) continue;

            int slot = i;
            new BukkitRunnable() {
                private final Player pl = RPGPerson.this.getPerson().getPlayer();

                @Override
                public void run() {
                    if (armor[slot] != piece) this.cancel();
                    if (!pl.isOnline()) return;

                    activity.activity(pl);
                }
            }.runTaskTimer(getInstance(), 0, activity.repeating());
        }
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

    @Nullable
    public Status getStatus(Status.Type type) {
        return statuses.get(type);
    }
    public boolean containStatus(Status.Type type) {
        return statuses.containsKey(type);
    }
    public Status addStatus(Status.Type type) {
        return this.addStatus(type, 0);
    }
    private final Map<Status.Type, Long> statusCooldown = new HashMap<>();
    @Nullable
    public Status addStatus(Status.Type type, long expires) {
        long cooldown = type.getCooldown();
        if (cooldown != 0) {
            long millis = System.currentTimeMillis();
            if (millis < statusCooldown.getOrDefault(type, 0L)) return null;
            statusCooldown.put(type, millis+cooldown);
        }
        Status status = new Status(type);
        this.statuses.put(type, status);
        return status;
    }
    public void removeStatus(Status.Type type) {
        if (statuses.containsKey(type)) {
            RPGStatus status = statuses.get(type).getActivity();
            if (status != null) status.stop(this, type);
            statuses.remove(type);
        }
    }
    public Collection<Status> getStatuses() {
        return statuses.values();
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

    public boolean isTeleportAccess() {
        return teleportAccess;
    }
    public void setTeleportAccess(boolean teleportAccess) {
        this.teleportAccess = teleportAccess;
    }
    public void teleport(Player player) {
        this.teleport(person.getPlayer(), player.getLocation());
    }
    public void teleport(Person person) {
        this.teleport(person.getPlayer(), person.getLocation());
    }
    public void teleport(Location location) {
        this.teleport(person.getPlayer(), location);
    }
    public void teleport(Player player, Location location) {
        this.teleportAccess = true;
        player.teleport(location);
    }

    public static void healPlayer(Player player, double regen) {
        player.setHealth(Math.min(player.getHealth() + regen
                , player.getAttribute(Attribute.MAX_HEALTH).getValue()));
    }
    public static void teleportHome(Player player) {
        Location loc = player.getRespawnLocation();
        player.teleport(loc == null ? player.getWorld().getSpawnLocation() : loc);
    }
}
