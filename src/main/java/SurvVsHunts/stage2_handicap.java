package SurvVsHunts;

import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import static org.bukkit.ChatColor.*;
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
        if (plugin.getConfig().getBoolean("gift_comapss")) {
            ItemStack compass = new ItemStack(Material.COMPASS);
            CompassMeta meta = (CompassMeta) compass.getItemMeta();
            meta.setDisplayName(DARK_PURPLE + "Compass to ruined portal");
            Location portal = player.getLocation().getWorld().locateNearestStructure(player.getLocation(),
                    StructureType.RUINED_PORTAL, 1000, true);
            meta.setLodestoneTracked(false);
            meta.setLodestone(portal);
            compass.setItemMeta(meta);
            player.getInventory().setItemInMainHand(compass);
        }
        plugin.say(DARK_RED + "Game started! RUN, SURV, RUN");
        plugin.counter(plugin.getConfig().getInt("handicap"), "Handicap ends after %d", HUNTING);
    }

    // Prevent hunters from moving and looking
    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        if (e.getPlayer().getName() != plugin.surv_player) {
            e.getPlayer().sendMessage(DARK_PURPLE + "Handicap does not end!");
            e.setCancelled(true);
        }
    }

    // Make hunters look down
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        if (e.getPlayer().getName() != plugin.surv_player) {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            Location loc = e.getPlayer().getLocation();
            loc.setPitch(90);
            e.getPlayer().teleport(loc);
        }
    }

    // Surv died before the hunt. Rly?
    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        if (e.getEntity().getName() == plugin.surv_player) {
            plugin.say(DARK_RED + "The loser is dead! Make fun of him!");
            plugin.surv_player = "";
            plugin.change_stage(END);
        }
    }

}