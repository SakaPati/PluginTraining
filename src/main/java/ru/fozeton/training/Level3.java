package ru.fozeton.training;

import com.menuapi.events.MenuListener;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.val;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import ru.fozeton.training.commands.MenuCommand;
import ru.fozeton.training.commands.StartBlockPartyCommand;
import ru.fozeton.training.events.SkyWarsListener;

public final class Level3 extends JavaPlugin {
    public static Level3 getInstance() {
        return getPlugin(Level3.class);
    }
    public World skywars;

    @Override
    public void onEnable() {
//        getServer().createWorld(new WorldCreator("skywars"));
            skywars = new WorldCreator("skywars").createWorld();
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new SkyWarsListener(), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            val registrar = commands.registrar();
            registrar.register(new StartBlockPartyCommand().getBuild());
            registrar.register(new MenuCommand().getBuild());
        });
    }

    @Override
    public void onDisable() {
    }
}
