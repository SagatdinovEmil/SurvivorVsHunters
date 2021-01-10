package code;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static code.Utils.*;


public class SurvVsHunts extends JavaPlugin {
    public String surv = "";
    public int stage = PRELUDE;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new Events(this), this);
        getCommand("startgame").setExecutor(new StartgameCmd(this));
    }
}
