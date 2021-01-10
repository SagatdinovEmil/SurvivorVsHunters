package ru.FullmetalTatarin;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.StructureType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class StartgameCmd implements CommandExecutor {
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
            if (!plugin.isStart) {
                plugin.isStart = true;
                Player p = (Player) sender;
                p.setGameMode(GameMode.SURVIVAL);
                p.getInventory().setItemInMainHand(Utils.buildItem(Material.COMPASS, 0, 1,
                        "&1Ruined portal compass", new String[]{"   ", "This compass setted on nearest ruined portal", "   "}));
                Location portal = p.getLocation().getWorld().locateNearestStructure(p.getLocation(),
                        StructureType.RUINED_PORTAL, 500, true);
                p.setCompassTarget(portal);
                List<Player> players = (List<Player>) plugin.getServer().getOnlinePlayers();
                for (Player player : players) {
                    player.sendMessage("§4Game started! RUN, SURV, RUN");
                }

                counterHandicup(plugin.getConfig().getInt("handicap"), "Handicup ends after %d ");

                plugin.getServer().getScheduler().runTaskTimer(plugin, ()->{
                    plugin.getServer().getPlayer(plugin.surv).setCompassTarget(portal);
                }, 20*10, 20*10);


                plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, ()->{
                    for (Player player: plugin.getServer().getOnlinePlayers()) {
                        if (player.getName()!=plugin.surv) {
                            player.setCompassTarget(plugin.getServer().getPlayer(plugin.surv).getLocation());
                        }
                    }
                }, 20*3L, 20*3L);
            }
            else {
                sender.sendMessage("§5Game already started!");
            }
        return true;
    }


    private void counterHandicup(int count, String msg) {
        if (count <= 1) {
            plugin.isHandicapEnds = true;
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    p.setGameMode(GameMode.SURVIVAL);
                    if (p.getName()!=plugin.surv) {
                        p.getInventory().setItemInMainHand(Utils.buildItem(Material.COMPASS, 0, 1,
                                "&1Survivor compass", new String[]{"   ", "This compass setted on survivor", "   "}));
                    }
                }

            });
            counterGameEnds(plugin.getConfig().getInt("game_duration") * 60, "Game ends after %s ");
        } else {
            if (count < 60) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    player.sendMessage(String.format(msg, count) + "sec");
                }
            } else {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    player.sendMessage(String.format(msg, count / 60) + "min");
                }
            }
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                counterHandicup(count / 2, msg);
            }, 20 * count / 2);
        }
    }


    private void counterGameEnds(int count, String msg) {
        if (count <= 1) {
            plugin.surv = "";
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                player.sendMessage("§2Survivor wins!");
            }
        } else {
            if (count < 60) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    player.sendMessage(String.format(msg, count) + "sec");
                }
            } else {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    player.sendMessage(String.format(msg, count / 60) + "min");
                }
            }
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                counterGameEnds(count / 2, msg);
            }, 20 * count / 2);
        }
    }
}
