package net.annova.config;

import net.annova.config.crafts.ConfigCrafts;
import net.annova.exception.AnnovaException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.config.AnnovaConfig.putLangFile;
import static net.annova.main.AnnovaRPG.loadInfo;

public class RPGConfig implements ConfigBase {
    private FileConfiguration craftConfig;
    private static final Map<String, ConfigCrafts> crafts = new HashMap<>();

    public void init() throws IOException, InvalidConfigurationException {
        this.loadConfigFiles();
        this.loadConfigObjects();
    }
    @Override
    public void loadConfigFiles() throws IOException, InvalidConfigurationException {
        getInstance().saveDefaultConfig();

        this.craftConfig = this.loadAnnovaConfig(getInstance(), "crafts");

        Map<String, FileConfiguration> lang = new HashMap<>();
        lang.put("ru", this.loadAnnovaConfig(getInstance(), "lang/russian"));
        loadInfo.add("{pc}| {mc}Языки  {sc}: {ac}" + lang.size());

        putLangFile(getInstance(), lang);
    }

    @Override
    public void loadConfigObjects() {
        try {
            ConfigCrafts.loadConfig(craftConfig, crafts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, ConfigCrafts> getCrafts() {
        return crafts;
    }
    public static Collection<ConfigCrafts> getAllCrafts() {
        return crafts.values();
    }
}
