package ru.fozeton.training;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.fozeton.training.task1.Hello;
import ru.fozeton.training.task1.commandEvent.CommandEvent;


import java.io.File;
import java.io.IOException;

import static ru.fozeton.training.task1.CommandPrac.*;

public final class Level1 extends JavaPlugin {
    public static Level1 instance;
    public static Level1 getInstance() {
        return instance;
    }

    private File tpFile;
    private FileConfiguration tpConfig;
    private File helloFile;
    private FileConfiguration helloConfig;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("tp_locations.yml", false);
        tpFile = new File(this.getDataFolder(), "tp_locations.yml");
        tpConfig = YamlConfiguration.loadConfiguration(tpFile);
        saveResource("hello.yml", false);
        helloFile = new File(this.getDataFolder(), "hello.yml");
        helloConfig = YamlConfiguration.loadConfiguration(helloFile);

        getServer().getPluginManager().registerEvents(new Hello(), this);
//        getServer().getPluginManager().registerEvents(new Spawn(), this);
        getServer().getPluginManager().registerEvents(new CommandEvent(), this);
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(btpCommand);
            commands.registrar().register(sethealthCommand);
            commands.registrar().register(showtextCommand);
            commands.registrar().register(godCommand);
            commands.registrar().register(flyCommand);
            commands.registrar().register(addEffectCommand);
            commands.registrar().register(getminCommand);
            commands.registrar().register(duplicatesCommand);
            commands.registrar().register(addnumCommand);
            commands.registrar().register(getnumCommand);
        });
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(instance);
    }

    public FileConfiguration getTpConfig() {
        return tpConfig;
    }

    public void saveTpConfig() {
        try {
            tpConfig.save(tpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getHelloConfig() {
        return helloConfig;
    }

    public void saveHelloConfig() {
        try {
            helloConfig.save(helloFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
