package SurvVsHunts;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.ChatColor.*;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class main extends JavaPlugin {
    private Listener stage = null;
    public String surv_player = "";
    public org.bukkit.scheduler.BukkitTask task = null;
    // Game stages
    public final static int LOADING = 0;
    public final static int PRELUDE = 1;
    public final static int HANDICAP = 2; // can be HANDICAP -> END
    public final static int HUNTING = 3;
    public final static int END = 4;


    // Send message to all players
    public void say(String msg) {
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendMessage(translateAlternateColorCodes('&', msg));
        }
    }

    // Counts down the remaining time
    public void counter(int value, String msg, int next_stage) {
        if (value <= 1) {
            change_stage(next_stage);
        } else {
            if (value <= 60) {
                say(String.format(msg, value) + this.getConfig().getString("messages.sec"));
            } else {
                say(String.format(msg, value / 60) + this.getConfig().getString("messages.min"));
            }
            task = getServer().getScheduler().runTaskLaterAsynchronously(this, () -> { 
                counter(value / 2, msg, next_stage); 
            }, 20 * value / 2);
        }
    }

    public void change_stage(int stage_num) {
        HandlerList.unregisterAll(stage);
        if (task != null) task.cancel();
        switch (stage_num) {
            case PRELUDE:  this.stage = new stage1_prelude(this);  break;
            case HANDICAP: this.stage = new stage2_handicap(this); break;
            case HUNTING:  this.stage = new stage3_hunting(this);  break;
            case END:      this.stage = new stage4_end(this);      break;
        }
        Bukkit.getPluginManager().registerEvents(this.stage, this);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        change_stage(PRELUDE);
    }
}
