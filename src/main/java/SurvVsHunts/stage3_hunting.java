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
import static org.bukkit.enchantments.Enchantment.VANISHING_CURSE;

import static SurvVsHunts.main.*;


public class stage3_hunting implements Listener {
    main plugin;

    public static void giveSurvivorCompass(Player p) {
        // Dont give duplicate
        for(ItemStack item : p.getInventory().getContents()){
            if (item == null) continue;
            if(item.getItemMeta().getDisplayName().equals(DARK_BLUE + "Hunter's compass")){
                return;
            }
        }
        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        meta.setDisplayName(DARK_BLUE + "Hunter's compass");
        meta.addEnchant(VANISHING_CURSE,1, false);
        compass.setItemMeta(meta);
        p.getInventory().setItem(4, compass);
    }

    public void updateHunterCompass() {
        for (Player player: plugin.getServer().getOnlinePlayers()) {
            if (player.getName() != plugin.surv_player) {
                player.setCompassTarget(plugin.getServer().getPlayer(plugin.surv_player).getLocation());
            }
        }
    }

    // Stage start
    public stage3_hunting(main plugin) {
        this.plugin = plugin;
        if (plugin.getConfig().getBoolean("hunt_in_night")) {
            Bukkit.getPlayer(plugin.surv_player).getWorld().setFullTime(13000);
        }
        // Let go of the hunters
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.getName() != plugin.surv_player) {
                p.setGameMode(GameMode.SURVIVAL);
                giveSurvivorCompass(p.getPlayer());
            }
        }
        // Start updating compass
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, ()->{
            updateHunterCompass();
        }, 20*3L, 20*3L);
        plugin.say(plugin.getConfig().getString("messages.hunt_starts"));
        plugin.counter(plugin.getConfig().getInt("game_duration") * 60,
        plugin.getConfig().getString("messages.game_ends_after"), END);
    }

    // Joining new hunters
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        if (e.getPlayer().getName() != plugin.surv_player) {
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
            giveSurvivorCompass(e.getPlayer());
        }
    }

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent e) {
        if (e.getPlayer().getName() != plugin.surv_player) {
            giveSurvivorCompass(e.getPlayer());
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        if (e.getEntity().getName() == plugin.surv_player) {
            plugin.surv_player = "";
            plugin.change_stage(END);
        }
    }

}
