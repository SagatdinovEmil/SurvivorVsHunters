package ru.FullmetalTatarin;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.StructureType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Events implements Listener {
    SurvVsHunts plugin;

    public Events(SurvVsHunts plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void moveEvent(PlayerMoveEvent e) {
        if (!plugin.isStart) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§5Game does not start!");
        } else {
            if (!plugin.isHandicapEnds) {
                if (e.getPlayer().getName() != plugin.surv) {
                    e.getPlayer().sendMessage("§5Handicap does not end!");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void interractEvent(PlayerInteractEvent e) {
        if (!plugin.isStart) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§5Game does not start!");
        } else {
            if (!plugin.isHandicapEnds) {
                if (e.getPlayer().getName() != plugin.surv) {
                    e.getPlayer().sendMessage("§5Handicap does not end!");
                    e.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void deathEvent(PlayerDeathEvent e) {
        if (plugin.isStart) {
            if (e.getEntity().getName() == plugin.surv) {
                for (Player player:plugin.getServer().getOnlinePlayers()) {
                    player.sendMessage("§4Game ends! Hunters win!");
                }
                plugin.surv = "";
            }
        }
    }


    @EventHandler
    public void respawnEvent(PlayerRespawnEvent e) {
        if (plugin.isStart) {
            if (e.getPlayer().getName() != plugin.surv) {
                e.getPlayer().getInventory().setItemInMainHand(Utils.buildItem(Material.COMPASS, 0, 1,
                        "&1Compass", new String[]{"   ", "Compass help you find survivor", "    "}));
            }
        }
    }


    @EventHandler
    public void joinEvent(PlayerJoinEvent e) {
        if (plugin.isStart) {
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
            if (e.getPlayer().getName() == plugin.surv) {
                Player p = e.getPlayer();
                Location portal = p.getLocation().getWorld().locateNearestStructure(p.getLocation(),
                        StructureType.RUINED_PORTAL, 500, true);
                p.setCompassTarget(portal);
            }
        }
    }
}
