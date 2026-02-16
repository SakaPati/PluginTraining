package ru.fozeton.training;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;
import ru.fozeton.training.commands.StartBlockPartyCommand;

public final class Level3 extends JavaPlugin {
    public static Level3 getInstance() {
        return getPlugin(Level3.class);
    }

    @Override
    public void onEnable() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            val registrar = commands.registrar();
            registrar.register(new StartBlockPartyCommand().getBuild());
        });
    }

    @Override
    public void onDisable() {
    }
}
