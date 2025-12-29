package ru.fozeton.training.task1;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.fozeton.training.Level1;

public class Hello implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
//        player.sendMessage("Привет " + player.getName());

        String message = Level1.getInstance().getHelloConfig().getString("helloTxt");
        long lastLogin = Level1.getInstance().getConfig().getLong("LastPlayerLogin");
        long currentTime = System.currentTimeMillis();
        long timeLogin = currentTime - lastLogin;
        if(message != null){
            if(timeLogin < 60*1000) return;
            message = message.replace("{player}", player.getName());
            player.sendMessage(message);
        }
        Level1.getInstance().getConfig().set("LastPlayerLogin", player.getLastLogin());
        Level1.getInstance().saveConfig();
    }
}
