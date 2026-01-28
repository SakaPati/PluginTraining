package ru.fozeton.training;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.fozeton.training.Task2.Commands.*;
import ru.fozeton.training.Task2.Event.*;
import ru.fozeton.training.Task2.PlayerScoreboard;

public final class Level2 extends JavaPlugin {
    public static Level2 instance;
    public static Level2 getInstance() {
        return instance;
    }
    public static DataYML configMenu;
    public static DataYML configBoss;

    @Override
    public void onEnable() {
        instance = this;
        PlayerScoreboard sbManager = new PlayerScoreboard();
        configMenu = new DataYML(this, getDataFolder(), "menus.yml");
        configBoss = new DataYML(this, getDataFolder(), "bosses.yml");

        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new EntityTargetLivingEntity(), this);
        getServer().getPluginManager().registerEvents(new EntityDamage(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEntity(), this);
        getServer().getPluginManager().registerEvents(new FurnaceStartSmelt(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(sbManager), this);
        getServer().getPluginManager().registerEvents(new EntityDeath(sbManager), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(sbManager), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(sbManager), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(sbManager), this);
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(SetPosCommand.build);
            commands.registrar().register(SpawnSheepCommand.build);
            commands.registrar().register(SpawnSpiderCommand.build);
            commands.registrar().register(SpawnMerchant.build);
            commands.registrar().register(TabNameCommand.build);
            commands.registrar().register(OpenMenuCommand.build);
            commands.registrar().register(OpenInvCommand.build);
            commands.registrar().register(SpawnBossCommand.build);
        });
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(instance);
    }
}
