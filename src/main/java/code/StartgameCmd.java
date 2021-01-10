package code;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.StructureType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import static code.Utils.*;
import static org.bukkit.ChatColor.*;


public class StartgameCmd implements CommandExecutor {
    public String counterMsg;
    SurvVsHunts plugin;

    public StartgameCmd(SurvVsHunts plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command only for players");
        }
        plugin.surv = sender.getName();
        if (plugin.stage == PRELUDE) {
            plugin.stage = HANDICAP;
            Player p = (Player) sender;
            p.setGameMode(GameMode.SURVIVAL);

            ItemStack compass = new ItemStack(Material.COMPASS);
            CompassMeta meta = (CompassMeta) compass.getItemMeta();
            meta.setDisplayName(DARK_PURPLE + "Ruined portal compass");
            Location portal = p.getLocation().getWorld().locateNearestStructure(p.getLocation(),
                    StructureType.RUINED_PORTAL, 1000, true);
            meta.setLodestoneTracked(false);
            meta.setLodestone(portal);
            compass.setItemMeta(meta);
            p.getInventory().setItemInMainHand(compass);

            say(plugin, DARK_RED + "Game started! RUN, SURV, RUN");
            counterMsg = "Handicap ends after %d ";
            counter(plugin.getConfig().getInt("handicap"));

            plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, ()->{
                for (Player player: plugin.getServer().getOnlinePlayers()) {
                    if (player.getName() != plugin.surv) {
                        player.setCompassTarget(plugin.getServer().getPlayer(plugin.surv).getLocation());
                    }
                }
            }, 20*3L, 20*3L);
        }
        else {
            sender.sendMessage(DARK_PURPLE + "Game already started!");
        }
        return true;
    }


    private void counter(int count) {
        if (count <= 1) {
            if (plugin.stage == HUNTING) {
                plugin.stage = END;
                say(plugin, DARK_GREEN + "Survivor wins!");
            }
            if (plugin.stage == HANDICAP) {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    for (Player p : plugin.getServer().getOnlinePlayers()) {
                        if (p.getName() != plugin.surv) {
                            p.setGameMode(GameMode.SURVIVAL);
                            giveSurvivorCompass(p.getPlayer());
                        }
                    }
                });
                counterMsg = "Game ends after %s ";
                plugin.stage = HUNTING;
                count = plugin.getConfig().getInt("game_duration") * 60;
            }
        }
        if (plugin.stage != END) {
            if (count < 60) {
                say(plugin, String.format(counterMsg, count) + "sec");
            } else {
                say(plugin, String.format(counterMsg, count / 60) + "min");
            }
            int finalCount = count;
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> { counter(finalCount / 2); },
                    20 * count / 2);
        }
    }
}
