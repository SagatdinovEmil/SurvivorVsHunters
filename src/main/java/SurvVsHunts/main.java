package SurvVsHunts;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.*;

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

    public String getTranslation(String name, Object... values) {
        String lang = getConfig().getString("lang");
        String format = getConfig().getString(lang + '.' + name);
        return translateAlternateColorCodes('&', String.format(format, values));
    }

    // Send message to all players
    public void say(String msg, Object... values) {
        String text = getTranslation(msg, values);
        getLogger().info(text);
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendMessage(text);
        }
    }

    // Send message to one player
    public void say(Player player, String msg, Object... values) {
        player.sendMessage(getTranslation(msg, values));
    }

    // Counts down the remaining time
    public void counter(int value, String msg, int next_stage) {
        if (value <= 1) {
            getServer().getScheduler().runTask(this, () -> {
                change_stage(next_stage);
            });
        } else {
            if (value <= 60*5) {
                if (value > 60) {
                    say(msg, getTranslation("minsec", value / 60, value % 60));
                } else {
                    say(msg, getTranslation("sec", value));
                }
            } else {
                say(msg, getTranslation("min", value / 60));
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
