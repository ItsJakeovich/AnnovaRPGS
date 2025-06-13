package net.annova.environment.structure;

import net.annova.object.Person;
import net.annova.object.SimpleConstruction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static net.annova.main.AnnovaAPI.getSQL;
import static net.annova.main.GlobalRPG.getStructure;
import static net.annova.util.GlobalTasks.importBuild;

public abstract class StructureBase {
    public BukkitTask runnable;
    public Structure structure;
    public static final String TABLE = "STRUCTURE";

    public abstract void onInit();
    public abstract void onInit(ResultSet rs) throws SQLException;
    public abstract void onTask();
    public abstract void onStart();

    public abstract void onSave(Map<String, Object> save);
    public abstract boolean isSave();

    private boolean start = false;

    public boolean isStart() {
        return start;
    }
    public void setStart(boolean start) {
        this.start = start;
    }

    public StructureBase(){}
    public StructureBase(ResultSet rs) throws SQLException {
        structure = getStructure(rs.getString("NAME"));
        start = rs.getInt("START") == 1;

        String[] loc = rs.getString("LOCATION").split(",");
        structure.getConstruction().setBuildPoint(new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3])));

        this.onInit(rs);

        if (runnable != null) runnable.cancel();
        if (start) this.onStart();
        else       this.onTask();
    }

    public void save() {
        if (structure == null) return;
        SimpleConstruction construction = structure.getConstruction();
        Location build = construction.getBuildPoint();
        Map<String, Object> save = new HashMap<>();
        save.put("NAME", construction.getKey());
        save.put("LOCATION", build.getWorld().getName() + "," + build.getBlockX() + "," + build.getBlockY() + "," + build.getBlockZ());
        save.put("START", this.isStart());

        this.onSave(save);

        getSQL().saveObject(TABLE, "NAME", save);
    }

    public static void init() {
        getSQL().initTable(TABLE,
                "NAME VARCHAR(36)",
                "LOCATION VARCHAR(128)",
                "DATA VARCHAR(11693)",
                "START TINYINT(1)",

                "PRIMARY KEY (NAME)"
        );
    }

    public void create(Structure structure, Location location) {
        if (runnable != null) runnable.cancel();
        this.structure = structure;
        SimpleConstruction construction = structure.getConstruction();
        construction.setBuildPoint(location);
        importBuild(structure.getConstruction().getStructure(), Person.CardinalDirection.NORTH, location);

        this.onInit();

        this.onTask();
    }
}
