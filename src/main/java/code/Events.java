package code;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import static code.Utils.*;
import static org.bukkit.ChatColor.*;


public class Events implements Listener {
    SurvVsHunts plugin;

    public Events(SurvVsHunts plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        if (plugin.stage == PRELUDE) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(DARK_PURPLE + "Game does not start!");
        } else {
            if (plugin.stage == HANDICAP) {
                if (e.getPlayer().getName() != plugin.surv) {
                    e.getPlayer().sendMessage(DARK_PURPLE + "Handicap does not end!");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        if ((plugin.stage == HANDICAP) || (plugin.stage == HUNTING)) {
            if (e.getEntity().getName() == plugin.surv) {
                say(plugin, DARK_RED + "Game ends! Hunters win!");
                plugin.stage = END;
            }
        }
    }

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent e) {
        if ((plugin.stage == HUNTING) && (e.getPlayer().getName() != plugin.surv)) {
            giveSurvivorCompass(e.getPlayer());
        }
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        if (plugin.stage == PRELUDE) {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            // Making the player look straight down
            Location loc = e.getPlayer().getLocation();
            loc.setPitch(90);
            e.getPlayer().teleport(loc);
        }
        if (e.getPlayer().getName() != plugin.surv) {
            if (plugin.stage == HANDICAP) {
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
            if (plugin.stage == HUNTING) {
                e.getPlayer().setGameMode(GameMode.SURVIVAL);
                giveSurvivorCompass(e.getPlayer());
            }
        }
    }

}
