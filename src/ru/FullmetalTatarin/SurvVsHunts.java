package ru.FullmetalTatarin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SurvVsHunts extends JavaPlugin {
    public String surv = "";
    public boolean isStart = false;
    public boolean isHandicapEnds = false;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new Events(this), this);
        getCommand("svh").setExecutor(new SvhCmd());
        getCommand("startgame").setExecutor(new StartgameCmd(this));
    }
}
