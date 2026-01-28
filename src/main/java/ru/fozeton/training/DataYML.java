package ru.fozeton.training;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DataYML {
    private final File file;
    private final FileConfiguration configuration;

    public DataYML(Level2 plugin, File dataFolder, String configName) {
        file = new File(dataFolder + File.separator, configName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) file.getParentFile().mkdir();
            try {
                if (plugin.getResource(configName) != null) {
                    plugin.saveResource(configName, true);
                } else file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
        saveData();
    }

    public void saveData() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getData() {
        return configuration;
    }
}