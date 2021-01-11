package SurvVsHunts;

import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static SurvVsHunts.main.*;


public class stage1_prelude implements Listener {
    main plugin;

    // Stage start
    public stage1_prelude(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("startgame").setExecutor(new StartgameCmd(plugin));
    }

    // Prevent players from moving and looking
    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        e.setCancelled(true);
        plugin.say(e.getPlayer(), "game_doesnt_start");
    }

    // Make players look down
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setGameMode(GameMode.SPECTATOR);
        Location loc = p.getLocation();
        loc.setPitch(90);
        p.teleport(loc);
        plugin.say(p, "enter_instructions");
    }

    // Stage end by /startgame or /go
    public class StartgameCmd implements CommandExecutor {
        main plugin;
        public StartgameCmd(main plugin) { this.plugin = plugin; }
        
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command only for players");
                return false;
            }
            plugin.surv_player = sender.getName();
            plugin.getCommand("startgame").setExecutor(null);
            plugin.change_stage(HANDICAP);
            return true;
        }
    }
}
