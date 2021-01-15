package SurvVsHunts;

import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import static SurvVsHunts.main.*;


public class stage2_handicap implements Listener {
    main plugin;

    // Stage start
    public stage2_handicap(main plugin) {
        this.plugin = plugin;
        // Let go of the survivor
        Player player = Bukkit.getPlayer(plugin.surv_player);
        player.setGameMode(GameMode.SURVIVAL);
        // Give compass
        if (plugin.getConfig().getBoolean("gift_compass")) {
            ItemStack compass = new ItemStack(Material.COMPASS);
            CompassMeta meta = (CompassMeta) compass.getItemMeta();
            meta.setDisplayName(plugin.getTranslation("gift_compass_name"));
            Location portal = player.getLocation().getWorld().locateNearestStructure(player.getLocation(),
                    StructureType.RUINED_PORTAL, 1000, true);
            meta.setLodestoneTracked(false);
            meta.setLodestone(portal);
            compass.setItemMeta(meta);
            player.getInventory().setItem(4, compass);
        }
        plugin.say("game_started");
        plugin.counter(plugin.getConfig().getInt("handicap"), "handicup_ends", HUNTING);
    }

    // Prevent spectators(hunters) from moving and looking
    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        if (e.getPlayer().getName() != plugin.surv_player) {
            plugin.say(e.getPlayer(), "handicup_doesnt_end");
            e.setCancelled(true);
        }
    }

    // Make spectators(hunters) look down
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        if (e.getPlayer().getName() != plugin.surv_player) {
            Player p = e.getPlayer();
            Location loc = p.getLocation();
            loc.setPitch(-90);
            p.teleport(loc);
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

    // Surv died before the hunt. Rly?
    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        if (e.getEntity().getName() == plugin.surv_player) {
            plugin.say("surv_die_before_hunt_starts");
            plugin.surv_player = "";
            plugin.change_stage(END);
        }
    }

    // Prevent spectators(hunters) from teleporting
    @EventHandler
    public void playerTeleportEvent(PlayerTeleportEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR)
            e.setCancelled(true);
    }

}
